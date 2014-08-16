/**
 * 
 */
package net.ghz.logagent;

import org.apache.flume.Context;
import org.apache.flume.EventDrivenSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.source.AbstractSource;

/**
 * @author huanzhong
 *
 */
public class LogSource extends AbstractSource implements Configurable,
		EventDrivenSource {

	@Override
	public void configure(Context context) {
		// TODO Auto-generated method stub

	}

}
