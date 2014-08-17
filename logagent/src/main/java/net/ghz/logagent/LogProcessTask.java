/**
 * 
 */
package net.ghz.logagent;

import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import org.apache.flume.channel.ChannelProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author huanzhong
 * @since 2014年8月17日
 */
public class LogProcessTask extends Thread {

	public final static Logger logger = LoggerFactory
			.getLogger(LogProcessTask.class);

	private LogSourceConfig logSourceConfig;
	private ChannelProcessor channelProcessor;
	private String taskName;
	private ExecutorService threadPool;

	/**
	 * @param logSourceConfig
	 * @param channelProcessor
	 * @param name
	 */
	public LogProcessTask(LogSourceConfig logSourceConfig,
			ChannelProcessor channelProcessor, String taskName) {
		this.logSourceConfig = logSourceConfig;
		this.channelProcessor = channelProcessor;
		this.taskName = taskName;

		threadPool = java.util.concurrent.Executors
				.newFixedThreadPool(logSourceConfig.getThreads());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		logger.debug("begin to execute LogProcessTask {}", this.taskName);
		File src = new File(logSourceConfig.getSourceDirectory());
		if (!src.exists()) {
			logger.warn("{} not exists", src.getAbsolutePath());
			return;
		} else if (!src.isDirectory()) {
			logger.warn("{} is not directory", src.getAbsolutePath());
			return;
		} else if (!src.canRead()) {
			logger.warn("{} has no read permission", src.getAbsolutePath());
			return;
		}
		List<FileResult> newFiles = FileUtils.getNewFiles(src,this.logSourceConfig);

		List<FileResult> processedFiles = FileUtils.getProcessedFiles(logSourceConfig
				.getResultFile());

		List<FileResult> allFiles = FileUtils.joinFiles(processedFiles, newFiles);
		CountDownLatch countDownLatch = new CountDownLatch(
				allFiles.size());
		for (FileResult file : allFiles) {
			threadPool
					.execute(new LogFileProcessor(file, this.channelProcessor,
							this.logSourceConfig, countDownLatch));
		}
		
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		FileUtils.saveAllFileResults(allFiles, logSourceConfig);
		logger.debug("begin to execute LogProcessTask {}", this.taskName);
	}

	@Override
	public void interrupt() {
		threadPool.shutdown();
		super.interrupt();
	}
	
	
}
