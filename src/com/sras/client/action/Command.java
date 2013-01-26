package com.sras.client.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.apache.log4j.Category;
import org.apache.velocity.context.Context;

import com.sras.client.utils.ClientConstants;
import com.sras.client.utils.Utilities;

public abstract class Command {
	protected static Category log = Category.getInstance(Command.class);
	protected static String TEMPLATE_NAME = "index.vm";

	public HttpServletRequest request;
	public HttpServletResponse response;
	public Context ctx;

	public boolean isPost;
	public boolean isGet;
	public boolean isAjax;

	public Command(HttpServletRequest request, HttpServletResponse response,
			Context ctx) {
		this.request = request;
		this.response = response;
		this.ctx = ctx;

		this.isPost = request.getMethod().equalsIgnoreCase("post");
		this.isGet = request.getMethod().equalsIgnoreCase("get");
		this.isAjax = Boolean.parseBoolean(request.getParameter("ajax"));
	}

	public static void redirectToURL(HttpServletRequest request,
			HttpServletResponse response, String url) throws IOException {
		// prepend the context?
		char firstChar = url.charAt(0);
		if (firstChar != '\'' && firstChar != '/') {
			String contextPath = request.getContextPath();
			log.info("prepending contextPath of " + contextPath);
			url = contextPath + "/" + url;
		}
		log.info("about to redirect to " + url);
		response.sendRedirect(url);
	}

	public static void redirectToLoginPage(HttpServletRequest request,
			HttpServletResponse response, Context context) throws IOException,
			ServletException {
		HttpSession session = request.getSession();
		String requestedURL = Utilities.getRequestedURL(request);
		log.info("About to redirect to login page. requestedURL="
				+ requestedURL);

		session.setAttribute(ClientConstants.loginRequest, requestedURL);
		Command.redirectToURL(request, response,
				ClientConstants.servletPageWithAction + "login");
	}

	// Returns the request parameter value as well as adds the same value into
	// the context
	public String addToContext(String reqParam) throws Exception {
		return addToContext(reqParam, false);
	}

	public String addToContext(String reqParam, boolean isRequired)
			throws Exception {
		String value = request.getParameter(reqParam);
		if ((value == null || value.trim().length() == 0) && isRequired) {
			throw new Exception(reqParam + " cannot be null or empty");
		}
		ctx.put(reqParam, value);
		return value;
	}

	public boolean verifyReCaptcha() throws Exception {
		String recaptchaRespField = request
				.getParameter("recaptcha_response_field");
		String recaptchaChalField = request
				.getParameter("recaptcha_challenge_field");

		if (recaptchaChalField == null
				|| recaptchaChalField.trim().length() == 0
				|| recaptchaRespField == null
				|| recaptchaRespField.trim().length() == 0) {
			throw new Exception("Details to recaptcha are not specified");
		}
		ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
		reCaptcha.setPrivateKey(ClientConstants.RECAPTCHA_PRIVATE_KEY);
		ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(
				ClientConstants.domainName, recaptchaChalField,
				recaptchaRespField);

		return reCaptchaResponse.isValid();
	}

	public String execute() throws Exception {
		if (isPost && !isAjax) {
			return doPost();
		} else if (isPost && isAjax) {
			return doAjaxPost();
		} else if (isGet && !isAjax) {
			return doGet();
		} else if (isGet && isAjax) {
			return doAjaxGet();
		}
		return null;

	}

	public String doPost() throws Exception {
		return TEMPLATE_NAME;
	}

	public String doGet() throws Exception {
		return TEMPLATE_NAME;
	}

	public String doAjaxPost() throws Exception {
		return TEMPLATE_NAME;
	}

	public String doAjaxGet() throws Exception {
		return TEMPLATE_NAME;
	}
}
