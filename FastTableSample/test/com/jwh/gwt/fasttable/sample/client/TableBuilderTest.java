package com.jwh.gwt.fasttable.sample.client;

import org.junit.Test;

import com.jwh.gwt.fasttable.client.CellEvent;
import com.jwh.gwt.fasttable.client.CellListener;
import com.jwh.gwt.fasttable.client.HeaderRow;
import com.jwh.gwt.fasttable.client.Row;
import com.jwh.gwt.fasttable.client.TableBuilder;


public class TableBuilderTest implements Style {

	@Test
	public void test() throws Exception {
		final TableBuilder<SampleModel> builder = new TableBuilder<SampleModel>() {

			@Override
			public void buildFooter(Row row) {
				row.newCell().addContents(NAME);
				row.newCell().addContents(STREET);
				row.newCell().addContents(CITY);
				row.newCell().addContents(STATE);
				row.newCell().addContents(ZIP);
			}
			@Override
			public void buildHeader(HeaderRow headerRow) {
				CellListener<SampleModel> sortListener = buildSortListener();
				headerRow.newHeaderCell(NAME, true);
				headerRow.newHeaderCell(STREET, true);
				headerRow.newHeaderCell(CITY, true);
				headerRow.newHeaderCell(STATE, true);
				headerRow.newHeaderCell(ZIP, true);
			}
			private CellListener<SampleModel> buildSortListener() {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			protected void populateRowCells(SampleModel t, Row row, String refId) {
				CellListener<SampleModel> listener = buildCellListener();
//				CellHandlerWrapper<SampleModel> wrapper = table.registerCellHandler(listener, OnEvent.onMouseOver, OnEvent.onMouseOut);
				row.newCell().setStyle(NAME).addContents(t.name); //.addHandler(wrapper, refId, 0);
				row.newCell().setStyle(STREET).addContents(t.street);
				row.newCell().setStyle(CITY).addContents(t.city);
				row.newCell().setStyle(STATE).addContents(t.state);
				row.newCell().setStyle(ZIP).addContents(t.zip); // .addHandler(wrapper, refId, 0);				
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
			}};
			builder.setContents(SampleModel.getTestSamples(1));
			String html = builder.getHtml();
			System.out.println(html);
	}
}
