package com.jwh.gwt.fasttable.client.element;

public class GenericElement extends HtmlElement {

	public static GenericElement getCell(StringBuilder builder) {
		return new GenericElement("td", builder);
	}

	public static GenericElement getHeaderCell(StringBuilder builder) {
		return new GenericElement("th", builder);
	}

	public static GenericElement getTableBody(StringBuilder builder) {
		final GenericElement tbody = new GenericElement("tbody", builder);
		// configure for locked header
		tbody.addAttribute("style", "overflow:auto;");
		return tbody;
	}

	public static GenericElement getTableFooter(StringBuilder builder) {
		return new GenericElement("tfoot", builder);
	}

	public static GenericElement getTableHeader(StringBuilder builder) {
		return new GenericElement("thead", builder);
	}

	final String tag;

	public GenericElement(String tag, StringBuilder builder) {
		super(builder, tag);
		this.tag = tag;
	}

	@Override
	public String getTag() {
		return tag;
	}

}