package com.jwh.gwt.fasttable.client;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;

import com.google.gwt.dev.util.collect.HashSet;

public class TableTest {

	@Test
	public void nextTableId() throws Exception {
		final ArrayList<String> all = new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			all.add(Table.nextTableId());
		}
		final HashSet<String> set = new HashSet<String>(all);
		Assert.assertEquals(all.size(), set.size());
	}
}
