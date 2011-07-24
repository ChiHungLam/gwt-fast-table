package com.jwh.gwt.fasttable.client.element;

public class FunctionBuilder<T> {

	/**
	 * counter to assure unique function names
	 */
	private static int requestCounter = 0;

	/**
	 * create a callback function 
	 * @param obj the callback receiver
	 * @return the name to be used when invoking the function
	 */
	public String registerCallbackFunction(Table<T> obj) {

		String dynamicName = "jwht" + (requestCounter++);
		// you can write your own logic to generate dynamic name for

		createDynamicFunction(obj, dynamicName);
		return dynamicName;
	}

	/**
	 * uses native features to create the function
	 * @param x
	 * @param dynamicName
	 */
	private native void createDynamicFunction(Table<T> obj,
			String dynamicName)/*-{
		tmp = function(stubId,objectId,event,columnIndex) {
			obj.@com.jwh.gwt.fasttable.client.element.Table::handleCellEvent(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)(stubId,objectId,event,columnIndex);
		};
		$wnd[dynamicName] = tmp;
	}-*/;

}