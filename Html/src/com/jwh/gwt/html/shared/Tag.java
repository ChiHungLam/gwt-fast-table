package com.jwh.gwt.html.shared;

import com.jwh.gwt.html.shared.annotation.FormTag;
import com.jwh.gwt.html.shared.annotation.Html5Tag;
import com.jwh.gwt.html.shared.annotation.TableTag;
import com.jwh.gwt.html.shared.annotation.UseCssTag;

/**
 * @author jheyne
 * 
 *         {@link http://www.quackit.com/html_5/tags/}
 */
public enum Tag {
	/**
	 * Specifies a hyperlink
	 */
	a,
	/**
	 * Specifies an abbreviation
	 */
	abbr,
	/**
	 * Specifies an address element
	 */
	address,
	/**
	 * Specifies an area inside an image map
	 */
	area,
	/**
	 * Specifies an article
	 */
	@Html5Tag
	article,
	/**
	 * Specifies content aside from the page content
	 */
	@Html5Tag
	aside,
	/**
	 * Specifies sound content
	 */
	@Html5Tag
	audio,
	/**
	 * Specifies bold text
	 */
	@UseCssTag
	b,
	/**
	 * Specifies a base URL for all the links in a page
	 */
	base,
	/**
	 * Specifies the direction of text display
	 */
	bdo,
	/**
	 * Specifies a long quotation
	 */
	blockquote,
	/**
	 * Specifies the body element
	 */
	body,
	/**
	 * Inserts a single line break
	 */
	br,
	/**
	 * Specifies a push button
	 */
	@FormTag
	button,
	/**
	 * Define graphics
	 */
	@Html5Tag
	canvas,
	/**
	 * Specifies a table caption
	 */
	caption,
	/**
	 * Specifies a citation
	 */
	cite,
	/**
	 * Specifies computer code text
	 */
	code,
	/**
	 * Specifies attributes for table columns
	 */
	col,
	/**
	 * Specifies groups of table columns
	 */
	colgroup,
	/**
	 * Specifies a command
	 */
	@Html5Tag
	command,
	/**
	 * Specifies an "autocomplete" dropdown list
	 */
	@Html5Tag
	datalist,
	/**
	 * Specifies a definition description
	 */
	dd,
	/**
	 * Specifies deleted text
	 */
	del,
	/**
	 * Specifies details of an element
	 */
	@Html5Tag
	details,
	/**
	 * Defines a definition term
	 */
	dfn,
	/**
	 * Specifies a section in a document
	 */
	div,
	/**
	 * Specifies a definition list
	 */
	dl,
	/**
	 * Specifies a definition term
	 */
	dt,
	/**
	 * Specifies emphasized text
	 */
	em,
	/**
	 * Specifies external application or interactive content
	 */
	@Html5Tag
	embed,
	/**
	 * Specifies a target for events sent by a server
	 */
	@Html5Tag
	eventsource,
	/**
	 * Specifies a fieldset
	 */
	fieldset,
	/**
	 * Specifies caption for the figure element.
	 */
	@Html5Tag
	figcaption,
	/**
	 * Specifies a group of media content, and their caption
	 */
	@Html5Tag
	figure,
	/**
	 * Specifies a footer for a section or page
	 */
	@Html5Tag
	footer,
	/**
	 * Specifies a form
	 */
	@FormTag
	form,
	/**
	 * Specifies a heading level 1
	 */
	h1,
	/**
	 * Specifies a heading level 2
	 */
	h2,
	/**
	 * Specifies a heading level 3
	 */
	h3,
	/**
	 * Specifies a heading level 4
	 */
	h4,
	/**
	 * Specifies a heading level 5
	 */
	h5,
	/**
	 * Specifies a heading level 6
	 */
	h6,
	/**
	 * Specifies information about the document
	 */
	head,
	/**
	 * Specifies a group of introductory or navigational aids,
	 * including hgroup elements
	 */
	@Html5Tag
	header,
	/**
	 * Specifies a header for a section or page
	 */
	@Html5Tag
	hgroup,
	/**
	 * Specifies a horizontal rule
	 */
	hr,
	/**
	 * Specifies an html document
	 */
	html,
	/**
	 * Specifies italic text
	 */
	@UseCssTag
	i,
	/**
	 * Specifies an inline sub window (frame)
	 */
	iframe,
	/**
	 * Specifies an image
	 */
	img,
	/**
	 * Specifies an input field
	 */
	@FormTag
	input,
	/**
	 * Specifies inserted text
	 */
	ins,
	/**
	 * Specifies keyboard text
	 */
	kbd,
	/**
	 * Generates a key pair
	 */
	@Html5Tag
	keygen,
	/**
	 * Specifies a label for a form control
	 */
	label,
	/**
	 * Specifies a title in a fieldset
	 */
	legend,
	/**
	 * Specifies a list item
	 */
	li,
	/**
	 * Specifies a resource reference
	 */
	link,
	/**
	 * Specifies marked text
	 */
	@Html5Tag
	mark,
	/**
	 * Specifies an image map
	 */
	map,
	/**
	 * Specifies a menu list
	 */
	menu,
	/**
	 * Specifies meta information
	 */
	meta,
	/**
	 * Specifies measurement within a predefined range
	 */
	@Html5Tag
	meter,
	/**
	 * Specifies navigation links
	 */
	@Html5Tag
	nav,
	/**
	 * Specifies a noscript section
	 */
	noscript,
	/**
	 * Specifies an embedded object
	 */
	object,
	/**
	 * Specifies an ordered list
	 */
	ol,
	/**
	 * Specifies an option group
	 */
	optgroup,
	/**
	 * Specifies an option in a drop-down list
	 */
	option,
	/**
	 * Specifies some types of output
	 */
	@Html5Tag
	output,
	/**
	 * Specifies a paragraph
	 */
	p,
	/**
	 * Specifies a parameter for an object
	 */
	param,
	/**
	 * Specifies preformatted text
	 */
	pre,
	/**
	 * Specifies progress of a task of any kind
	 */
	@Html5Tag
	progress,
	/**
	 * Specifies a short quotation
	 */
	q,
	/**
	 * Specifies a ruby annotation (used in East Asian typography)
	 */
	@Html5Tag
	ruby,
	/**
	 * Used for the benefit of browsers that don't support ruby annotations
	 */
	@Html5Tag
	rp,
	/**
	 * Specifies the ruby text component of a ruby annotation.
	 */
	@Html5Tag
	rt,
	/**
	 * Specifies sample computer code
	 */
	samp,
	/**
	 * Specifies a script
	 */
	script,
	/**
	 * Specifies a section
	 */
	@Html5Tag
	section,
	/**
	 * Specifies a selectable list
	 */
	@FormTag
	select,
	/**
	 * Specifies small text
	 */
	small,
	/**
	 * Specifies media resources
	 */
	@Html5Tag
	source,
	/**
	 * Specifies a section in a document
	 */
	span,
	/**
	 * Specifies strong text
	 */
	strong,
	/**
	 * Specifies a style definition
	 */
	style,
	/**
	 * Specifies subscripted text
	 */
	sub,
	/**
	 * Specifies a summary/caption for the details, // element
	 */
	@Html5Tag
	summary,
	/**
	 * Specifies superscripted text
	 */
	sup,
	/**
	 * Specifies a table
	 */
	@TableTag
	table,
	/**
	 * Specifies a table body
	 */
	@TableTag
	tbody,
	/**
	 * Specifies a table cell
	 */
	@TableTag
	td,
	/**
	 * Specifies a text area
	 */
	@FormTag
	textarea,
	/**
	 * Specifies a table footer
	 */
	@TableTag
	tfoot,
	/**
	 * Specifies a table header cell
	 */
	@TableTag
	th,
	/**
	 * Specifies a table header
	 */
	@TableTag
	thead,
	/**
	 * Specifies a date/time
	 */
	@Html5Tag
	time,
	/**
	 * Specifies the document title
	 */
	title,
	/**
	 * Specifies a table row
	 */
	@TableTag
	tr,
	/**
	 * Specifies an unordered list
	 */
	ul,
	/**
	 * Specifies a variable
	 */
	var,
	/**
	 * Specifies a video
	 */
	@Html5Tag
	video,
	/**
	 * Specifies a line break opportunity for very long words and strings of
	 * text with no spaces.
	 */
	@Html5Tag
	wbr

}
