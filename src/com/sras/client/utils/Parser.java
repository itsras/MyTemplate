/*
 *   	Copyright 2002-2003, AppIQ, Inc. All Rights Reserved
 */

package com.sras.client.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Category;

public class Parser
{
	private static Category log = Category.getInstance(Parser.class);

	public static Date parseDate(DateFormat format, String src) throws ParseException
	{
		Date date = null;

		try
		{
			if ((src != null) && (src.length() > 0))
			{
				date = format.parse(src);
			}
			else
			{
				log.debug("Skipping parse of empty date string");
			}
		}
		catch (ParseException e)
		{
			log.error("Failure parsing date string: " + src + " in format " + format, e);
			throw e;
		}

		return date;
	}

	public static Float parseCurrency(String src) throws ParseException
	{
		Float f = null;

		try
		{
			if ((src != null) && (src.length() > 0))
			{
				NumberFormat nf = NumberFormat.getCurrencyInstance();
				Number n = nf.parse(src);
				f = new Float(n.floatValue());
			}
			else
			{
				log.debug("Skipping parse of empty float string");
			}
		}
		catch (ParseException e)
		{
			log.error("Failure parsing currency: " + src, e);
			throw e;
		}

		return f;
	}

	public static Float parseFloat(String format, String src) throws ParseException
	{
		Float f = null;

		try
		{
			if ((src != null) && (src.length() > 0))
			{
				NumberFormat nf = new DecimalFormat(format);
				Number n = nf.parse(src);
				f = new Float(n.floatValue());
			}
			else
			{
				log.debug("Skipping parse of empty float string");
			}
		}
		catch (ParseException e)
		{
			log.error("Cannot parse float: " + src, e);
			throw e;
		}

		return f;
	}

	public static Number parseNumber(String format, String src) throws ParseException
	{
		Number n = null;

		try
		{
			if ((src != null) && (src.length() > 0))
			{
				NumberFormat nf = new DecimalFormat(format);
				n = nf.parse(src);
			}
			else
			{
				log.debug("Skipping parse of empty number string");
			}
		}
		catch (ParseException e)
		{
			log.error("Cannot parse number: " + src, e);
			throw e;
		}

		return n;
	}

	static public Map parseParameters(String parameterString, String splitString)
	{
		HashMap parameters = new HashMap();
		String[] nameValuePairs = parameterString.split(splitString);
		for (int i = 0; i < nameValuePairs.length; i++)
		{
			String nameValuePair = nameValuePairs[i];
			int equalSign = nameValuePair.indexOf('=');
			if (equalSign <= 0)
			{
				if (parameterString.length() > 0)
					log.error("Bad parameter string: " + parameterString + " for " + nameValuePair);
				continue;
			}
			String parameterName = nameValuePair.substring(0, equalSign);
			String parameterValue = nameValuePair.substring(equalSign + 1);
			parameters.put(parameterName, parameterValue);
		}
		return parameters;
	}

}
