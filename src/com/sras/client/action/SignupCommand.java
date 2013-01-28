package com.sras.client.action;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.apache.velocity.context.Context;

import com.sras.client.utils.AuthenticationUtils;
import com.sras.client.utils.ClientConstants;
import com.sras.client.utils.MailUtils;
import com.sras.dao.ModelFactory;
import com.sras.dao.UserDao;
import com.sras.datamodel.UserData;

public class SignupCommand extends Command {
	private static String TEMPLATE_NAME = "login.vm";
	protected static Category log = Category.getInstance(SignupCommand.class);

	public SignupCommand(HttpServletRequest request,
			HttpServletResponse response, Context ctx) {
		super(request, response, ctx);
	}

	public String doPost() throws Exception {
		try {
			String activationKey = UUID.randomUUID().toString()+UUID.randomUUID().toString();
			handleSignup(request, response, ctx, activationKey);
			LoginCommand lc = new LoginCommand(request, response, ctx);
			TEMPLATE_NAME = lc.execute();
		} catch (Exception e) {
			log.debug(e);
			ctx.put(ClientConstants.errorTextVariableName + "-signup",
					"Please provide valid details to signup");
			TEMPLATE_NAME = "login.vm";
		}
		return TEMPLATE_NAME;
	}

	private long handleSignup(HttpServletRequest request,
			HttpServletResponse response, Context ctx, String activationKey) throws Exception {
		try {
			String firstName = addToContext("firstName", true);
			String lastName = addToContext("lastName", true);
			String suserName = addToContext("suserName", true);
			String suserName1 = addToContext("suserName1", true);
			String spassword = addToContext("spassword", true);
			String gender = addToContext("gender", true);

			if (verifyReCaptcha()) {
				log.debug("ReCaptcha answer was entered correctly!");
			} else {
				throw new Exception("Answer is wrong");
			}

			int sex = gender.equalsIgnoreCase("male") ? 0 : 1;

			log.debug("logging in: handleSignup");
			// TODO: Verify User Credentials
			UserData user = new UserData();
			user.setUserName(suserName);
			user.setPassword(AuthenticationUtils.createPassword(spassword));
			user.setDob(null);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setMailId(suserName);
			user.setSex(sex);
			user.setActive(false);
			user.setActivationKey(activationKey);

			UserDao userDao = (UserDao) ModelFactory.getImplementation(user);
			return userDao.create();
		} catch (Exception e) {
			throw e;
		}
	}
}
