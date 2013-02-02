package com.sras.client.utils;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.sras.dao.ModelFactory;
import com.sras.dao.UserDao;
import com.sras.dao.UserSessionDao;
import com.sras.datamodel.UserData;
import com.sras.datamodel.UserSessionData;

public class SessionHelper {

	public static void createUserSession(HttpServletRequest request,
			String uuid, long userId, long timeout) throws Exception {
		UserSessionData userSessionData = new UserSessionData();
		userSessionData.setSessionId(uuid);
		userSessionData.setUserId(userId);
		userSessionData.setTimeout(timeout);
		Locale locale = request.getLocale();
		userSessionData.setBrowser(request.getHeader("User-Agent"));
		userSessionData.setLanguage(locale.getLanguage());
		userSessionData.setCountry(locale.getDisplayCountry());
		userSessionData.setHostName(request.getRemoteHost());
		userSessionData.setIpAddress(request.getRemoteAddr());

		UserSessionDao userDao = (UserSessionDao) ModelFactory
				.getImplementation(userSessionData);
		userDao.create();
	}

	public static void updateUserSession(HttpServletRequest request,
			String uuid, long userId, long timeout) throws Exception {
		UserSessionData userSessionData = new UserSessionData();
		userSessionData.setSessionId(uuid);
		userSessionData.setUserId(userId);
		if (timeout > 0) {
			userSessionData.setTimeout(timeout);
		}
		Locale locale = request.getLocale();
		userSessionData.setBrowser(request.getHeader("User-Agent"));
		userSessionData.setLanguage(locale.getLanguage());
		userSessionData.setCountry(locale.getDisplayCountry());
		userSessionData.setHostName(request.getHeader("Host"));
		userSessionData.setIpAddress(request.getLocalAddr());

		UserSessionDao userDao = (UserSessionDao) ModelFactory
				.getImplementation(userSessionData);
		userDao.update();
	}

	public static void deleteUserSession(String uuid, long userId)
			throws Exception {
		UserSessionData userSessionData = new UserSessionData();
		userSessionData.setSessionId(uuid);
		userSessionData.setUserId(userId);

		UserSessionDao userDao = (UserSessionDao) ModelFactory
				.getImplementation(userSessionData);
		userDao.delete();
	}

	public static UserData getUserFromSession(String uuid) throws Exception {
		UserSessionData userSessionData = new UserSessionData();
		userSessionData.setSessionId(uuid);
		UserSessionDao userSessionDao = (UserSessionDao) ModelFactory
				.getImplementation(userSessionData);
		userSessionData = (UserSessionData) userSessionDao.read();

		if (userSessionData != null) {
			UserData userData = new UserData();
			userData.setId(userSessionData.getUserId());
			UserDao userDao = (UserDao) ModelFactory
					.getImplementation(userData);
			return (UserData) userDao.read();
		}
		return null;
	}

	public static UserSessionData getUserSession(String uuid) throws Exception {
		UserSessionData userSessionData = new UserSessionData();
		userSessionData.setSessionId(uuid);
		UserSessionDao userSessionDao = (UserSessionDao) ModelFactory
				.getImplementation(userSessionData);
		userSessionData = (UserSessionData) userSessionDao.read();
		return userSessionData;
	}

}
