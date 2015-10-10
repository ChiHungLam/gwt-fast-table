# Introduction #

Sample project to demonstrate usage of gwt-fast-table. Subject to change, as described in [RoadMap](RoadMap.md).


# Details #

A sample project demonstrating usage is included. It is available live [here](http://fast-table-sample.appspot.com/). Mousing over a name or zip highlights the cell, demonstrating that we have access to the underlying DOM elements. Clicking on a name or zip opens a popup showing a description of the underlying object.

The sample project

  * generates 1000 sample domain objects
  * builds a row for each domain object
  * builds a cell (column) for each field of the domain object
  * specifies a CSS style for each cell
  * adds handlers for the first and last columns

This snippet gives you an idea of the simple sample objects we'll show in the table:

```
	public SampleModel() {
		super();
		this.name = randomSample(names);
		this.street = randomSample(streets);
		this.city = randomSample(cities);
		this.state = randomSample(states);
		this.zip = randomSample(zips);
	}
```

Here's a snippet showing how the table, rows and columns are built:

```
		final Table<SampleModel> table = new Table<SampleModel>();
		CellListener<SampleModel> cellListener = buildCellListener(body);
		final CellHandlerWrapper<SampleModel> cellHandler = table
				.registerCellHandler(ON_CLICK, cellListener);
		// note that the same call handler can handle multiple events
		cellHandler.addEvent(ON_MOUSE_OVER);
		cellHandler.addEvent(ON_MOUSE_OUT);
		// get a bunch of model objects
		final ArrayList<SampleModel> samples = SampleModel.getSamples(1000);
		// add rows and cells for the model objects
		for (final SampleModel sample : samples) {
			final Row row = table.newRow();
			// associate the model object with the row
			final String objectId = table.register(sample, row);
			// add style info, an event handler, and set the contents
			// here we only add the event handler to NAME and ZIP
			row.newCell().setStyle(NAME)
					.addHandler(cellHandler, objectId, 0)
					.addContents(sample.name);
			row.newCell().setStyle(STREET).addContents(sample.street);
			row.newCell().setStyle(CITY).addContents(sample.city);
			row.newCell().setStyle(STATE).addContents(sample.state);
			row.newCell().setStyle(ZIP).addHandler(cellHandler, objectId, 4)
					.addContents(sample.zip);
		}
		final String html = table.toString();
```

Here's a snippet of the cell listener.

```
	private CellListener<SampleModel> buildCellListener(final Element body) {
		return new CellListener<SampleModel>() {

			@Override
			public void handlerCellEvent(CellEvent<SampleModel> event) {
				switch (event.getOnEvent()) {
				case onClick:
					Window.alert("Event: " + event.getOnEvent() + "\nObject: "
							+ event.domainObject.toString() + "\nColumn: " + event.column);
					break;
				case onMouseOver:
					try {
						event.getColumnElement(body.getOwnerDocument()).addClassName(HIGHLIGHT);
					} catch (ElementNotFound e1) {
					}
					break;
				case onMouseOut:
					try {
						event.getColumnElement(body.getOwnerDocument()).removeClassName(HIGHLIGHT);
					} catch (ElementNotFound e1) {
					}
					break;
				default:
					break;
				}
				
			}
		};
	}
```

Here's the interface used to manage CSS entries:

```
public interface Style {
	final static String CITY = "city";
	final static String NAME = "name";
	final static String STATE = "state";
	final static String STREET = "street";
	final static String ZIP = "zip";
	final static String HIGHLIGHT = "highlight";
}
```

Here's the sample CSS:

```
.name {
	font-size: 1em;
	font-weight: bold;
	color: blue;
	text-align: right;
	border-width: thin;
	padding-right: 1.5em;
	cursor: pointer;
}

table {
	border-collapse: collapse;
	border-width: medium;
	margin: 2em;
	border-style: outset;
	border-color: gray;
	border-collapse: collapse;
}

td {
	padding-left: 1em;
	padding-right: 1em;
	border-color: black;
	border-width: thin;
	border-style: inset;
	border-color: gray;
}

tr {
	padding: 5px;
}

.street {
	border-width: thin;
	border-right-width: 0;
}

.city {
	border-width: thin;
	border-left-width: 0;
	border-right-width: 0;
}

.state {
	border-width: thin;
	border-left-width: 0;
}

.zip {
	border-width: thin;
	cursor: pointer;
}

.highlight {
	background-color: yellow;
}
```