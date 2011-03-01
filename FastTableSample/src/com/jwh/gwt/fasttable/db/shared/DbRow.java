package com.jwh.gwt.fasttable.db.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DbRow implements IsSerializable {
	public ArrayList<Comparable<?>> values = new ArrayList<Comparable<?>>();
	public void add(Comparable<?> obj) {
		values.add(obj);
	}
	public void add(byte[] bytes) {
		add(String.valueOf(bytes));
	}
}
