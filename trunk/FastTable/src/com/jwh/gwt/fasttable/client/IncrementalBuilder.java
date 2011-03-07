package com.jwh.gwt.fasttable.client;

import java.util.ArrayList;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.DOM;
import com.jwh.gwt.html.shared.Tag;
import com.jwh.gwt.html.shared.util.HtmlFactory;
import com.jwh.gwt.html.shared.util.IdGenerator;
import com.jwh.gwt.html.shared.util.HtmlFactory.HtmlElement;

/**
 * Because only a limited number of table elements are available at a time
 * 
 * @author jheyne
 * 
 * @param <T>
 */
public class IncrementalBuilder<T> {

	boolean cancelled = false;

	int startIndex;

	final TableBuilder<T> tableBuilder;
	final ArrayList<T> items;
	/**
	 * The id of the element where the items should be inserted
	 */
	String refTbodyId;
	private String newTbodyId = IdGenerator.getNextId();
	int scheduleWaitCount = 0;

	/**
	 * The last built row
	 */
	String lastRowId = null;

	public IncrementalBuilder(TableBuilder<T> builder, ArrayList<T> items, String refId, int startIndex) {
		super();
		this.tableBuilder = builder;
		this.items = items;
		this.refTbodyId = refId;
		this.startIndex = startIndex;
	}

	public void build() {
		final Scheduler.RepeatingCommand repeatingCommand = new Scheduler.RepeatingCommand() {
			@Override
			public boolean execute() {
				final String html = generateHtml();
				if (cancelled) {
					tableBuilder.logInfo("cancelled incremental build");
					return false;
				}
				final Element previousTBody = getPreviousTBody();
				if (previousTBody != null) {
					tableBuilder.logInfo("inserting html - " + startIndex);
					final com.google.gwt.user.client.Element tBody = DOM.createTBody();
					newTbodyId = IdGenerator.getNextId();
					tBody.setId(newTbodyId);
					tBody.setInnerHTML(html);
					insertHtml(previousTBody, tBody);
				} else {
					// TODO repeat some number of times?
					tableBuilder.logError("Abandoning insert, cannot find previous tbody - " + startIndex);
					cancelled = true;
				}
				return !cancelled && startIndex < items.size();
			}

		};
		 Scheduler.get().scheduleFixedDelay(repeatingCommand, 5);
//		Scheduler.get().scheduleIncremental(repeatingCommand);
	}

	private int getSubsequentIncrement() {
		return tableBuilder.configuration.getSubsequentIncrement();
	}

	public void cancel() {
		this.cancelled = true;
	}

	private String generateHtml() {
		final HtmlElement tbody = HtmlFactory.forRoot(Tag.tbody);
		final int stopIndex = Math.min(items.size(), startIndex + getSubsequentIncrement());
		for (int i = startIndex; i < stopIndex; i++) {
			final T object = items.get(i);
			final HtmlElement row = tbody.addChild(Tag.tr);
			lastRowId = tableBuilder.table.register(object, row);
			tableBuilder.populateRowCells(object, row, lastRowId, i);
			row.cleanup();
		}
		String html = tbody.toHtml();
		html = HtmlFactory.trimTag(html, Tag.tbody);
		return html;
	}

	private Element getPreviousTBody() {
		return tableBuilder.getDocument().getElementById(refTbodyId);
	}

	private void insertHtml(Node previousTBody, Node tBody) {
		try {
			final Node table = previousTBody.getParentNode();
			table.insertAfter(tBody, previousTBody);
			startIndex += getSubsequentIncrement();
			refTbodyId = newTbodyId;
		} catch (final Exception e) {
			tableBuilder.logError("Cancelling. Error inserting html: " + e.getMessage());
			cancelled = true;
		}
	}

}
