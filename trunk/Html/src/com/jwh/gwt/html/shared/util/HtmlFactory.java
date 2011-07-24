package com.jwh.gwt.html.shared.util;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.jwh.gwt.html.shared.Attribute;
import com.jwh.gwt.html.shared.Tag;
import com.jwh.gwt.html.shared.event.CellEvent.OnEvent;
import com.jwh.gwt.html.shared.event.CellHandlerWrapper;
import com.jwh.gwt.html.shared.exception.NotFound;

public class HtmlFactory {

	public class HtmlElement {
		State currentState = State.StartTag;

		Tag tag;

		HtmlElement currentChild;

		public HtmlElement() {
			super();
			// TODO Auto-generated constructor stub
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
		public HtmlElement addAttribute(final String key, final String value) {
			assert isAppropriate(State.StartTag) : "attributes should be added before " + currentState;
			builder.append(key);
			builder.append("=\"");
			builder.append(value);
			builder.append("\" ");
			return this;
		}

		public HtmlElement addChild(final Tag tag) {
			assert Validator.validateChild(this.tag, tag) : this.tag + " cannot have child: " + tag;
			closeOpeningTag();
			if (currentChild != null) {
				currentChild.cleanup();
			}
			currentChild = beginTag(tag);
			assert currentChild != this : "child and parent should be different";
			return currentChild;
		}

		/**
		 * Add content to the tag. Uses @see SafeHtmlUtils to mitigate security
		 * risks.
		 * 
		 * @param contents
		 *            The text to add.
		 * @return The receiver
		 */
		public HtmlElement addContents(final String contents) {
			assert isAppropriate(State.Contents) : "contents should be set before " + currentState;
			closeOpeningTag();
			currentState = State.Contents;
			if (contents != null) {
				builder.append(SafeHtmlUtils.fromString(contents).asString());
			}
			return this;
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
		public HtmlElement addHandler(final CellHandlerWrapper<?> wrapper, final String objectId, final int columnIndex) {
			assert isAppropriate(State.StartTag) : "attributes should be added before " + currentState;
			for (final OnEvent onEvent : wrapper.onEvents) {
				builder.append(onEvent.name());
				builder.append("=\"window.");
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

		private HtmlElement beginTag(final Tag tag) {
			final HtmlElement element = getElement();
			element.tag = tag;
			element.startOpenTag();
			return element;
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

		private void cleanupPendingChildren() {
			if (currentChild != null) {
				currentChild.cleanup();
				currentChild = null;
			}
		}

		private HtmlElement closeOpeningTag() {
			if (currentState == State.StartTag) {
				builder.append('>');
				currentState = State.Contents;
			}
			return this;
		}

		private HtmlElement getElement() {
			HtmlElement recycled;
			try {
				recycled = recycleBin.get();
			} catch (final NotFound e) {
				assert false : "Should not need to create element. Need to make recycle bin bigger?";
				recycled = new HtmlElement();
			}
			recycled.currentState = State.StartTag;
			recycled.currentChild = null;
			return recycled;
		}

		public int getHtmlLength() {
			return builder.length();
		}

		public boolean hasChild() {
			return currentChild != null;
		}

		/**
		 * assure that components are not written in the wrong order
		 * 
		 * @param state
		 * @return true if the operation is appropriate now
		 */
		private boolean isAppropriate(final State state) {
			return currentState.compareTo(state) <= 0;
		}

		public void reset(final Tag tag) {
			assert this.tag == tag : "Expected root tag not to change";
			this.tag = tag;
			currentChild = null;
			builder.setLength(0);
			currentState = State.StartTag;
			startOpenTag();
		}

		public HtmlElement setContentsRaw(final String contents) {
			closeOpeningTag();
			if (contents != null) {
				builder.append(contents);
			}
			return this;
		}

		/**
		 * Set the id attribute
		 * 
		 * @param id
		 * @return
		 */
		public HtmlElement setId(final String id) {
			addAttribute(Attribute.ID, id);
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
		public HtmlElement setStyle(final String... styles) {
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

		private void startOpenTag() {
			builder.append('<').append(tag).append(' ');
		}

		public String toHtml() {
			cleanup();
			return builder.toString();
		}
	}

	enum State {
		StartTag, Contents, EndTag
	}

	private static final int DEFAULT_SIZE = 1000;

	public static HtmlElement forRoot(final Tag tag) {
		final HtmlFactory stream = new HtmlFactory(tag);
		return stream.root;
	}

	public static String trimTag(final String html, final Tag tag) {
		final int start = html.indexOf('>') + 1;
		final int stop = html.length() - (tag.name().length() + 3);
		final String substring = html.substring(start, stop);
		return substring;
	}

	final private HtmlElement root;

	final StringBuilder builder;

	final RecycleBin<HtmlElement> recycleBin = new RecycleBin<HtmlElement>(new HtmlElement[] { new HtmlElement(),
			new HtmlElement(), new HtmlElement(), new HtmlElement(), new HtmlElement(), new HtmlElement(),
			new HtmlElement(), new HtmlElement(), new HtmlElement() });

	private HtmlFactory(final Tag tag) {
		this(tag, DEFAULT_SIZE);
	}

	private HtmlFactory(final Tag tag, final int size) {
		super();
		builder = new StringBuilder(size);
		root = createRoot(tag);
	}

	private HtmlElement createRoot(final Tag tag) {
		final HtmlElement element = new HtmlElement();
		element.tag = tag;
		element.startOpenTag();
		return element;
	}

}
