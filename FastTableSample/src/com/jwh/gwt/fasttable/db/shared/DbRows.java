package com.jwh.gwt.fasttable.db.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DbRows implements IsSerializable {

	public ArrayList<String> columnNames = new ArrayList<String>();
	public ArrayList<Integer> columnTypes = new ArrayList<Integer>();
	public ArrayList<DbRow> rows = new ArrayList<DbRow>();
	
}
