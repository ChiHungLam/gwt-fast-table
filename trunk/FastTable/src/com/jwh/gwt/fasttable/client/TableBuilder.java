package com.jwh.gwt.fasttable.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.ui.Panel;
import com.jwh.gwt.fasttable.client.CellEvent.OnEvent;

public abstract class TableBuilder<T> {

	public enum SortAction {
		None, Ascending, Descending
	}

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

	HashMap<Integer, SortAction> currentSortAction = new HashMap<Integer, TableBuilder.SortAction>();

	/**
	 * We cache the initial sort action, and toggle the action when sort is invoked. 
	 * @param column 
	 * @return The sort action to invoke if the column header is clicked
	 */
	public SortAction getCachedSortAction(int column) {
		SortAction sortAction = currentSortAction.get(column);
		if (sortAction == null) {
			sortAction = getSortAction(column);
			currentSortAction.put(column, sortAction);
		}
		return sortAction;
	}
	
	/**
	 * Override this if you want some columns to sort when clicked in the
	 * header. The default does not sort.
	 * 
	 * @param column
	 *            The column of interest
	 * @return The action to take if the header is clicked
	 */
	public SortAction getSortAction(int column) {
		return SortAction.None;
	}

	/**
	 * Override to sort a column. The default does nothing.
	 * @param sortMe TODO
	 * @param column
	 *            The column to sort
	 * @param action
	 *            The sorting action to execute;
	 */
	public void sort(ArrayList<T> sortMe, int column, SortAction action) {

	}

	ArrayList<T> allObjects = new ArrayList<T>();
	ArrayList<T> filteredObjects = new ArrayList<T>();
	
	/**
	 * Implemented as an inner class so it can have access to sorting services
	 */
	public class SortableHeaderRow extends HeaderRow {
		public SortableHeaderRow(StringBuilder builder) {
			super(builder);
		}
		CellHandlerWrapper<T> sortHandler = null;
		public CellHandlerWrapper<T> getSortListener() {
			if (sortHandler == null) {
				final CellListener<T> sortListener = new CellListener<T>() {
					@Override
					public void handlerCellEvent(CellEvent<T> sortEvent) {
						getContainingElement().addStyleName(Style.SORTING);
						int columnToSort = sortEvent.column;
						final SortAction sortAction = getCachedSortAction(columnToSort);
						// TODO only need to sort filtered ?
						sort(allObjects, columnToSort, sortAction);
						final SortAction nextAction = sortAction == SortAction.Ascending ? SortAction.Descending : SortAction.Ascending;
						currentSortAction.put(columnToSort, nextAction);
						table.reset();
						updateView();
						getContainingElement().removeStyleName(Style.SORTING);
					}
				};				
				sortHandler = table.registerCellHandler(sortListener, OnEvent.onClick);
			}
			return sortHandler;
		}
		int columnCount = 0;
		@Override
		public GenericElement newHeaderCell(String columnName) {
			columnCount++;
			GenericElement cell = super.newHeaderCell(columnName);
			SortAction sortAction = getCachedSortAction(columnCount);
			switch (sortAction) {
			case Ascending:
				cell.setStyle(Style.SORT_ASCENDING);
				cell.addHandler(getSortListener(), null, columnCount);
				break;
			case Descending:
				cell.setStyle(Style.SORT_DESCENDING);
				cell.addHandler(getSortListener(), null, columnCount);
				break;
			default:
				break;
			}
			cell.addContents(columnName);
			return cell;
		}
	}

	/**
	 * Implement this if you want to show column headings (which can be sorted)
	 * 
	 * @param headerRow
	 */
	public void buildHeader(SortableHeaderRow headerRow) {

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

	private void doFilter() {
		if (filter == defaultFilter) {
			filteredObjects = allObjects;
		} else {
			filteredObjects.clear();
			for (T t : allObjects) {
				if (this.filter.isIncluded(t)) {
					filteredObjects.add(t);
				}
			}
		}
	}

	public void setContents(ArrayList<T> allObjects) {
		this.allObjects = allObjects;
	}

	public Table<T> table = new Table<T>();

	public void updateView() {
		getContainingElement().clear();
		getContainingElement().getElement().setInnerHTML(getHtml());
	}
	
	public abstract Panel getContainingElement();
	
	public String getHtml() {
		doFilter();
		table.reset();
		table.closeOpeningTag();
		buildHeaderPrivate();
		buildRows();
		buildFooterPrivate();
		String html = table.toString();
		return html;
	}

	private void buildRows() {
		GenericElement tbody = GenericElement.getTableBody(table.builder);
		tbody.closeOpeningTag();
		for (T t : filteredObjects) {
			final Row row = table.newRow();
			final String refId = table.register(t, row);
			populateRowCells(t, row, refId);
		}
		table.cleanupCurrentRow();
		tbody.cleanup();
	}

	/**
	 * @param t
	 *            the domain object underlying the row
	 * @param row
	 *            Add cells to the row
	 * @param refId
	 *            TODO
	 */
	protected abstract void populateRowCells(T t, Row row, String refId);

	private void buildFooterPrivate() {
		final StringBuilder b = new StringBuilder();
		Row headerRow = new Row(b);
		int length = b.length();
		buildFooter(headerRow);
		if (length != b.length()) {
			headerRow.cleanup();
			GenericElement tfoot = GenericElement.getTableFooter(table.builder);
			tfoot.setContentsRaw(b.toString());
			tfoot.cleanup();
		}

	}

	private void buildHeaderPrivate() {
		final StringBuilder b = new StringBuilder();
		SortableHeaderRow headerRow = new SortableHeaderRow(b);
		buildHeader(headerRow);
		if (headerRow.currentCell != null) {
			headerRow.cleanup();
			GenericElement thead = GenericElement.getTableHeader(table.builder);
			thead.setContentsRaw(b.toString());
			thead.cleanup();
		}
	}

}
