package com.jwh.gwt.fasttable.client;


public class HeaderCell extends HtmlElement {

	final private boolean sortable;

	public HeaderCell(String columnName, StringBuilder builder, boolean sortable) {
		super(builder);
		this.columnName = columnName;
		this.sortable = sortable;
	}

	enum SortState {
		None, Ascending, Descending
	}

	final String columnName;
	SortState sortState = SortState.None;

	@Override
	public String getTag() {
		return "th";
	}

	public boolean isSortable() {
		return sortable;
	}
}
