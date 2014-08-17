/**
 * 
 */
package net.ghz.logagent;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.flume.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author huanzhong. <br>
 * @since 2014年8月17日
 */
public class LogSourceConfig {

	public final static Logger logger = LoggerFactory
			.getLogger(LogSourceConfig.class);

	private long interval = 10;

	private String sourceDirectory;

	private String resultFile;

	private int threads;

	private String fileNamePattern;

	/**
	 * 根据上下文进行配置属性初始化。
	 * 
	 * @param context
	 */
	public LogSourceConfig(Context context) {
		if (context.getString("interval") != null) {
			this.interval = NumberUtils.toLong(context.getString("interval"));
		}
		logger.info("init config interval is {}", this.interval);

		if (context.getString("sourceDirectory") != null) {
			this.sourceDirectory = context.getString("sourceDirectory");
		}
		logger.info("init config sourceDirectory is {}", this.sourceDirectory);

		if (context.getString("resultFile") != null) {
			this.resultFile = context.getString("resultFile");
		}
		logger.info("init config resultFile is {}", this.resultFile);

		if (context.getString("threads") != null) {
			this.threads = NumberUtils.toInt(context.getString("threads"));
		}
		logger.info("init config threads is {}", this.threads);
		
		if (context.getString("fileNamePattern") != null) {
			this.fileNamePattern = context.getString("fileNamePattern");
		}
		logger.info("init config fileNamePattern is {}", this.fileNamePattern);
	}

	/**
	 * 日志扫描时间间隔，默认10s
	 * 
	 * @return
	 */
	public long getInterval() {
		return interval;
	}

	/**
	 * 日志所在目录
	 */
	public String getSourceDirectory() {
		return sourceDirectory;
	}

	public String getResultFile() {
		return resultFile;
	}

	/**
	 * @return
	 */
	public int getThreads() {
		// TODO Auto-generated method stub
		return threads;
	}

	/**
	 * @return
	 */
	public String getFileNamePattern() {
		return fileNamePattern;
	}

}
