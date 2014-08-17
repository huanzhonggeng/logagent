/**
 * 
 */
package net.ghz.logagent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author huanzhong
 * @since 2014年8月17日
 */
public class FileUtils {

	public final static Logger logger = LoggerFactory
			.getLogger(FileUtils.class);
	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * @param name
	 * @param fileNamePattern
	 * @return
	 */
	public static boolean matchFileName(String name, String fileNamePattern) {
		return name.matches(fileNamePattern);
	}

	/**
	 * 查看已处理文件结果
	 * 
	 * @param resultFile
	 * @return
	 */
	public static List<FileResult> getProcessedFiles(String resultFile) {
		List<FileResult> results = new ArrayList<FileResult>();
		File src = new File(resultFile);
		if (!src.exists()) {
			if (!src.getParentFile().exists()) {
				src.getParentFile().mkdirs();
			}
			try {
				src.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return results;
		}
		if (!src.isFile()) {
			return results;
		}
		try {
			results = (List<FileResult>) mapper.readValue(src, List.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug("this result from file is {}", results);
		return results;
	}

	/**
	 * 扫描当前目录中的文件
	 * 
	 * @param src
	 * @param logSourceConfig
	 * @return
	 */
	public static List<FileResult> getNewFiles(File src,
			LogSourceConfig logSourceConfig) {
		List<FileResult> results = new ArrayList<FileResult>();
		if (src.isFile()) {
			if (!FileUtils.matchFileName(src.getName(),
					logSourceConfig.getFileNamePattern())) {
				return results;
			} else {
				FileResult e = new FileResult();
				e.setComplemented(false);
				e.setFileName(src.getName());
				e.setLastProcessTime(new DateTime(src.lastModified()).toDate());
				e.setParentPath(src.getParent());
				e.setPosition(0);
				results.add(e);
			}
		} else {
			File[] listFiles = src.listFiles();
			for (File f : listFiles) {
				results.addAll(getNewFiles(f, logSourceConfig));
			}
		}
		return results;
	}

	/**
	 * 合并已处理文件和待处理文件。
	 * 
	 * @param processedFiles
	 * @param newFilesmjnnjm
	 *            -jnhjmnj
	 * @return
	 */
	public static List<FileResult> joinFiles(List<FileResult> processedFiles,
			List<FileResult> newFiles) {
		ArrayList<FileResult> tmp = new ArrayList<FileResult>();
		for (FileResult f : newFiles) {
			for (FileResult ff : processedFiles) {
				if (f.getFileName().equalsIgnoreCase(ff.getFileName())
						&& ff.isComplemented()) {
					tmp.add(f);
				}
			}
		}
		// 去掉处理过的文件
		newFiles.removeAll(tmp);
		if (newFiles.size() <= 1) {
			return processedFiles;
		} else {
			FileResult old = null;
			for (FileResult ff : processedFiles) {
				if (!ff.isComplemented() && containDate(ff.getFileName())) {
					old = ff;
					break;
				}
			}
			processedFiles.remove(old);
			FileResult last = null;
			for (FileResult f : newFiles) {
				if (containDate(f.getFileName())) {
					if (last == null) {
						last = f;
					} else if (f.getFileName().compareTo(last.getFileName()) < 0) {
						last = f;
					}
				}
			}
			last.setPosition(old.getPosition());
			processedFiles.addAll(newFiles);
		}
		return processedFiles;
	}

	/**
	 * @param fileName
	 * @return
	 */
	public static boolean containDate(String fileName) {
		if (fileName != null && fileName.trim().length() > 0) {
			String[] split = fileName.split("_");
			if (split.length > 2) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param allFiles
	 * @param logSourceConfig
	 */
	public static void saveAllFileResults(List<FileResult> allFiles,
			LogSourceConfig logSourceConfig) {
		File file = new File(logSourceConfig.getResultFile());
		try {
			if (!file.exists() && !file.getParentFile().exists()) {
				file.mkdirs();
			}
			mapper.writeValue(file, allFiles);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
