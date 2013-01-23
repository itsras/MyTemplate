package com.sras.client.utils;

public class Formatter
{
	public static String makeSingleLine(String s)
	{
		if (s == null)
		{
			return "";
		}

		int prevLength = s.length();
		StringBuffer stringBuffer = new StringBuffer(prevLength + 1);
		char lastChar = ' ';
		for (int i = 0; i < prevLength; i++)
		{
			char c = s.charAt(i);

			// replace newlines and carriage returns with spaces
			if (c == '\n' || c == '\r')
				c = ' ';

			// escape all quotes
			if (c == '"' && lastChar != '\\')
				stringBuffer.append('\\');

			stringBuffer.append(c);
			lastChar = c;
		}
		return stringBuffer.toString();
	}
}
