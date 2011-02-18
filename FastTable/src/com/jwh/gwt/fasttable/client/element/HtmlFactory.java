package com.jwh.gwt.fasttable.client.element;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.jwh.gwt.fasttable.client.CellEvent.OnEvent;
import com.jwh.gwt.fasttable.client.CellHandlerWrapper;
import com.jwh.gwt.fasttable.client.exception.NotFound;
import com.jwh.gwt.fasttable.client.util.RecycleBin;

public class HtmlFactory {

	private static final int DEFAULT_SIZE = 1000;
	final private HtmlElement root;
		private HtmlFactory(Tag tag) {
		this(tag, DEFAULT_SIZE);
	}

	private HtmlFactory(Tag tag, int size) {
		super();
		builder = new StringBuilder(size);
		root = createRoot(tag);
	}

	public static HtmlElement forRoot(Tag tag) {
		final HtmlFactory stream = new HtmlFactory(tag);
		return stream.root;
	}

	private HtmlElement createRoot(Tag tag) {
		HtmlElement element = new HtmlElement();
		element.tag = tag;
		element.startOpenTag();
		return element;
	}

	final StringBuilder builder;

	enum State {
		StartTag, Contents, EndTag
	}

	public enum Tag {
		table, thead, tbody, tfoot, tr, th, td, label, input
	}

	final RecycleBin<HtmlElement> recycleBin = new RecycleBin<HtmlElement>(new HtmlElement[] {
			new HtmlElement(), new HtmlElement(), new HtmlElement(), new HtmlElement(), new HtmlElement(),
			new HtmlElement(), new HtmlElement(), new HtmlElement(), new HtmlElement() });

	public class HtmlElement {
		State currentState = State.StartTag;

		Tag tag;

		/**
		 * Add content to the tag. Uses @see SafeHtmlUtils to mitigate security
		 * risks.
		 * 
		 * @param contents
		 *            The text to add.
		 * @return The receiver
		 */
		public HtmlElement addContents(String contents) {
			assert isAppropriate(State.Contents) : "contents should be set before " + currentState;
			closeOpeningTag();
			currentState = State.Contents;
			if (contents != null) {
				builder.append(SafeHtmlUtils.fromString(contents).asString());
			}
			return this;
		}

		/**
		 * Add an attribute to the opening tag
		 * 
		 * @param key
		 *            Attribute name
		 * @param value
		 *            Attribute value
		 * @return The receiver
		 */
		public HtmlElement addAttribute(String key, String value) {
			assert isAppropriate(State.StartTag) : "attributes should be added before " + currentState;
			builder.append(key);
			builder.append("=\"");
			builder.append(value);
			builder.append("\" ");
			return this;
		}

		HtmlElement currentChild;

		public HtmlElement() {
			super();
			// TODO Auto-generated constructor stub
		}

		public HtmlElement addChild(Tag tag) {
			assert Validator.validateChild(this.tag, tag) : this.tag + " cannot have child: " + tag;
			closeOpeningTag();
			if (currentChild != null) {
				currentChild.cleanup();
			}
			currentChild = beginTag(tag);
			assert currentChild != this : "child and parent should be different";
			return currentChild;
		}

		private HtmlElement beginTag(Tag tag) {
			final HtmlElement element = getElement();
			element.tag = tag;
			element.startOpenTag();
			return element;
		}

		private void startOpenTag() {
			builder.append('<').append(tag).append(' ');
		}

		public String toHtml() {
			cleanup();
			return builder.toString();
		}

		public HtmlElement setContentsRaw(String contents) {
			closeOpeningTag();
			if (contents != null) {
				builder.append(contents);
			}
			return this;
		}

		/**
		 * Define a single style. Mutually exclusive with @see
		 * Element.setStyles.
		 * 
		 * @param style
		 *            The class attribute
		 * @return The receiver
		 */
		public HtmlElement setStyle(String... styles) {
			assert isAppropriate(State.StartTag) : "style should be set before " + currentState;
			if (styles.length > 0) {
				builder.append("class=\"");
				for (final String style : styles) {
					builder.append(style);
					builder.append(' ');
				}
				builder.deleteCharAt(builder.length() - 1);
				builder.append("\" ");
			}
			return this;
		}

		/**
		 * Set the id attribute
		 * 
		 * @param id
		 * @return
		 */
		public HtmlElement setId(String id) {
			addAttribute("id", id);
			return this;
		}

		/**
		 * assure that components are not written in the wrong order
		 * 
		 * @param state
		 * @return true if the operation is appropriate now
		 */
		private boolean isAppropriate(State state) {
			return currentState.compareTo(state) <= 0;
		}

		private void cleanupPendingChildren() {
			if (currentChild != null) {
				currentChild.cleanup();
				currentChild = null;
			}
		}

		/**
		 * @param wrapper
		 *            Encapsulates what we need to know to find the handler when
		 *            an event occurs
		 * @param objectId
		 *            Identifier for the domain model instance
		 * @param columnIndex
		 *            One-based. First column is 1.
		 * @return
		 */
		public HtmlElement addHandler(CellHandlerWrapper<?> wrapper, String objectId, int columnIndex) {
			assert isAppropriate(State.StartTag) : "attributes should be added before " + currentState;
			for (final OnEvent onEvent : wrapper.onEvents) {
				builder.append(onEvent.name());
				builder.append("=\"window.fctn");
				builder.append(wrapper.functionId);
				builder.append("('");
				builder.append(wrapper.id);
				builder.append("','");
				builder.append(objectId);
				builder.append("','");
				builder.append(onEvent.name());
				builder.append("',");
				builder.append(columnIndex);
				builder.append(")\" ");
			}
			return this;
		}

		private HtmlElement closeOpeningTag() {
			if (currentState == State.StartTag) {
				builder.append('>');
				currentState = State.Contents;
			}
			return this;
		}

		/**
		 * Assure that the entire element is manifested as a string
		 */
		public void cleanup() {
			if (currentState != State.EndTag) {
				closeOpeningTag();
			}
			if (currentState != State.EndTag) {
				cleanupPendingChildren();
				builder.append("</");
				builder.append(tag);
				builder.append('>');
				if (this != root) {
					recycleBin.put(this);
				}
			}
			currentState = State.EndTag;
		}

		private HtmlElement getElement() {			
				HtmlElement recycled;
				try {
					recycled = recycleBin.get();
				} catch (NotFound e) {
					assert false : "Should not need to create element. Need to make recycle bin bigger?";
					recycled = new HtmlElement();
				}
				recycled.currentState = State.StartTag;
				recycled.currentChild = null;
				return recycled;
		}

		public void reset(Tag tag) {
			assert this.tag == tag : "Expected root tag not to change";
			this.tag = tag;
			currentChild = null;
			builder.setLength(0);
			currentState = State.StartTag;
			startOpenTag();
		}

		public int getHtmlLength() {
			return builder.length();
		}

		public boolean hasChild() {
			return currentChild != null;
		}
	}

	public static String trimTag(String html, Tag tag) {
		final int start = html.indexOf('>') + 1;
		int stop = html.length() - (tag.name().length() + 3);
		final String substring = html.substring(start, stop);
		return substring;
	}

}