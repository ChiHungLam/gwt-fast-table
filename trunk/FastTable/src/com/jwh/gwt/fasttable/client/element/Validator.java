package com.jwh.gwt.fasttable.client.element;

import com.jwh.gwt.fasttable.client.element.HtmlFactory.Tag;

public class Validator {

	public static boolean validateChild(Tag parent, Tag child) {
		switch (parent) {
		case input:
			return false;
		case label:
			return false;
		case table:
			return child == Tag.tbody || child == Tag.thead || child == Tag.tfoot || child == Tag.tr;
		case tbody:
			return child == Tag.tr;
		case td:
			return true;
		case tfoot:
			return false;
		case th:
			return child == Tag.td;
		case thead:
			return child == Tag.th;
		case tr:
			return child == Tag.td || child == Tag.th;
		default:
			break;
		}
		return true;
	}
}
