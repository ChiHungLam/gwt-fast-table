package com.jwh.gwt.fasttable.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
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
	 * We cache the initial sort action, and toggle the action when sort is
	 * invoked.
	 * 
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
	 * 
	 * @param sortMe
	 *            TODO
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
						try {
							sortEvent.getColumnElement(getDocument())
									.addClassName(Style.CURSOR_WAIT);
						} catch (ElementNotFound e) {
						}
						int columnToSort = sortEvent.column;
						lastSortColumn = columnToSort;
						final SortAction sortAction = getCachedSortAction(columnToSort);
						// TODO only need to sort filtered ?
						sort(allObjects, columnToSort, sortAction);
						final SortAction nextAction = sortAction == SortAction.Ascending ? SortAction.Descending
								: SortAction.Ascending;
						currentSortAction.put(columnToSort, nextAction);
						table.reset();
						getSelectionManager().getSelections().clear();
						updateView();
					}
				};
				sortHandler = table.registerCellHandler(sortListener,
						OnEvent.onClick);
			}
			return sortHandler;
		}

		int columnCount = 0;

		@Override
		public GenericElement newHeaderCell() {
			return newHeaderCell(new String[0]);
		}

		public GenericElement newHeaderCell(String... styles) {
			final ArrayList<String> allStyles = new ArrayList<String>();
			for (String style : styles) {
				allStyles.add(style);
			}
			columnCount++;
			GenericElement cell = super.newHeaderCell();
			SortAction sortAction = getCachedSortAction(columnCount);
			switch (sortAction) {
			case Ascending:
				allStyles
						.add(columnCount == lastSortColumn ? Style.SORT_DESCENDING
								: Style.SORT_ASCENDING_GREY);
				cell.addHandler(getSortListener(), null, columnCount);
				break;
			case Descending:
				allStyles
						.add(columnCount == lastSortColumn ? Style.SORT_ASCENDING
								: Style.SORT_DESCENDING_GREY);
				cell.addHandler(getSortListener(), null, columnCount);
				break;
			default:
				break;
			}
			cell.setStyle(allStyles.toArray(new String[allStyles.size()]));
			return cell;
		}
	}

	int lastSortColumn = 0;

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

	public class SelectionManager {
		final HashSet<T> selections = new HashSet<T>();

		/**
		 * Useful for multiple selection. Select an object, or unselect if
		 * already selected.
		 * 
		 * @param object
		 * @return true if newly selected, false if unselected
		 */
		public boolean toggleSelection(T object) {
			final boolean removed = unselect(object);
			if (!removed) {
				select(object);
			}
			return !removed;
		}

		/**
		 * Use for single selection. Select an object, unselect any current
		 * selection (unless it is the object)
		 * 
		 * @param object
		 *            To be selected
		 * @return any existing selection, or null if none
		 */
		public T singleSelection(T object) {
			T existing = null;
			if (!selections.isEmpty()) {
				existing = new ArrayList<T>(selections).get(0);
			}
			unselect(existing);
			select(object);
			return existing;
		}

		/**
		 * @return all selections
		 */
		public HashSet<T> getSelections() {
			return selections;
		}

		/**
		 * @param object
		 * @return true if the item is newly added
		 */
		public boolean select(T object) {
			return selections.add(object);
		}

		/**
		 * @param object
		 * @return true if the item was removed
		 */
		public boolean unselect(T object) {
			return selections.remove(object);
		}
	}

	final SelectionManager selectionManager = new SelectionManager();

	public SelectionManager getSelectionManager() {
		return selectionManager;
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
		// TODO test code: API to set height
		tbody.setStyle("TABLE_500");
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
			tfoot.setId(getFooterId());
			tfoot.setContentsRaw(b.toString());
			tfoot.cleanup();
		}

	}

	/**
	 * @return The footer ID for external DOM manipulation
	 */
	public String getFooterId() {
		return getTableId() + "_footer";
	}

	private void buildHeaderPrivate() {
		final StringBuilder b = new StringBuilder();
		SortableHeaderRow headerRow = new SortableHeaderRow(b);
		headerRow.setId(getHeaderId());
		buildHeader(headerRow);
		if (headerRow.currentCell != null) {
			headerRow.cleanup();
			GenericElement thead = GenericElement.getTableHeader(table.builder);
			thead.setContentsRaw(b.toString());
			thead.cleanup();
		}
	}

	/**
	 * @return The header id for DOM manipulation
	 */
	public String getHeaderId() {
		return getTableId() + "_Header";
	}

	public String getTableId() {
		return table.getId();
	}

	private Document getDocument() {
		return getContainingElement().getElement().getOwnerDocument();
	}

	enum Position {
		First, Last, Sorted
	}

	public void add(T domainObject, Position position) {
		// TODO
	}

	public void remove(T domainObject) {
		// TODO
	}

	/**
	 * register or unregister the selection, and update CSS selection
	 * 
	 * @param columnElement
	 * @param domainObject
	 * 
	 * @return
	 */
	public boolean toggleSelection(Element columnElement, T domainObject) {
		final boolean newlySelected = getSelectionManager().toggleSelection(
				domainObject);
		if (newlySelected) {
			columnElement.replaceClassName(Style.UNSELECTED, Style.SELECTED);
		} else {
			columnElement.replaceClassName(Style.SELECTED, Style.UNSELECTED);
		}
		return newlySelected;
	}

	/**
	 * Use for single selection. Select an object, unselect any current
	 * selection (unless it is the object)
	 * 
	 * @param columnElement
	 * @param domainObject
	 * @return the previous selection, or null if none
	 */
	public T singleSelection(Element columnElement, T domainObject) {
		final T existing = getSelectionManager().singleSelection(domainObject);
		columnElement.removeClassName(Style.UNSELECTED);
		columnElement.addClassName(Style.SELECTED);
		if (existing != null && existing != domainObject) {
			try {
				final Element existingCell = findRowElement(existing)
						.getFirstChildElement();
				existingCell.removeClassName(Style.SELECTED);
				existingCell.addClassName(Style.UNSELECTED);
			} catch (ElementNotFound e) {
			}
		}
		return existing;
	}

	public Element findRowElement(T existing) throws ElementNotFound {
		final String refId = table.getRefId(existing);
		return getDocument().getElementById(refId);
	}
}