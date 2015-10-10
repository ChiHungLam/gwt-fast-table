### Overview (BETA) ###

Provides an API to create rich HTML tables while retaining full access to the underlying objects and easily specifying cell event callbacks. The API generates HTML suitable for use in Element.setInnerHtml().

The _**Table Builder**_ API makes it easy to build tables with headers, footers and sorting. See the [sample project](http://fast-table-sample.appspot.com/) for an example.

Decoration of cells is supported via CSS. Cell event handlers are created by implementing a simple interface. A single event handler can be used for multiple events (such as onClick and onMouseOver), for multiple columns, and for all rows. Event callbacks supply the underlying domain object, along with the details required to easily manipulate the row and cell. Callback performance is excellent in Chrome anf Firefox, tolerable in IE7&8.

  * [What makes it so fast?](WhatMakesItSoFast.md)
  * [Roadmap](Roadmap.md)
  * [Sample Project](FastTableSample.md)
  * [Live Demo of Sample Project](http://fast-table-sample.appspot.com/)

