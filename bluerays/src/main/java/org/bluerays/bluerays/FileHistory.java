package org.bluerays.bluerays;

public class FileHistory {

	private long lineRead=0;
	private int lineSkiped=0;
	private int  modified=1;
	private long offsetRead=0;


	public long getOffsetRead() {
		return offsetRead;
	}
	public void setOffsetRead(long offsetRead) {
		this.offsetRead = offsetRead;
	}
	private boolean lineReading=false;

	public long getLineRead() {
		return lineRead;
	}
	public void setLineRead(long lineRead) {
		this.lineRead = lineRead;
	}
	public int getLineSkiped() {
		return lineSkiped;
	}
	public void setLineSkiped(int lineSkiped) {
		this.lineSkiped = lineSkiped;
	}
	public boolean isLineReading() {
		return lineReading;
	}
	public void setLineReading(boolean lineReading) {
		this.lineReading = lineReading;
	}
	public int getModified() {
		return modified;
	}
	public void setModified(int modified) {
		this.modified = modified;
	}



}
