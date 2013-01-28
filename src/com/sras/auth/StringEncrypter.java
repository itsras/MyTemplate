package com.sras.auth;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class StringEncrypter {

	public static final String DES_ENCRYPTION_SCHEME = "DES";
	public static final String DEFAULT_ENCRYPTION_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAPMSpqvJj9FqXXtn3/oXAVnl5Ugw8+dV7UTYYM0FKpH2Ig1WiQz0E00aI/1fRfMc2DePBL8rBtRF4YOoG+iryOQBJi0yooCXKivUe4KHLzgzyWC+zFNrpr8BvTvcCu/p6KTMg2ZNdbfDjysW/6WxKTVW01XSsix2P/eydJ4vK/vpAgMBAAECgYAVVXW7bh1pS+SYiC1oTcyYj0zoYSOHEkuY10TnuaRa2zdx5D5zHJj1E25FNq90/vsbJTpNbQFUcC2y2HZanXksHgKyU4kYY3Bk7AbecqqbjqzuyibreXW4k1fyZ+taaR7A+xdkCVJbjsYol26warvDGS/EusTNuJiUhfBSz0094QJBAP0q50I3A427UPvGDIPTNTPEGFOLHtMudQx1JJBNjxeh6YkSLF77uOQqr7wdhwMVw5EB0lXRUTAZ1QWCL4JwM4sCQQD1ytXcr1HAw5Qm3wI6gA1Et2u1akHApnwdIs5PwYwPdw+QsEk1wg59C9qHEMTkXZluGj2WzUCCCn3j60crfCzbAkAifhLt+ZLZX5kEBJK17zPDbsclssGrYhv+a4NkybxiKuQnkbtL4Z3qMEqrJZa8e92eRxJCMPePxyU3z+nrKqBLAkAfKJt74m3GtgRt+Q9bQyhrcaUh1lqHhZNuTh9BVRPgUgOTIAXn3rXffEUzohiTqpnsrz7ngxWOZq4W2pinFpLJAkBpyFsx9Ru1ZjbZ1+8c47khP3kW6EgFMiNTts3UMR14Keer9o7OXSCosIvVlPSuqqUBTegk3eRfwbZ7V7CTcPu4";

	private final SecretKey key;
	private final Cipher cipher;
	private final BASE64Encoder base64encoder;
	private final BASE64Decoder base64decoder;

	private static final String UNICODE_FORMAT = "UTF8";

	public StringEncrypter(String encryptionScheme) throws EncryptionException {
		this(encryptionScheme, DEFAULT_ENCRYPTION_KEY);
	}

	public StringEncrypter(String encryptionScheme, String encryptionKey)
			throws EncryptionException {
		if (encryptionKey == null) {
			throw new IllegalArgumentException("encryption key was null");
		}
		if (encryptionKey.trim().length() < 24) {
			throw new IllegalArgumentException(
					"encryption key was less than 24 characters");
		}

		try {
			byte[] keyAsBytes = encryptionKey.getBytes(UNICODE_FORMAT);
			KeySpec keySpec = new DESKeySpec(keyAsBytes);
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance(encryptionScheme);

			this.key = keyFactory.generateSecret(keySpec);
			this.cipher = Cipher.getInstance(encryptionScheme);
		} catch (Exception e) {
			throw new EncryptionException(e);
		}
		this.base64encoder = new BASE64Encoder();
		this.base64decoder = new BASE64Decoder();
	}

	public synchronized String encrypt(String clearText)
			throws EncryptionException {
		if (clearText == null || clearText.trim().length() < 1) {
			throw new IllegalArgumentException(
					"clear-text string was null or empty");
		}

		try {
			this.cipher.init(Cipher.ENCRYPT_MODE, this.key);
			byte[] clearTextBytes = clearText.getBytes(UNICODE_FORMAT);
			byte[] cipherTextBytes = this.cipher.doFinal(clearTextBytes);

			return this.base64encoder.encode(cipherTextBytes);
		} catch (Exception e) {
			throw new EncryptionException(e);
		}
	}

	// this is not working.....
	public synchronized String decrypt(String clearText)
			throws EncryptionException {
		if (clearText == null || clearText.trim().length() < 1) {
			throw new IllegalArgumentException(
					"clear-text string was null or empty");
		}

		try {
			this.cipher.init(Cipher.DECRYPT_MODE, this.key);
			byte[] clearTextBytes = clearText.getBytes(UNICODE_FORMAT);
			byte[] cipherTextBytes = this.cipher.doFinal(clearTextBytes);

			return this.base64decoder.decodeBuffer(cipherTextBytes.toString())
					.toString();
		} catch (Exception e) {
			throw new EncryptionException(e);
		}
	}

	public static class EncryptionException extends RuntimeException {
		public EncryptionException(Throwable t) {
			super(t);
		}
	}

}