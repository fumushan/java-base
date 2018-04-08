package com.base.util;

import java.util.Random;
import java.util.UUID;

public class IdUtil {

	private static String[] base = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "m", "n", "p", "q",
			"r", "s", "t", "u", "v", "w", "x", "y", "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "M", "N", "P",
			"Q", "R", "S", "T", "U", "V", "W", "X", "Y", "2", "3", "4", "5", "6", "7", "8", "9" };

	private static int baseLength = base.length - 1;

	private static IdUtil uniqueIdentity = null;

	private IdUtil() {
	};

	public static IdUtil getInstance() {

		if (uniqueIdentity == null) {
			uniqueIdentity = new IdUtil();
		}
		return uniqueIdentity;
	}

	public String getIdentity(int length) {

		Random random = new Random();
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < length; i++) {
			str.append(base[random.nextInt(baseLength)]);
		}
		return str.toString();
	}

	public String uuid() {

		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
