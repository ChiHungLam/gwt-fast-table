package com.jwh.gwt.fasttable.client.util;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Logger {

	public Logger(Panel parentPanel) {
		super();
		setParent(parentPanel);
	}
	public void setParent(Panel parentPanel) {
		parentPanel.add(panel);
	}
	public Logger() {
	}
	final VerticalPanel panel = new VerticalPanel();
	double lastTime = new Date().getTime();
	private double elapsedTime;
	public void clear() {
		panel.clear();
		lastTime = 0;
	}
	public void logInfo(Object... message) {
		log(message, Style.LOG_SUCCESS);
	}
	private void log(Object[] message, String style) {		
		final StringBuilder b = new StringBuilder();
		String timeStamp = getTimeStamp();
		b.append(timeStamp);
		b.append(" - ");
		for (Object string : message) {
			b.append(string);
			b.append(' ');
		}
		Label label = new Label(b.toString());
		label.addStyleName(style);
		panel.add(label);
		label.setTitle(elapsedTime + " ms since last entry (or initialization");
	}
	private String getTimeStamp() {
		final Date date = new Date();
		final long timeNow = date.getTime();
		elapsedTime = timeNow - lastTime;
		lastTime = timeNow;
		final DateTimeFormat format = DateTimeFormat.getFormat(PredefinedFormat.TIME_MEDIUM);
		return format.format(date);
	}
	public void logError(Object... message) {
		log(message, Style.LOG_ERROR);
	}
}
