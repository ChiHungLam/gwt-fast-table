package com.jwh.gwt.html.client;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import com.google.gwt.dev.util.collect.HashSet;
import com.jwh.gwt.html.shared.util.IdGenerator;

public class IdGeneratorTest {

	@Test
	public void nextTableId() throws Exception {
		final ArrayList<String> all = new ArrayList<String>();
		for (int i = 0; i < 1000; i++) {
			final String id = IdGenerator.getNextId();
			System.out.println(id);
			all.add(id);
		}
		final HashSet<String> set = new HashSet<String>(all);
		assertEquals(all.size(), set.size());
	}
}
