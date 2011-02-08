package com.jwh.gwt.fasttable.client;

import java.util.ArrayList;

import com.jwh.gwt.fasttable.client.HeaderCell.SortState;

public class HeaderRow extends HtmlElement {

	public HeaderRow(StringBuilder builder) {
		super(builder);
	}
	
	public HeaderCell newHeaderCell(String columnName, boolean sortable) {
		HeaderCell headerCell = new HeaderCell(columnName, sortable, builder);
		headerCells.add(headerCell);
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
}
