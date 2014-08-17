/**
 * 
 */
package net.ghz.logagent;

import java.util.concurrent.CountDownLatch;

import org.apache.flume.channel.ChannelProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author huanzhong.
 * @since 2014年8月17日
 */
public class LogFileProcessor extends Thread {
	public final static Logger logger = LoggerFactory
			.getLogger(LogFileProcessor.class);
	private FileResult file;
	private ChannelProcessor channelProcessor2;
	private LogSourceConfig logSourceConfig;
	private CountDownLatch countDownLatch;

	/**
	 * @param file
	 * @param channelProcessor
	 * @param logSourceConfig
	 * @param countDownLatch
	 */
	public LogFileProcessor(FileResult file, ChannelProcessor channelProcessor,
			LogSourceConfig logSourceConfig, CountDownLatch countDownLatch) {
		this.file = file;
		channelProcessor2 = channelProcessor;
		this.logSourceConfig = logSourceConfig;
		this.countDownLatch = countDownLatch;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		countDownLatch.countDown();
	}

	@Override
	public void interrupt() {
		countDownLatch.countDown();
		super.interrupt();
	}

}
