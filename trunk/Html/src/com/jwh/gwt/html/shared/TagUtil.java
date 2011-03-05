package com.jwh.gwt.html.shared;

import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.EventListener;
import com.jwh.gwt.html.shared.exception.NotFound;

public class TagUtil {

	/**
	 * Append an element with the specified tag
	 * 
	 * @param parent
	 *            The parent of the newly created element
	 * @param tag
	 *            Create an element with this tag
	 * @return return a new element with the specified tag
	 */
	public static Element appendChild(final Element parent, final Tag tag) {
		final Element addMe = DOM.createElement(tag.name());
		parent.appendChild(addMe);
		return addMe;
	}

	/**
	 * @param element
	 * @param tag
	 * @param depth
	 * @return
	 * @throws NotFound
	 *             if no element with the specified tag is found within the
	 *             specified depth
	 */
	public static Element assureParent(final Element element, final Tag tag, int depth) throws NotFound {
		if (element == null || depth < 0) {
			throw NotFound.getInstance();
		}
		depth--;
		if (!element.getTagName().equalsIgnoreCase(tag.name())) {
			return assureParent((Element) element.getParentElement(), tag, depth);
		}
		return element;
	}

	/**
	 * Assure that the specified sequence of tags is available. If they do not
	 * exist, create them.
	 * 
	 * @param parent
	 * @param parents
	 * @return an element with the specified heritage
	 */
	public static Element assureParents(final Element parent, final Tag... parents) {
		Element closestParent = parent;
		for (int i = parents.length - 1; i >= 0; i--) {
			final Tag tag = parents[i];
			try {
				closestParent = assureParent(closestParent, parents[0], i);
			} catch (final NotFound e) {
				closestParent = appendChild(closestParent, tag);
			}
		}
		return closestParent;
	}

	public static void registerListener(final Element element, final EventListener listener,
			final boolean includeChildren) {
		// TODO revisit redundant listener
		DOM.setEventListener(element, listener);
		if (includeChildren) {
			final NodeList<Node> childNodes = element.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				final Node child = childNodes.getItem(i);
				// TODO revisit cast
				registerListener((Element) child, listener, includeChildren);
			}
		}
	}
}
