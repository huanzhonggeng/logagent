/**
 * 
 */
package net.ghz.logagent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.flume.Context;
import org.apache.flume.EventDrivenSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.source.AbstractSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author huanzhong
 * created 2014年8月17日
 */
public class LogSource extends AbstractSource implements Configurable,
		EventDrivenSource {
	public final static Logger logger = LoggerFactory
			.getLogger(LogSource.class);
	/**
	 * 系统系统后首次等待时间。
	 */
	private static final long initialDelay = 20L;

	/**
	 * 配置文件
	 */
	private LogSourceConfig logSourceConfig;
	private ScheduledExecutorService newSingleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor();

	@Override
	public void configure(Context context) {
		logger.info("begin configure {}", this.getName());
		logSourceConfig = new LogSourceConfig(context);
	}

	@Override
	public synchronized void start() {
		logger.info("begin start {}", this.getName());

		long delay = logSourceConfig.getInterval();
		LogProcessTask logProcessTask = new LogProcessTask(logSourceConfig,
				this.getChannelProcessor(), this.getName());

		newSingleThreadScheduledExecutor.scheduleWithFixedDelay(
				logProcessTask, initialDelay, delay, TimeUnit.SECONDS);
		super.start();
	}

	@Override
	public synchronized void stop() {
		logger.info("begin stop {}", this.getName());
		newSingleThreadScheduledExecutor.shutdown();
		this.getChannelProcessor().close();
		
		super.stop();
	}

}
