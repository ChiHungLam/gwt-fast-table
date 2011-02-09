package com.jwh.gwt.fasttable.client;


public class HeaderRow extends HtmlElement {

	public HeaderRow(StringBuilder builder) {
		super(builder);
	}
	public GenericElement currentCell = null;
	
	public GenericElement newHeaderCell(String columnName) {
		closeOpeningTag();
		if (currentCell != null) {
			currentCell.cleanup();
		}
		final GenericElement headerCell = GenericElement.getHeaderCell(builder);
		currentCell = headerCell;
		return headerCell;
	}
	
	@Override
	public String getTag() {
		return "tr";
	}

	@Override
	public void cleanup() {
		closeOpeningTag();
		if (currentCell != null) {
			currentCell.cleanup();
		}
		super.cleanup();
	}
}
