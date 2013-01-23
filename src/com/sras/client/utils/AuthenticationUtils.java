package com.sras.client.utils;

import java.util.Random;

import com.sras.auth.Encryption;

/**
 * User: dsonti Date: Jan 12, 2011 Time: 7:01:27 PM
 */
public final class AuthenticationUtils
{

	/**
	 * 
	 * @param actual
	 * @param provided
	 * @return
	 */
	public static boolean tryLogin(String actual, String provided)
	{
		return actual.equals(createPassword(provided));
	}

	/**
	 * 
	 * @param msg
	 * @return
	 */
	public static String createPassword(String msg)
	{
		String encryptedText = Encryption.encrypt(msg);
		return Encryption.encode(encryptedText);
	}

	/**
	 * this resets the user password initiated by admin user.
	 * 
	 * @param userId
	 * @param newPassword
	 * @return
	 */
	public boolean resetPassword(String userId, String newPassword)
	{
		// AuthenticationUtils.tryLogin();
		StringBuilder ad = new StringBuilder();
		for (int k = 0; k < 7; k++)
			ad.append((char) (65 + new Random().nextInt(256) % 62));
		String encNewPwd = AuthenticationUtils.createPassword(newPassword);
		//AuthenticationDAO.getInstance().resetUserPassword(userId, encNewPwd);
		return false;
	}
}
