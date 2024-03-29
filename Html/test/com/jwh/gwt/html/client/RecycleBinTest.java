package com.jwh.gwt.html.client;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.jwh.gwt.html.shared.util.RecycleBin;

public class RecycleBinTest {

	private RecycleBin<String> recycleBin;

	@Before
	public void before() {
		recycleBin = new RecycleBin<String>(new String[] { "A", "B", "C" });
	}

	@Test
	public void testGet() throws Exception {
		assertNotNull(recycleBin.get());
		assertNotNull(recycleBin.get());
		assertNotNull(recycleBin.get());
	}

	@Test
	public void testGetNotFound() throws Exception {
		assertNotNull(recycleBin.get());
		assertNotNull(recycleBin.get());
		assertNotNull(recycleBin.get());
		try {
			assertNotNull(recycleBin.get());
		} catch (final Exception e) {
			// should fail
			return;
		}
		fail();
	}

	@Test
	public void testPut() throws Exception {
		final String one = recycleBin.get();
		final String two = recycleBin.get();
		final String three = recycleBin.get();
		recycleBin.put(one);
		recycleBin.put(two);
		recycleBin.put(three);
		recycleBin.put("extra");
	}
}
