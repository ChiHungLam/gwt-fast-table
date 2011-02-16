package com.jwh.gwt.fasttable.client;

import java.util.ArrayList;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.jwh.gwt.fasttable.client.stream.HtmlFactory;
import com.jwh.gwt.fasttable.client.stream.HtmlFactory.HtmlElement;
import com.jwh.gwt.fasttable.client.stream.HtmlFactory.Tag;
import com.jwh.gwt.fasttable.client.util.IdGenerator;

/**
 * Because only a limited number of table elements are available at a time
 * 
 * @author jheyne
 * 
 * @param <T>
 */
public class IncrementalBuilder<T> {

	boolean cancelled = false;

	public void cancel() {
		this.cancelled = false;
	}

	public IncrementalBuilder(TableBuilder<T> builder, ArrayList<T> items, String refId, int startIndex) {
		super();
		this.tableBuilder = builder;
		this.items = items;
		this.refId = refId;
		this.startIndex = startIndex;
		this.tableBuilder.setIncrementalBuilder(this);
	}

	final static int buildCount = 50;
	final int startIndex;
	final TableBuilder<T> tableBuilder;
	final ArrayList<T> items;
	/**
	 * The id of the element where the items should be inserted
	 */
	final String refId;
	final private String myId = IdGenerator.getNextId();

	public void build() {
		generateHtml();
		if (cancelled) {
			return;
		}
		scheduleInsert();
	}

	int scheduleWaitCount = 0;

	String html;

	private void scheduleInsert() {
		System.out.println("scheduling insert");
		final Element previousTBody = getPreviousTBody();
		if (!cancelled && previousTBody == null && scheduleWaitCount < 30) {
			final Timer timer = new Timer() {
				@Override
				public void run() {
					IncrementalBuilder.this.scheduleInsert();
				}
			};
			System.out.println("waiting " + scheduleWaitCount);
			scheduleWaitCount++;
			System.out.println("waiting");
			timer.schedule(50);
		} else {
			if (!cancelled && previousTBody != null) {
				insertHtml();
			} else {
				System.err.println("Waited too long");
			}
		}

	}

	private Element getPreviousTBody() {
		return tableBuilder.getDocument().getElementById(refId);
	}

	/**
	 * Create a new incremental builder if there are any items remaining
	 */
	private void buildRemainingItems() {
		if (lastId == null || startIndex + buildCount >= items.size()) {
			cleanup();
			return;
		}
		if (cancelled) {
			return;
		}
		new IncrementalBuilder<T>(tableBuilder, items, myId, startIndex + buildCount).build();
	}

	/**
	 * We are done. Cleanup any "still building" messages
	 */
	private void cleanup() {
	}

	private void insertHtml() {
		final Element lastItem = Document.get().getElementById(refId);
		final com.google.gwt.user.client.Element tBody = DOM.createTBody();
		tBody.setId(myId);
		try {
			lastItem.getParentNode().insertAfter(tBody, lastItem);
			tBody.setInnerHTML(html);
		} catch (Exception e) {
//			for IE
			tableBuilder.setUseIncrementalBuild(false);
		}
		if (cancelled) {
			return;
		}
		buildRemainingItems();
	}

	String lastId = null;

	private void generateHtml() {
		final HtmlElement tbody = HtmlFactory.forRoot(Tag.tbody);
		for (int i = startIndex; i < Math.min(items.size(), startIndex + buildCount); i++) {
			final T object = items.get(i);
			final HtmlElement row = tbody.addChild(Tag.tr);
			lastId = tableBuilder.table.register(object, row);
			tableBuilder.populateRowCells(object, row, lastId);
			row.cleanup();
		}
		html = tbody.toHtml();
		html = HtmlFactory.trimTag(html, Tag.tbody);
	}
}
