package com.jwh.gwt.fasttable.db.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;

public class FastTableDb implements EntryPoint {

	@Override
	public void onModuleLoad() {
		if (Window.Location.getHref().indexOf("FastTableDb") < 0) {
			return;
		}

	}

}
