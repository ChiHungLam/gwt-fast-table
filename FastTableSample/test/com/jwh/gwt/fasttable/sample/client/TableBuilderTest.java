package com.jwh.gwt.fasttable.sample.client;

import org.junit.Test;

import com.google.gwt.user.client.ui.Panel;
import com.jwh.gwt.fasttable.client.TableBuilder;
import com.jwh.gwt.html.shared.Tag;
import com.jwh.gwt.html.shared.event.CellEvent;
import com.jwh.gwt.html.shared.event.CellListener;
import com.jwh.gwt.html.shared.util.HtmlFactory.HtmlElement;


public class TableBuilderTest implements SampleStyle {

	@Test
	public void test() throws Exception {
		final TableBuilder<SampleModel> builder = new TableBuilder<SampleModel>() {

			@Override
			public void buildFooter(HtmlElement row) {
				row.addChild(Tag.td).addContents(NAME);
				row.addChild(Tag.td).addContents(BORDER_OPEN_RIGHT);
				row.addChild(Tag.td).addContents(BORDER_OPEN_LEFT_RIGHT);
				row.addChild(Tag.td).addContents(BORDER_OPEN_LEFT);
				row.addChild(Tag.td).addContents(BORDER);
			}
			
			public void buildHeader(TableBuilder<SampleModel>.SortableHeaderRow headerRow) {
				headerRow.newHeaderCell();
				headerRow.newHeaderCell();
				headerRow.newHeaderCell();
				headerRow.newHeaderCell();
				headerRow.newHeaderCell();
			}
			
			@Override
			protected void populateRowCells(SampleModel t, HtmlElement row, String refId, int rowNumber) {
				CellListener<SampleModel> listener = buildCellListener();
//				CellHandlerWrapper<SampleModel> wrapper = table.registerCellHandler(listener, OnEvent.onMouseOver, OnEvent.onMouseOut);
				row.addChild(Tag.td).setStyle(NAME).addContents(t.name); //.addHandler(wrapper, refId, 0);
				row.addChild(Tag.td).setStyle(BORDER_OPEN_RIGHT).addContents(t.street);
				row.addChild(Tag.td).setStyle(BORDER_OPEN_LEFT_RIGHT).addContents(t.city);
				row.addChild(Tag.td).setStyle(BORDER_OPEN_LEFT).addContents(t.state);
				row.addChild(Tag.td).setStyle(BORDER).addContents(t.zip); // .addHandler(wrapper, refId, 0);				
			}

			private CellListener<SampleModel> buildCellListener() {
				return new CellListener<SampleModel>() {					
					@Override
					public void handlerCellEvent(CellEvent<SampleModel> cellEvent) {
						switch (cellEvent.getOnEvent()) {
						case onMouseOver:
							System.out.println("Mouse Over");
							break;
						case onMouseOut:
							System.out.println("Mouse Out");
							break;
						default:
							break;
						}						
					}
				};
			}
			@Override
			public Panel getContainingElement() {
				return null;
			}};
			builder.setContents(SampleModel.getTestSamples(1));
			String html = builder.getHtml();
			builder.table.reset();
			html = builder.getHtml();
			System.out.println(html);
	}
}
