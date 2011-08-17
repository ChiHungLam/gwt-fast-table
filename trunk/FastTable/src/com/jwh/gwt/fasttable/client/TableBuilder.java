package com.jwh.gwt.fasttable.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Panel;
import com.jwh.gwt.fasttable.client.element.Table;
import com.jwh.gwt.fasttable.client.selection.SelectionListener;
import com.jwh.gwt.fasttable.client.selection.SelectionTracker;
import com.jwh.gwt.fasttable.client.util.Style;
import com.jwh.gwt.html.shared.Tag;
import com.jwh.gwt.html.shared.event.CellEvent;
import com.jwh.gwt.html.shared.event.CellEvent.OnEvent;
import com.jwh.gwt.html.shared.event.CellHandlerWrapper;
import com.jwh.gwt.html.shared.event.CellListener;
import com.jwh.gwt.html.shared.exception.AbortOperation;
import com.jwh.gwt.html.shared.exception.NotFound;
import com.jwh.gwt.html.shared.util.HtmlFactory;
import com.jwh.gwt.html.shared.util.HtmlFactory.HtmlElement;
import com.jwh.gwt.html.shared.util.IdGenerator;

public abstract class TableBuilder<T> {

	enum Position {
		First, Last, Sorted
	}

	/**
	 * Implemented as an inner class so it can have access to sorting services
	 */
	public class SortableHeaderRow {
		CellHandlerWrapper<T> sortHandler = null;

		int columnCount = 0;

		private final HtmlElement tr;

		public SortableHeaderRow() {
			tr = HtmlFactory.forRoot(Tag.tr);
		}

		public CellHandlerWrapper<T> getSortListener() {
			if (sortHandler == null) {
				final CellListener<T> sortListener = new CellListener<T>() {
					@Override
					public void handlerCellEvent(CellEvent<T> sortEvent) {
						try {
							sortEvent.getColumnElement(getDocument()).addClassName(Style.CURSOR_WAIT);
						} catch (final NotFound e) {
						}
						final int columnToSort = sortEvent.column;
						lastSortColumn = columnToSort;
						final SortAction sortAction = getCachedSortAction(columnToSort);
						// TODO only need to sort filtered ?
						sort(allObjects, columnToSort, sortAction);
						final SortAction nextAction = sortAction == SortAction.Ascending ? SortAction.Descending
								: SortAction.Ascending;
						currentSortAction.put(columnToSort, nextAction);
						table.reset();
						getSelectionTracker().getSelections().clear();
						updateView();
					}
				};
				sortHandler = table.registerCellHandler(sortListener, OnEvent.onClick);
			}
			return sortHandler;
		}

		public HtmlElement newHeaderCell() {
			return newHeaderCell(new String[0]);
		}

		public HtmlElement newHeaderCell(String... styles) {
			final ArrayList<String> allStyles = new ArrayList<String>();
			for (final String style : styles) {
				allStyles.add(style);
			}
			columnCount++;
			final HtmlElement cell = tr.addChild(Tag.th);
			final SortAction sortAction = getCachedSortAction(columnCount);
			switch (sortAction) {
			case Ascending:
				allStyles.add(columnCount == lastSortColumn ? Style.SORT_DESCENDING
						: Style.SORT_ASCENDING_GREY);
				cell.addHandler(getSortListener(), null, columnCount);
				break;
			case Descending:
				allStyles.add(columnCount == lastSortColumn ? Style.SORT_ASCENDING
						: Style.SORT_DESCENDING_GREY);
				cell.addHandler(getSortListener(), null, columnCount);
				break;
			default:
				break;
			}
			cell.setStyle(allStyles.toArray(new String[allStyles.size()]));
			return cell;
		}

		public String toHtml() {
			return tr.toHtml();
		}
	}

	public enum SortAction {
		None, Ascending, Descending
	}

	private IncrementalBuilder<T> incrementalBuilder;

	protected Filter<T> defaultFilter = getDefaultFilter();
	protected Filter<T> filter = defaultFilter;
	protected HashMap<Integer, SortAction> currentSortAction = new HashMap<Integer, TableBuilder.SortAction>();

	protected ArrayList<T> allObjects = new ArrayList<T>();

	protected ArrayList<T> filteredObjects = new ArrayList<T>();

	protected int lastSortColumn = 0;

	protected final SelectionTracker<T> selectionTracker = new SelectionTracker<T>();

	public Table<T> table = new Table<T>();

	public Configuration configuration = new Configuration();
	
	public void add(T domainObject, Position position) {
		// TODO
	}

	/**
	 * Implement this if you want to show a table footer
	 * 
	 * @param headerRow
	 */
	public void buildFooter(HtmlElement row) {

	}

	private void buildFooterPrivate() {
		final HtmlElement row = HtmlFactory.forRoot(Tag.tr);
		final int length = row.getHtmlLength();
		buildFooter(row);
		if (length != row.getHtmlLength()) {
			final HtmlElement tfoot = table.getRoot().addChild(Tag.tfoot);
			tfoot.setId(getFooterId());
			tfoot.setContentsRaw(row.toHtml());
			tfoot.cleanup();
		}

	}

	/**
	 * Implement this if you want to show column headings (which can be sorted)
	 * 
	 * @param headerRow
	 */
	public void buildHeader(SortableHeaderRow headerRow) {

	}

	private void buildHeaderPrivate() {
		final SortableHeaderRow headerRow = new SortableHeaderRow();
		headerRow.tr.setId(getHeaderId());
		buildHeader(headerRow);
		if (headerRow.columnCount > 0) {
			final HtmlElement thead = table.getRoot().addChild(Tag.thead);
			thead.setContentsRaw(headerRow.toHtml());
			thead.cleanup();
		}
	}

	private void buildRows() {
		final HtmlElement tbody = table.getRoot().addChild(Tag.tbody);
		final String tbodyId = IdGenerator.getNextId();
		tbody.setId(tbodyId);
		int count = 0;
		for (final T t : filteredObjects) {
			final HtmlElement row = tbody.addChild(Tag.tr);
			final String refId = table.register(t, row);
			populateRowCells(t, row, refId, count);
			count++;
			if (count >= configuration.getInitialIncrement()) {
				scheduleIncrementalTimer(count, new ArrayList<T>(filteredObjects), tbodyId);
				break;
			}
		}
		tbody.cleanup();
	}

	protected void cancelIncrementalBuilder() {
		if (incrementalBuilder != null) {
			incrementalBuilder.cancel();
		}
	}

	private void doFilter() {
		if (filter == defaultFilter) {
			filteredObjects = allObjects;
		} else {
			filteredObjects.clear();
			for (final T t : allObjects) {
				if (this.filter.isIncluded(t)) {
					filteredObjects.add(t);
				}
			}
		}
	}

	public Element findRowElement(T existing) throws NotFound {
		final String refId = table.getRefId(existing);
		return getDocument().getElementById(refId);
	}

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

	public abstract Panel getContainingElement();

	public Filter<T> getDefaultFilter() {
		return new Filter<T>() {
			@Override
			public boolean isIncluded(T candidate) {
				return true;
			}
		};
	}

	public Document getDocument() {
		return getContainingElement().getElement().getOwnerDocument();
	}

	public ArrayList<T> getFilteredObjects() {
		return filteredObjects;
	}

	/**
	 * @return The footer ID for external DOM manipulation
	 */
	public String getFooterId() {
		return getTableId() + "_footer";
	}

	/**
	 * @return The header id for DOM manipulation
	 */
	public String getHeaderId() {
		return getTableId() + "_Header";
	}

	public String getHtml() {
		doFilter();
		table.reset();
		buildHeaderPrivate();
		buildRows();
		buildFooterPrivate();
		final String html = table.toString();
		return html;
	}

	public SelectionTracker<T> getSelectionTracker() {
		return selectionTracker;
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

	public String getTableId() {
		return table.getId();
	}

	public void logError(String message) {
	}

	public void logInfo(String message) {
	}

	/**
	 * register or unregister the selection, and update CSS selection
	 * 
	 * @param columnElement
	 * @param domainObject
	 * @param listener
	 *            TODO
	 * @return
	 * @throws AbortOperation
	 */
	public void multiSelect(final Element columnElement, final T domainObject, SelectionListener<T> listener)
			throws AbortOperation {
		final SelectionListener<T> selectionListener = new SelectionListener<T>() {

			@Override
			public void select(T object) {
				columnElement.replaceClassName(Style.UNSELECTED, Style.SELECTED);
			};

			@Override
			public void unselect(T object) {
				columnElement.replaceClassName(Style.SELECTED, Style.UNSELECTED);
			};
		};
		getSelectionTracker().multiSelect(domainObject, selectionListener.append(listener));
	}

	/**
	 * @param t
	 *            the domain object underlying the row
	 * @param row
	 *            Add cells to the row
	 * @param refId
	 *            TODO
	 * @param rowNumber
	 *            TODO
	 */
	protected abstract void populateRowCells(T t, HtmlElement row, String refId, int rowNumber);

	public void remove(T domainObject) {
		// TODO
	}

	private void scheduleIncrementalTimer(int startIndex, ArrayList<T> items, String refId) {
		new IncrementalBuilder<T>(this, items, refId, startIndex).build();
	}

	public void setContents(ArrayList<T> allObjects) {
		this.allObjects = allObjects;
	}

	public void setFilter(Filter<T> filter) {
		if (allObjects == filteredObjects) {
			filteredObjects = new ArrayList<T>(allObjects);
		}
		this.filter = filter;
	}

	public void setIncrementalBuilder(IncrementalBuilder<T> incrementalBuilder) {
		this.incrementalBuilder = incrementalBuilder;
	}

	public static native String getUserAgent() /*-{
		return navigator.userAgent.toLowerCase();
	}-*/;

	/**
	 * Use for single selection. Select an object, unselect any current
	 * selection (unless it is the object)
	 * 
	 * @param columnElement
	 * @param domainObject
	 * @param listener
	 *            TODO
	 * @return the previous selection, or null if none
	 * @throws AbortOperation
	 */
	public void singleSelect(final Element columnElement, final T domainObject, SelectionListener<T> listener)
			throws AbortOperation {
		final SelectionListener<T> selectionListener = new SelectionListener<T>() {
			@Override
			public void select(T object) {
				columnElement.removeClassName(Style.UNSELECTED);
				columnElement.addClassName(Style.SELECTED);
			}

			@Override
			public void unselect(T object) {
				try {
					final Element existingCell = findRowElement(object).getFirstChildElement();
					existingCell.removeClassName(Style.SELECTED);
					existingCell.addClassName(Style.UNSELECTED);
				} catch (final NotFound e) {
				}
			};
		};
		getSelectionTracker().singleSelect(domainObject, selectionListener.append(listener));
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

	public void updateView() {
		getContainingElement().clear();
		getContainingElement().getElement().setInnerHTML(getHtml());
	}
}
