package com.jwh.gwt.design.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.jwh.gwt.design.client.tree.TreeNodeSupplier;
import com.jwh.gwt.design.client.tree.TreeViewer;
import com.jwh.gwt.html.shared.Attribute;
import com.jwh.gwt.html.shared.Tag;
import com.jwh.gwt.html.shared.TagUtil;
import com.jwh.gwt.html.shared.util.IdGenerator;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Design implements EntryPoint, ValueChangeHandler<String>, EventListener, Style, ClickHandler,
		TreeNodeSupplier<Element> {

	public enum Operation {
		tableInsert, tableHeaderRow, tableRow, tableFooterRow, tableCell, moveUp, moveDown, label, value, labelValue, binderId, styleAdd, styleRemove
	}

	class Operations {
		private void binderId() {
			// TODO Auto-generated method stub

		}

		private void doOperation(final Operation op) {
			switch (op) {
			case binderId:
				binderId();
				break;
			case label:
				label();
				break;
			case labelValue:
				labelValue();
				break;
			case moveDown:
				moveDown();
				break;
			case moveUp:
				moveUp();
				break;
			case styleAdd:
				styleAdd();
				break;
			case styleRemove:
				styleRemove();
				break;
			case tableCell:
				tableCell();
				break;
			case tableFooterRow:
				tableFooterRow();
				break;
			case tableHeaderRow:
				tableHeaderRow();
				break;
			case tableInsert:
				tableInsert();
				break;
			case tableRow:
				tableRow();
				break;
			default:
				break;
			}
		}

		private void label() {
			{
				final Element tr = TagUtil.assureParents(currentElement, Tag.tr, Tag.tbody, Tag.table);
				setCurrentElement(tr);
				final Element labelCell = TagUtil.appendChild(tr, Tag.td);
				final Element label = TagUtil.appendChild(labelCell, Tag.label);
				label.setInnerText("Label");
				setPropertyFocus(label);
			}
		}

		private void labelValue() {
			{
				final Element tr = TagUtil.assureParents(currentElement, Tag.tr, Tag.tbody, Tag.table);
				setCurrentElement(tr);
				final Element labelCell = TagUtil.appendChild(tr, Tag.td);
				final Element label = TagUtil.appendChild(labelCell, Tag.label);
				label.setInnerText("Label");
				final Element valueCell = TagUtil.appendChild(tr, Tag.td);
				final String id = IdGenerator.getNextId();
				valueCell.setId(id);
				valueCell.setInnerText("Value");
				label.setAttribute(Attribute.FOR, id);
				setPropertyFocus(label, valueCell);
			}
		}

		private void moveDown() {
			// TODO Auto-generated method stub

		}

		private void moveUp() {
			// TODO Auto-generated method stub

		}

		private void styleAdd() {
			// TODO Auto-generated method stub

		}

		private void styleRemove() {
			// TODO Auto-generated method stub

		}

		private void tableCell() {
			{
				final Element tr = TagUtil.assureParents(currentElement, Tag.tr, Tag.tbody, Tag.table);
				final Element cell = TagUtil.appendChild(tr, Tag.td);
				setCurrentElement(tr);
				setPropertyFocus(cell);
			}
		}

		private void tableFooterRow() {
			// TODO Auto-generated method stub

		}

		private void tableHeaderRow() {
			// TODO Auto-generated method stub

		}

		private void tableInsert() {
			final Element table = DOM.createTable();
			currentElement.insertAfter(table, null);
			setCurrentElement(table);
			setPropertyFocus(table);
		}

		private void tableRow() {
			{
				final Element tbody = TagUtil.assureParents(currentElement, Tag.tbody, Tag.table);
				final Element row = TagUtil.appendChild(tbody, Tag.tr);
				setCurrentElement(row);
				setPropertyFocus(row);
			}
		}
	}

	private RootPanel statesPanel;
	private RootPanel targetPanel;
	private RootPanel toolsPanel;

	TreeViewer<Element> tree = new TreeViewer<Element>(new Tree(), this);
	Element currentElement;
	private SuggestBox commandBox;
	private TextBox textBox;

	private Element[] propertyFocus;

	final Operations operations = new Operations();

	private void addOnClick(final Element element) {
		DOM.setEventListener(element, this);
	}

	private void buildToolsPanel() {
		final VerticalPanel verticalPanel = new VerticalPanel();
		toolsPanel.add(verticalPanel);
		textBox = new TextBox();
		verticalPanel.add(textBox);
		final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		for (final Operation operation : Operation.values()) {
			oracle.add(operation.name());
		}
		commandBox = new SuggestBox(oracle);
		verticalPanel.add(commandBox);
		commandBox.addValueChangeHandler(this);
		for (final Operation operation : Operation.values()) {
			final PushButton button = new PushButton(operation.name());
			verticalPanel.add(button);
			button.addClickHandler(this);
		}
	}

	private void doOperation(final String text) throws IllegalArgumentException {
		operations.doOperation(Operation.valueOf(text));
	}

	private void initializeTargetPanel() {
		currentElement = targetPanel.getElement();
		addOnClick(currentElement);
	}

	@Override
	public void onBrowserEvent(final Event event) {
		switch (event.getTypeInt()) {
		case Event.ONCLICK:
			final EventTarget eventTarget = event.getEventTarget();
			// TODO revisit cast
			final Element newElement = (Element) com.google.gwt.dom.client.Element.as(eventTarget);
			setCurrentElement(newElement);
			commandBox.setFocus(true);
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(final ClickEvent event) {
		if (event.getSource() instanceof PushButton) {
			final PushButton button = (PushButton) event.getSource();
			try {
				doOperation(button.getText());
			} catch (final IllegalArgumentException e) {
			}
		} else if (event.getSource() instanceof Element) {
			final Element element = (Element) event.getSource();
			setCurrentElement(element);
		}

	}

	@Override
	public void onModuleLoad() {
		statesPanel = RootPanel.get("statesPanel");
		targetPanel = RootPanel.get("targetPanel");
		toolsPanel = RootPanel.get("toolsPanel");
		final RootPanel treePanel = RootPanel.get("treePanel");
		buildToolsPanel();
		initializeTargetPanel();
	}

	/*
	 * An operation has been selected
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.logical.shared.ValueChangeHandler#onValueChange(
	 * com.google.gwt.event.logical.shared.ValueChangeEvent)
	 */
	@Override
	public void onValueChange(final ValueChangeEvent<String> event) {
		try {
			final String text = commandBox.getText();
			doOperation(text);
		} catch (final IllegalArgumentException e) {
		}

	}

	@Override
	public void populate(final TreeItem item, final Element obj) {
		item.setText(obj.getTagName());
	}

	private void register(final Element element) {
		addOnClick(element);
		tree.add(element, (Element) element.getParentElement());
	}

	private void setCurrentElement(final Element newElement) {
		if (newElement != currentElement) {
			currentElement.removeClassName(CURRENT_TARGET);
			newElement.addClassName(CURRENT_TARGET);
			currentElement = newElement;
		}
	}

	private void setPropertyFocus(final Element... element) {
		for (final Element e : element) {
			addOnClick(e);
		}
		this.propertyFocus = element;
	}

	@Override
	public void update(final TreeItem item, final Element obj) {
		populate(item, obj);
	}
}
