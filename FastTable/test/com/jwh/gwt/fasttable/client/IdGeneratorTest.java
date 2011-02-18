package com.jwh.gwt.fasttable.client;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;

import com.google.gwt.dev.util.collect.HashSet;
import com.jwh.gwt.fasttable.client.element.Table;
import com.jwh.gwt.fasttable.client.util.IdGenerator;

public class IdGeneratorTest {

	@Test
	public void nextTableId() throws Exception {
		final ArrayList<String> all = new ArrayList<String>();
		for (int i = 0; i < 1000; i++) {
			String id = IdGenerator.getNextId();
			System.out.println(id);
			all.add(id);
		}
		final HashSet<String> set = new HashSet<String>(all);
		Assert.assertEquals(all.size(), set.size());
	}
}