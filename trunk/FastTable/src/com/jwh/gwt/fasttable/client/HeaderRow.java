package com.jwh.gwt.fasttable.client;

import java.util.ArrayList;

import com.jwh.gwt.fasttable.client.HeaderCell.SortState;

public class HeaderRow extends HtmlElement {

	public HeaderRow(StringBuilder builder) {
		super(builder);
	}
	HeaderCell currentCell = null;
	
	public HeaderCell newHeaderCell(String columnName, boolean sortable) {
		if (currentCell != null) {
			currentCell.cleanup();
		}
		HeaderCell headerCell = new HeaderCell(columnName, builder, sortable);
		headerCell.addContents(columnName);
		headerCells.add(headerCell);
		currentCell = headerCell;
		return headerCell;
	}

	ArrayList<HeaderCell> headerCells = new ArrayList<HeaderCell>();
	
	/**
	 * Use this if you know the sort state of the column
	 * @param columnName
	 * @param sortable
	 * @param sortState
	 * @return
	 */
	public HeaderCell newHeaderCell(String columnName, boolean sortable, SortState sortState) {
		HeaderCell headerCell = newHeaderCell(columnName, sortable);
		headerCell.sortState = sortState;
		return headerCell;
	}

	@Override
	public String getTag() {
		return "tr";
	}

	public int getColumnCount() {
		return headerCells.size();
	}
	@Override
	public void cleanup() {
		if (currentCell != null) {
			currentCell.cleanup();
		}
		super.cleanup();
	}
}
