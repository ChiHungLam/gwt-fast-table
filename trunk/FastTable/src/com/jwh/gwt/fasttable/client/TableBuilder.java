package com.jwh.gwt.fasttable.client;

import java.util.ArrayList;
import java.util.Comparator;

public abstract class TableBuilder<T> {

	Filter<T> defaultFilter = getDefaultFilter();
	Filter<T> filter = defaultFilter;

	public Filter<T> getDefaultFilter() {
		return new Filter<T>() {
			@Override
			public boolean isIncluded(T candidate) {
				return true;
			}
		};
	}

	ArrayList<T> allObjects = new ArrayList<T>();
	ArrayList<T> filteredObjects = new ArrayList<T>();

	/**
	 * Implement this if you want to show column headings (which can be sorted)
	 * 
	 * @param headerRow
	 */
	public void buildHeader(HeaderRow headerRow) {

	}

	/**
	 * Implement this if you want to show a table footer
	 * 
	 * @param headerRow
	 */
	public void buildFooter(Row row) {

	}

	public void setFilter(Filter<T> filter) {
		this.filter = filter;
	}

	Comparator<T> sorter = null;
	
	private void doFilter() {
		if (filter == defaultFilter) {
			filteredObjects = allObjects;
		} else {
			filteredObjects.clear();
			ArrayList<T> allObjects2 = allObjects;
			for (T t : allObjects2) {
				if (this.filter.isIncluded(t)) {
					filteredObjects.add(t);
				}
			}
		}
	}
	
	protected Table<T> table = new Table<T>();
	
	public String getHtml() {
		doFilter();
		doSort();
		table.reset();
		buildHeaderPrivate();
		for (T t : filteredObjects) {
			Row row = table.newRow();
			table.register(t, row);
			populateRowCells(t, row);
		}
		table.cleanupCurrentRow();
		buildFooterPrivate();
		return table.toString();
	}

	/**
	 * @param t the domain object underlying the row
	 * @param row Add cells to the row
	 */
	protected abstract void populateRowCells(T t, Row row);

	private void buildFooterPrivate() {
		final StringBuilder b = new StringBuilder();
		HeaderRow headerRow = new HeaderRow(b);
		int length = b.length();
		buildHeader(headerRow);
		if (length != b.length()) {
			headerRow.cleanup();
			GenericElement tfoot = GenericElement.getTableFooter(table.builder);
			tfoot.setContentsRaw(b.toString());
			tfoot.cleanup();
		}
		
	}

	private void buildHeaderPrivate() {
		final StringBuilder b = new StringBuilder();
		HeaderRow headerRow = new HeaderRow(b);
		buildHeader(headerRow);
		if (headerRow.getColumnCount()!= 0) {
			headerRow.cleanup();
			GenericElement thead = GenericElement.getTableHeader(table.builder);
			thead.setContentsRaw(b.toString());
			thead.cleanup();		
		}
	}

	private void doSort() {
		// TODO Auto-generated method stub
		
	}

}
