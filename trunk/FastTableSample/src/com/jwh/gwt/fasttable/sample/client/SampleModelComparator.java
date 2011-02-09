package com.jwh.gwt.fasttable.sample.client;

import java.util.Comparator;

public class SampleModelComparator implements Comparator<SampleModel> {

	public SampleModelComparator(int column, boolean ascending) {
		super();
		this.column = column;
		this.ascending = ascending;
	}

	final int column;
	final boolean ascending;
	
	@Override
	public int compare(SampleModel o1, SampleModel o2) {
		String a = get(o1);
		String b = get(o2);
		return ascending ? a.compareTo(b) : b.compareTo(a);
	}

	private String get(SampleModel m) {
		switch (column) {
		case 1:			
			return m.name;
		case 2:			
			return m.street;
		case 3:			
			return m.city;
		case 4:			
			return m.state;
		case 5:			
			return m.zip;
		default:
			break;
		}
		return "";
	}
	

}
