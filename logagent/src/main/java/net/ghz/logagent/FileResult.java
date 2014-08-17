/**
 * 
 */
package net.ghz.logagent;

import java.util.Date;

/**
 * @author huanzhong.
 * @since  2014年8月17日
 */
public class FileResult {
	/**
	 * 文件名
	 */
	private String fileName;
	/**
	 * 文件所在路径
	 */
	private String parentPath;
	/**
	 * 处理位置
	 */
	private long position;
	/**
	 * 是否完成
	 */
	private boolean complemented;
	/**
	 * 处理时间
	 */
	private Date lastProcessTime;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	public long getPosition() {
		return position;
	}

	public void setPosition(long position) {
		this.position = position;
	}

	public boolean isComplemented() {
		return complemented;
	}

	public void setComplemented(boolean complemented) {
		this.complemented = complemented;
	}

	public Date getLastProcessTime() {
		return lastProcessTime;
	}

	public void setLastProcessTime(Date lastProcessTime) {
		this.lastProcessTime = lastProcessTime;
	}

	@Override
	public String toString() {
		return "FileResult [fileName=" + fileName + ", parentPath="
				+ parentPath + ", position=" + position + ", complemented="
				+ complemented + ", lastProcessTime=" + lastProcessTime + "]";
	}

}
