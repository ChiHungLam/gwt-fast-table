package com.jwh.gwt.fasttable.client.element;

public class HeaderRow extends HtmlElement {

	public GenericElement currentCell = null;

	public HeaderRow(StringBuilder builder) {
		super(builder);
	}

	@Override
	public void cleanup() {
		closeOpeningTag();
		if (currentCell != null) {
			currentCell.cleanup();
		}
		super.cleanup();
	}

	@Override
	public String getTag() {
		return "tr";
	}

	public GenericElement newHeaderCell() {
		closeOpeningTag();
		if (currentCell != null) {
			currentCell.cleanup();
		}
		final GenericElement headerCell = GenericElement.getHeaderCell(builder);
		currentCell = headerCell;
		return headerCell;
	}
}
