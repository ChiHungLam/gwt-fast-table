package com.jwh.gwt.design.client;

import java.util.EnumSet;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.Node;
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
import com.jwh.gwt.html.shared.CreationListener;
import com.jwh.gwt.html.shared.Tag;
import com.jwh.gwt.html.shared.TagUtil;
import com.jwh.gwt.html.shared.util.IdGenerator;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Design implements EntryPoint, ValueChangeHandler<String>, EventListener, Style, ClickHandler,
		TreeNodeSupplier<Element>, CreationListener<Element> {

	class Operations {
		private void binderId() {
			// TODO Auto-generated method stub

		}

		private void doOperation(final Operation op) {
			switch (op) {
			case binderId:
				binderId();
				break;
			case delete:
				delete();
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
			case value:
				value();
			default:
				break;
			}
		}

		private void value() {
			final Element tr = tagUtil.assureParents(currentElement, Tag.tr, Tag.tbody, Tag.table);
			setCurrentElement(tr);
			final Element labelCell = tagUtil.appendChild(tr, Tag.td);
			labelCell.setInnerText("Value");
			labelCell.setAttribute(Attribute.SAMPLE, "Value");
			setPropertyFocus(labelCell);
		}

		private void delete() {
			if (currentElement != null) {
				currentElement = (Element) currentElement.getParentElement();
				currentElement.removeFromParent();
			}

		}

		final TagUtil tagUtil = new TagUtil(Design.this);

		private void label() {
			final Element tr = tagUtil.assureParents(currentElement, Tag.tr, Tag.tbody, Tag.table);
			setCurrentElement(tr);
			final Element labelCell = tagUtil.appendChild(tr, Tag.td);
			final Element label = tagUtil.appendChild(labelCell, Tag.label);
			label.setInnerText("Label");
			setPropertyFocus(label);
		}

		private void labelValue() {
			final Element tr = tagUtil.assureParents(currentElement, Tag.tr, Tag.tbody, Tag.table);
			setCurrentElement(tr);
			final Element labelCell = tagUtil.appendChild(tr, Tag.td);
			final Element label = tagUtil.appendChild(labelCell, Tag.label);
			label.setInnerText("Label");
			final Element valueCell = tagUtil.appendChild(tr, Tag.td);
			final String id = IdGenerator.getNextId();
			valueCell.setId(id);
			valueCell.setInnerText("Value");
			label.setAttribute(Attribute.FOR, id);
			setPropertyFocus(label, valueCell);
		}

		private void moveDown() {
			// TODO Synchronize tree
			Element parentElement = (Element) currentElement.getParentElement();
			Node nextSibling = currentElement.getNextSibling();
			if (parentElement != null && nextSibling != null) {
				currentElement.removeFromParent();
				parentElement.insertAfter(currentElement, nextSibling);
			}
		}

		private void moveUp() {
			// TODO Synchronize tree
			Element parentElement = (Element) currentElement.getParentElement();
			Node previousSibling = currentElement.getPreviousSibling();
			if (parentElement != null && previousSibling != null) {
				currentElement.removeFromParent();
				parentElement.insertBefore(currentElement, previousSibling);
			}
		}

		private void styleAdd() {
			// TODO Auto-generated method stub

		}

		private void styleRemove() {
			// TODO Auto-generated method stub

		}

		private void tableCell() {
			final Element tr = tagUtil.assureParents(currentElement, Tag.tr, Tag.tbody, Tag.table);
			final Element cell = tagUtil.appendChild(tr, Tag.td);
			setCurrentElement(tr);
			setPropertyFocus(cell);
		}

		private void tableFooterRow() {
			final Element tfoot = tagUtil.assureParents(currentElement, Tag.tfoot, Tag.table);
			final Element tr = tagUtil.appendChild(tfoot, Tag.tr);
			setCurrentElement(tr);
			setPropertyFocus(tr);
		}

		private void tableHeaderRow() {
			final Element thead = tagUtil.assureParents(currentElement, Tag.thead, Tag.table);
			final Element tr = tagUtil.appendChild(thead, Tag.tr);
			setCurrentElement(tr);
			setPropertyFocus(tr);
		}

		private void tableInsert() {
			final Element table = DOM.createTable();
			currentElement.insertAfter(table, null);
			setCurrentElement(table);
			setPropertyFocus(table);
		}

		private void tableRow() {
			final Element tbody = tagUtil.assureParents(currentElement, Tag.tbody, Tag.table);
			final Element row = tagUtil.appendChild(tbody, Tag.tr);
			setCurrentElement(row);
			setPropertyFocus(row);
		}
	}

	private RootPanel statesPanel;
	private RootPanel targetPanel;
	private RootPanel toolsPanel;

	final Tree tree = new Tree();
	final TreeViewer<Element> treeViewer = new TreeViewer<Element>(tree, this);
	Element currentElement;
	private SuggestBox commandBox;
	private TextBox textBox;

	private Element[] propertyFocus;

	final Operations operations = new Operations();

	private void addOnClick(final Element element) {
		DOM.setEventListener(element, this);
		DOM.sinkEvents(element, Event.ONMOUSEDOWN);
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
		System.err.println(event.getTypeInt());
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
			if (currentElement == element && isInPlaceEditable(element)) {
				new InPlaceEditor(element);
			} else {
				setCurrentElement(element);
			}
		}

	}

	/**
	 * @param element
	 * @return true if this tag has editable text
	 */
	private boolean isInPlaceEditable(Element element) {
		String tagName = element.getTagName();
		EnumSet<Tag> editable = EnumSet.of(Tag.label, Tag.td, Tag.th);
		for (Tag tag : editable) {
			if (tag.name().equalsIgnoreCase(tagName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onModuleLoad() {
		statesPanel = RootPanel.get("statesPanel");
		targetPanel = RootPanel.get("targetPanel");
		toolsPanel = RootPanel.get("toolsPanel");
		final RootPanel treePanel = RootPanel.get("treePanel");
		treePanel.add(tree);
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
		final StringBuilder b = new StringBuilder(obj.getTagName());
		final String innerText = obj.getInnerText();
		if (innerText != null && innerText.length() > 0) {
			b.append(" - ");
			b.append(innerText);
		}
		item.setText(b.toString());
	}

	private void setCurrentElement(final Element newElement) {
		if (newElement != currentElement) {
			currentElement.removeClassName(CURRENT_TARGET);
			newElement.addClassName(CURRENT_TARGET);
			currentElement = newElement;
		}
	}

	private void setPropertyFocus(final Element... element) {
		this.propertyFocus = element;
	}

	@Override
	public void update(final TreeItem item, final Element obj) {
		populate(item, obj);
	}

	@Override
	public void created(final Element obj) {
		addOnClick(obj);
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {			
			@Override
			public void execute() {
				treeViewer.add(obj, (Element) obj.getParentElement());
			}
		});
	}
}
