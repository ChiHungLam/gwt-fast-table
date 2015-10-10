# Introduction #

These are the tricks used to render big, rich tables fast.


# Details #

## Use setInnerHtml() ##

The typical technique for building a page in GWT is to add widgets. This offers great flexibility and fine control, but manipulating the DOM is not as efficient as asking the browser to parse a big chunk of HTML and render it.

## Tooling for generating HTML ##

A very efficient, stream-based tool makes it easy to build safe HTML. Java's Assert syntax is used for enforcing the state of an element. This assures that the opening tag, contents, and end tag are specified in the correct order. Assert is also used to validate appropriate tag type nesting (_i.e_; a TR can be followed by a TD). Assert gives protection during debugging, but is omitted from GWT's generated JavaScript.

## Incremental Rendering ##
Instead of building all the rows and then rendering them, we build a page-sized chunk of rows and hand them off for rendering. This is repeated until all rows are inserted. This enhances the perceived speed.

## Avoid Garbage Collection ##
The best way to minimize garbage collection is to avoid creating trash. Over 7000 element objects are consumed to generate the 1000 row sample table. But only a handful are live at any given time -- so we recycle them.