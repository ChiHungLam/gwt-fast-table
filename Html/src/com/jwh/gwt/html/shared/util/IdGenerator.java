package com.jwh.gwt.html.shared.util;

public final class IdGenerator {

	static int instanceCounter = 1;

	public static String getNextId() {
		final String validCharacters = "QWERTYUIOPASDFGHJKLZXCVBNM";
		final StringBuilder b = new StringBuilder();
		int current = instanceCounter++;
		while (current > 0) {
			final int remainder = current % validCharacters.length();
			b.append(validCharacters.charAt(remainder));
			current = current / validCharacters.length();
		}
		return b.toString();
	}
}
