package com.sras.client.utils;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.session.StandardSession;
import org.apache.catalina.session.StandardSessionFacade;
import org.apache.log4j.Category;

public class Utilities
{
	private static Category log = Category.getInstance(Utilities.class);
	private static HashMap<String, String> hostNameMap = new HashMap<String, String>();
	public static final DateFormat timeFormat = new SimpleDateFormat("HH:mm");

	public static String getRemoteHostName(HttpServletRequest request)
	{
		String remoteHost = request.getRemoteHost();
		String remoteIpAddress = remoteHost;
		remoteHost = (String) hostNameMap.get(remoteIpAddress);
		if (remoteHost != null)
		{
			return remoteHost;
		}
		remoteHost = remoteIpAddress;
		try
		{
			InetAddress inetAddress = InetAddress.getByName(remoteHost);
			remoteHost = inetAddress.getHostName();
			hostNameMap.put(remoteIpAddress, remoteHost);
		}
		catch (Exception e)
		{
		}
		return remoteHost;
	}

	public static String getRequestedURL(HttpServletRequest request)
	{
		String url = request.getRequestURI();
		String queryString = request.getQueryString();
		if (queryString != null && queryString.length() > 0)
		{
			url += "?" + queryString;
		}
		return url;
	}

	/**
	 * Escapes quotes and backslashes for use in JavaScript. Also changes all
	 * newlines and carriage returns to spaces.
	 * 
	 * @param input
	 *            unescaped string
	 * @return escaped string
	 */
	public static String escapeForJavascript(String input)
	{
		if (input == null || input.length() == 0)
			return "";
		int inputLength = input.length();
		StringBuffer output = new StringBuffer(inputLength + 10);
		boolean modified = false;
		for (int i = 0; i < inputLength; i++)
		{
			char c = input.charAt(i);
			switch (c)
			{
				case '"':
				case '\\':
					output.append('\\');
					modified = true;
					break;

				case '\'':
				case '\n':
				case '\r':
					c = ' '; // JavaScript doesn't like multiline text
					modified = true;
					break;
			}
			output.append(c);
		}
		if (modified)
		{
			/*
			 * log.debug("in [escapeForJavascript] we changed the message  \"" +
			 * input + "\"  TO  \"" + output + "\"");
			 */
		}
		return output.toString();
	}

	public static String escapeXSS(String s, boolean replaceSingleQuotes)
	{
		if (null == s)
			return "";
		s = s.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;");
		if (replaceSingleQuotes)
		{
			s = s.replaceAll("'", "&#39;");
		}
		return s;
	}

	public static String escapeXSS(String s)
	{
		return escapeXSS(s, true);
	}

	public static String getCookieValue(HttpServletRequest request, final String cookieName)
	{
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
		{
			for (int i = 0; i < cookies.length; i++)
			{
				Cookie cookie = cookies[i];
				if (cookie.getName().equalsIgnoreCase(cookieName))
				{
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	public static void addCookie(HttpServletResponse response, String name, String value, int maxAge)
	{
		Cookie cookie = new Cookie(name, value);
		// cookie.setSecure(true);
		cookie.setComment("Used to keep the user logged in");
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}

	public static void removeCookie(HttpServletResponse response, String name)
	{
		addCookie(response, name, null, 0);
	}

	public static void markSessionAsAccessed(final HttpSession session)
	{
		log.debug("mark the session as accessed at the end of the request, not just the	beginning");
		// mark the session as accessed at the end of the request, not just the
		// beginning
		if (session instanceof StandardSessionFacade)
		{
			final StandardSessionFacade standardSessionFacade = (StandardSessionFacade) session;
			final Class<StandardSessionFacade> standardSessionFacadeClass = StandardSessionFacade.class;
			AccessController.doPrivileged(new PrivilegedAction<Object>()
			{
				public Object run()
				{
					try 
					{
						Field sessionField = standardSessionFacadeClass.getDeclaredField("session");
						sessionField.setAccessible(true);
						final StandardSession tomcatSession = (StandardSession) sessionField
								.get(standardSessionFacade);
						tomcatSession.access();
						tomcatSession.endAccess();
					}
					catch (Exception e)
					{
						log.debug(e.getMessage(), e);
					}
					return null;
				}
			});
		}
	}

	/**
	 * Gets the value of a parameter as a long
	 * 
	 * @param request
	 * @param parameterName
	 * @return Long value, or None if parameter is not found or is not numeric
	 */
	public static Long getLongFromRequest(HttpServletRequest request, String parameterName)
	{
		String parameterValue = request.getParameter(parameterName);
		if (parameterValue == null)
			return null;
		Long returnValue = null;
		try
		{
			returnValue = new Long(parameterValue.trim());
		}
		catch (NumberFormatException e)
		{
			log.debug(e.getMessage(), e);
			return null;
		}
		return returnValue;
	}

	public static Double getDoubleFromRequest(HttpServletRequest request, String parameterName)
	{
		String parameterValue = request.getParameter(parameterName);
		if (parameterValue == null)
			return null;
		Double returnValue = null;
		try
		{
			returnValue = new Double(parameterValue);
		}
		catch (NumberFormatException e)
		{
			log.debug(e.getMessage(), e);
			return null;
		}
		return returnValue;
	}

	public static boolean getBooleanFromRequest(HttpServletRequest request, String parameterName)
	{
		String parameterValue = request.getParameter(parameterName);

		if (parameterValue == null || parameterValue.length() == 0
				|| parameterValue.equalsIgnoreCase("false"))
			return false;
		return true;
	}

	public static boolean getBooleanTrueFromRequest(HttpServletRequest request, String parameterName)
	{
		String parameterValue = request.getParameter(parameterName);

		if (parameterValue != null
				&& (parameterValue.equalsIgnoreCase("true") || parameterValue
						.equalsIgnoreCase("on")))
			return true;
		return false;
	}

	public static Date getDateFromRequest(HttpServletRequest request, String parameterName)
	{
		String parameterValue = request.getParameter(parameterName);
		if (parameterValue == null)
			return null;
		final String[] parts = parameterValue.split("-");
		if (parts.length != 3)
			return null;
		try
		{
			Long year = new Long(parts[0]);
			Long month = new Long(parts[1]);
			Long date = new Long(parts[2]);
			final Calendar calendar = Calendar.getInstance();
			calendar.clear();
			calendar.set(year.intValue(), month.intValue() - 1, date.intValue());
			return calendar.getTime();
		}
		catch (Exception e)
		{
			log.debug(e.getMessage(), e);
			return null;
		}
	}

	public static Date getRequiredDateFromRequest(HttpServletRequest request, String parameterName)
			throws Exception
	{
		final Date returnValue = getDateFromRequest(request, parameterName);
		if (returnValue == null)
			throw new Exception("Date cannot be null or empty");
		return returnValue;
	}

	public static Date getTimeFromRequest(HttpServletRequest request, String parameterName)
			throws Exception
	{
		String parameterValue = request.getParameter(parameterName);
		if (parameterValue == null)
			return null;
		try
		{
			Date time = Parser.parseDate(timeFormat, parameterValue);
			if (time == null)
			{
				throw new Exception("Time cannot be null or empty");
			}
			// log.debug("Got time_date = " + time_date);
			return time;
		}
		catch (java.text.ParseException e)
		{
			// log.error("", e);
			throw e;
		}
	}

	public static Date getRequiredTimeFromRequest(HttpServletRequest request, String parameterName)
			throws Exception
	{
		final Date returnValue = getTimeFromRequest(request, parameterName);
		if (returnValue == null)
			throw new Exception("Time cannot be null or empty");
		return returnValue;
	}

	public static Long getRequiredLongFromRequest(HttpServletRequest request, String parameterName)
			throws Exception
	{
		final Long returnValue = getLongFromRequest(request, parameterName);
		if (returnValue == null)
			throw new Exception("Value cannot be null or empty");
		return returnValue;
	}

	public static Double getRequiredDoubleFromRequest(HttpServletRequest request,
			String parameterName) throws Exception
	{
		final Double returnValue = getDoubleFromRequest(request, parameterName);
		if (returnValue == null)
			throw new Exception("Value cannot be null or empty");
		return returnValue;
	}

	public static int getRequiredIntFromRequest(HttpServletRequest request, String parameterName)
			throws Exception
	{
		final Long returnValue = getRequiredLongFromRequest(request, parameterName);
		return returnValue.intValue();
	}

	public static String getRequiredStringFromRequest(HttpServletRequest request,
			String parameterName) throws Exception
	{
		final String returnValue = request.getParameter(parameterName);
		if (returnValue == null || returnValue.length() == 0)
			throw new Exception("Value cannot be null or empty");
		return returnValue;
	}
}
