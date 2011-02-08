package com.jwh.gwt.fasttable.client;

public class HeaderCell extends HtmlElement {

	public HeaderCell(String columnName, boolean sortable, StringBuilder builder) {
		super(builder);
		this.columnName = columnName;
		this.sortable = sortable;
	}
	enum SortState {None, Ascending, Descending}
	final String columnName;
	final boolean sortable;
	SortState sortState = SortState.None;
	@Override
	public String getTag() {
		return "th";
	}
}
