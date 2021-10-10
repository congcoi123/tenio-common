/*
The MIT License

Copyright (c) 2016-2021 kong <congcoi123@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.tenio.common.utilities;

import java.util.UUID;

/**
 * A collection of utility methods for strings.
 * 
 * @author kong
 */
public final class StringUtility {

	private static String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";

	private StringUtility() {
		
	}
	
	/**
	 * To generate {@code String} for logging information by the corresponding
	 * objects
	 * 
	 * @param objects the corresponding objects, {@link Object}
	 * @return a string value
	 */
	public static String strgen(Object... objects) {
		StringBuilder builder = new StringBuilder();
		for (var object : objects) {
			builder.append(object);
		}
		return builder.toString();
	}

	/**
	 * To generate an unique string in UUID format
	 * 
	 * @return an unique string
	 */
	public static String getRandomUUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * To generate a randomized string
	 * 
	 * @param length limited size of the text
	 * @return a randomized string that could be duplicated
	 */
	public static String getRandomTextByLength(int length) {
		// create StringBuffer size of AlphaNumericString
		StringBuilder sb = new StringBuilder(length);

		for (int i = 0; i < length; i++) {

			// generate a random number between
			// 0 to AlphaNumericString variable length
			int index = (int) (ALPHA_NUMERIC_STRING.length() * Math.random());

			// add Character one by one in end of sb
			sb.append(ALPHA_NUMERIC_STRING.charAt(index));
		}

		return sb.toString();
	}

}
