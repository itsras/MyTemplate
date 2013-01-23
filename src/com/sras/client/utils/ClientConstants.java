package com.sras.client.utils;

import java.util.Hashtable;

import com.sras.datamodel.UserData;

public class ClientConstants
{
	public static final String servletPage = "a"; // This value should reflect
													// the same value defined
													// for
													// welcome-file and
													// url-pattern in web.xml
													// file
	public static final String pageVerb = "page";
	public static final String servletPageWithAction = servletPage + "?" + pageVerb + "=";
	public static final String servletPageWithAjaxAction = servletPage + "?ajax=true&" + pageVerb
			+ "=";
	public static final String servletPageWithDefaultAction = servletPage + "?" + pageVerb
			+ "=Home";

	public static final String errorTextVariableName = "ErrorText";
	public static final String dontUseFrameworkVariableName = "dontUseFramework";

	public static final String loginRequest = "login_request";

	public static final String ajaxResponseData = "ajax_response_data";
	public static final String ajaxTemplate = "ajax_template.vm";

	public static final String loginFailureCount = "login_failure_count";

	public static final String domainName = "clicktobuy.com";
	public static final String RECAPTCHA_PUBLIC_KEY = "6LfPgNsSAAAAAASO2ef1Y4jeOy0kvWSSfIkyVcBo";
	public static final String RECAPTCHA_PRIVATE_KEY = "6LfPgNsSAAAAAP_J8UgX4SKhU075ar1blw0VR5zx";

	public static final int COOKIE_AGE = 2592000;
	public static final String COOKIE_NAME = "6LfPgNsSAAAAAASO2ef1Y4jeOy0kvWSSfIkyVcBo";

	public static final String productName = "Click to Buy";
	public static final String productCaption = "A place where you can get the things for free of cost!";

	public static final Hashtable<String, UserData> sessions = new Hashtable<String, UserData>();

}
