package com.jwh.gwt.fasttable.client;

public class GenericElement extends HtmlElement {

	public static GenericElement getCell(StringBuilder builder) {
		return new GenericElement("td", builder);
	}
	public static GenericElement getTableHeader(StringBuilder builder) {
		return new GenericElement("thead", builder);
	}
	public static GenericElement getTableBody(StringBuilder builder) {
		return new GenericElement("tbody", builder);
	}
	public static GenericElement getTableFooter(StringBuilder builder) {
		return new GenericElement("tfoot", builder);
	}
	public GenericElement(String tag, StringBuilder builder) {
		super(builder);
		this.tag = tag;
	}

	final String tag;
	
	@Override
	public String getTag() {
		return tag;
	}

}
