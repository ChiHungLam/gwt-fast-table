package com.jwh.gwt.fasttable.client;

import static junit.framework.Assert.*;

import org.junit.Test;

import com.jwh.gwt.fasttable.client.element.HtmlFactory;
import com.jwh.gwt.fasttable.client.element.HtmlFactory.HtmlElement;
import com.jwh.gwt.fasttable.client.element.HtmlFactory.Tag;

public class HtmlFactoryTest {

	@Test
	public void testRoot() throws Exception {
		final HtmlElement table = HtmlFactory.forRoot(HtmlFactory.Tag.table);
		assertEquals("<table ></table>", table.toHtml());
		System.out.println(table.toHtml());
	}

	@Test
	public void testID() throws Exception {
		final HtmlElement table = HtmlFactory.forRoot(HtmlFactory.Tag.table);
		table.setId("MyId");
		assertTrue("Expected ID", table.toHtml().indexOf("id=\"MyId\"") > 0);
		System.out.println(table.toHtml());
	}

	@Test
	public void testAttribute() throws Exception {
		final HtmlElement table = HtmlFactory.forRoot(HtmlFactory.Tag.table);
		table.addAttribute("for", "ABC");
		assertTrue("Expected Attribute", table.toHtml().indexOf("for=\"ABC\"") > 0);
		System.out.println(table.toHtml());
	}

	@Test
	public void testStyle() throws Exception {
		final HtmlElement table = HtmlFactory.forRoot(HtmlFactory.Tag.table);
		table.setStyle("ABC", "CDE");
		assertTrue("Expected Style", table.toHtml().indexOf("class=\"ABC") > 0);
		System.out.println(table.toHtml());
	}

	@Test
	public void testContent() throws Exception {
		final HtmlElement table = HtmlFactory.forRoot(HtmlFactory.Tag.table);
		table.addContents("ABC");
		assertTrue("Expected Contents", table.toHtml().indexOf(">ABC<") > 0);
		System.out.println(table.toHtml());
	}

	@Test
	public void testChild() throws Exception {
		final HtmlElement table = HtmlFactory.forRoot(HtmlFactory.Tag.table);
		table.addChild(Tag.tbody);
		assertTrue("Expected Tbody", table.toHtml().indexOf("tbody") > 0);
		assertTrue("Expected closing table tag", table.toHtml().indexOf("</table>") > 0);
		System.out.println(table.toHtml());
	}

	@Test
	public void testMultipleChild() throws Exception {
		final HtmlElement table = HtmlFactory.forRoot(HtmlFactory.Tag.table);
		final HtmlElement tbody = table.addChild(Tag.tbody);
		final HtmlElement row = tbody.addChild(Tag.tr);
		for (int i = 0; i < 4; i++) {
			final HtmlElement child = row.addChild(Tag.td);
			child.addContents(String.valueOf(i));
		}
		assertTrue("Expected multiple children",
				table.toHtml().indexOf("<td >0</td><td >1</td><td >2</td><td >3</td>") > 0);
		assertTrue("Expected closing sequence",
				table.toHtml().indexOf("<td >3</td></tr></tbody></table>") > 0);
		System.out.println(table.toHtml());
	}

	@Test
	public void testGrandChild() throws Exception {
		final HtmlElement table = HtmlFactory.forRoot(HtmlFactory.Tag.table);
		final HtmlElement tbody = table.addChild(Tag.tbody);
		tbody.addChild(Tag.tr);
		assertTrue("Expected Tbody", table.toHtml().indexOf("</tbody>") > 0);
		assertTrue("Expected Tr", table.toHtml().indexOf("</tr>") > 0);
		assertTrue("Expected closing table tag", table.toHtml().indexOf("</table>") > 0);
		System.out.println(table.toHtml());
	}
}
