package com.sras.auth;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;

public class Encryption
{

	protected static final byte[] base64Chars =
	{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
			'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/', };

	private static StringEncrypter encrypter = null;
	public static final String MD = "md5";

	private Encryption()
	{
	}

	public static String encrypt(String clearText)
	{
		return getEncrypter().encrypt(clearText);
	}

	public static String decrypt(String clearText)
	{
		return getEncrypter().decrypt(clearText);
	}

	private static StringEncrypter getEncrypter()
	{
		synchronized (Encryption.class)
		{
			if (Encryption.encrypter == null)
			{
				Encryption.encrypter = new StringEncrypter(StringEncrypter.DES_ENCRYPTION_SCHEME);
			}
		}
		return Encryption.encrypter;
	}

	/**
	 * This method returns the encrypted MessageDigest string for the user
	 * passed argument string.
	 * 
	 * @param strMessage
	 *            String for which MessageDigest is to be computed.
	 * @return ciphertext of supplied string.
	 */
	public static String encode(String strMessage)
	{

		byte[] bArrCipherText = null;
		try
		{
			MessageDigest md = MessageDigest.getInstance(MD);
			if (strMessage != null)
			{
				md.update(strMessage.getBytes());

				bArrCipherText = md.digest(); // digest the clear text.
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return new String(encodeBase64(bArrCipherText));
	}

	private static byte[] encodeBase64(byte[] bytes)
	{
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		int mod;
		int length = bytes.length;
		if ((mod = length % 3) != 0)
		{
			length += 3 - mod;
		}
		length = length * 4 / 3;
		ByteArrayOutputStream out = new ByteArrayOutputStream(length);
		try
		{
			encodeBase64(in, out, false);
		}
		catch (IOException x)
		{
			// This can't happen.
			// The input and output streams were constructed
			// on memory structures that don't actually use IO.
		}
		return out.toByteArray();
	}

	/**
	 * Encode data from the InputStream to the OutputStream in Base64.
	 * 
	 * @param in
	 *            Stream from which to read data that needs to be encoded.
	 * @param out
	 *            Stream to which to write encoded data.
	 * @param lineBreaks
	 *            Whether to insert line breaks every 76 characters in the
	 *            output.
	 * @throws java.io.IOException
	 *             ioexception
	 */
	private static void encodeBase64(InputStream in, OutputStream out, boolean lineBreaks)
			throws IOException
	{

		int[] inBuffer = new int[3];
		int lineCount = 0;

		boolean done = false;
		while (!done && (inBuffer[0] = in.read()) != -1)
		{
			inBuffer[1] = in.read();
			inBuffer[2] = in.read();

			out.write(base64Chars[inBuffer[0] >> 2]);
			if (inBuffer[1] != -1)
			{
				out.write(base64Chars[((inBuffer[0] << 4) & 0x30) | (inBuffer[1] >> 4)]);
				if (inBuffer[2] != -1)
				{
					out.write(base64Chars[((inBuffer[1] << 2) & 0x3c) | (inBuffer[2] >> 6)]);
					out.write(base64Chars[inBuffer[2] & 0x3F]);
				}
				else
				{
					out.write(base64Chars[((inBuffer[1] << 2) & 0x3c)]);
					out.write('=');
					done = true;
				}
			}
			else
			{
				out.write(base64Chars[((inBuffer[0] << 4) & 0x30)]);
				out.write('=');
				out.write('=');
				done = true;
			}
			lineCount += 4;
			if (lineBreaks && lineCount >= 76)
			{
				out.write('\n');
				lineCount = 0;
			}
		}
	}

	public static void main()
	{
		// empty body
	}
}
