package com.jwh.gwt.fasttable.db.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DbRow implements IsSerializable {
	ArrayList<Comparable<?>> values = new ArrayList<Comparable<?>>();
}
