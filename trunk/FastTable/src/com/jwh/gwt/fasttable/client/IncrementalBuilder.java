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

	final int startIndex;

	final TableBuilder<T> tableBuilder;
	final ArrayList<T> items;
	/**
	 * The id of the element where the items should be inserted
	 */
	final String refId;
	final private String myId = IdGenerator.getNextId();
	int scheduleWaitCount = 0;
	String html;

	String lastId = null;

	public IncrementalBuilder(TableBuilder<T> builder, ArrayList<T> items, String refId, int startIndex) {
		super();
		this.tableBuilder = builder;
		this.items = items;
		this.refId = refId;
		this.startIndex = startIndex;
		this.tableBuilder.setIncrementalBuilder(this);
	}

	public void build() {
		generateHtml();
		if (cancelled) {
			return;
		}
		scheduleInsert();
	}

	/**
	 * Create a new incremental builder if there are any items remaining
	 */
	private void buildRemainingItems() {
		final int subsequentIncrement = tableBuilder.configuration.getSubsequentIncrement();
		if (lastId == null || startIndex + subsequentIncrement >= items.size()) {
			cleanup();
			return;
		}
		if (cancelled) {
			return;
		}
		new IncrementalBuilder<T>(tableBuilder, items, myId, startIndex + subsequentIncrement).build();
	}

	public void cancel() {
		this.cancelled = false;
	}

	/**
	 * We are done. Cleanup any "still building" messages
	 */
	private void cleanup() {
	}

	private void generateHtml() {
		final HtmlElement tbody = HtmlFactory.forRoot(Tag.tbody);
		final int subsequentIncrement = tableBuilder.configuration.getSubsequentIncrement();
		for (int i = startIndex; i < Math.min(items.size(), startIndex + subsequentIncrement); i++) {
			final T object = items.get(i);
			final HtmlElement row = tbody.addChild(Tag.tr);
			lastId = tableBuilder.table.register(object, row);
			tableBuilder.populateRowCells(object, row, lastId, i);
			row.cleanup();
		}
		html = tbody.toHtml();
		html = HtmlFactory.trimTag(html, Tag.tbody);
	}

	private Element getPreviousTBody() {
		return tableBuilder.getDocument().getElementById(refId);
	}

	private void insertHtml(Node previousTBody, Node tBody) {
		try {
			if (tableBuilder.configuration.getIncrementalStrategy() == IncrementalStrategy.APPEND) {
				previousTBody.getParentNode().insertAfter(tBody, previousTBody);
			} else {
				previousTBody.getParentNode().replaceChild(tBody, previousTBody);
			}
		} catch (final Exception e) {
			// for IE
			tableBuilder.logError("Error inserting html: " + e.getMessage());
			tableBuilder.setUseIncrementalBuild(IncrementalStrategy.NONE, true);
			return;
		}
		if (cancelled) {
			return;
		}
		buildRemainingItems();
	}

	private void scheduleInsert() {
		tableBuilder.logInfo("scheduling insert for " + startIndex);
		final Element previousTBody = getPreviousTBody();
		if (!cancelled && previousTBody == null && scheduleWaitCount < 30) {
			tableBuilder.logInfo("waiting to insert " + scheduleWaitCount);
			final Scheduler.ScheduledCommand command = new Scheduler.ScheduledCommand() {

				@Override
				public void execute() {
					IncrementalBuilder.this.scheduleInsert();
				}
			};
			scheduleWaitCount++;
			Scheduler.get().scheduleDeferred(command);
		} else {
			if (cancelled) {
				tableBuilder.logInfo("Incremental insert cancelled - " + startIndex);
			} else {
				if (previousTBody != null) {
					tableBuilder.logInfo("inserting html - " + startIndex);
					final com.google.gwt.user.client.Element tBody = DOM.createTBody();
					tBody.setId(myId);
					tBody.setInnerHTML(html);
					insertHtml(previousTBody, tBody);
				} else {
					tableBuilder.logError("Abandoning insert, waited too long - " + startIndex);
				}
			}
		}

	}
}
