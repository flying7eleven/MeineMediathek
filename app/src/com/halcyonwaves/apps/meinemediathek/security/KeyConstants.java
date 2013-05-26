package com.halcyonwaves.apps.meinemediathek.security;

/**
 * This file holds the placeholder variables for the licensing parts
 * of the App. In the open source version of this App, this ensures that
 * the App will build and run without the licensing checks.
 * 
 * @author Tim Huetz
 */
public class KeyConstants {

	/**
	 * Since the Open Source version of the App is not distributed with
	 * the licensing keys and therefore no license checks, the security feature
	 * should be disabled in the OS version :)
	 */
	public static final boolean isOpenSource = true;

	/**
	 * The salt which is used for licensing checks.
	 */
	public static final byte[] SALT = new byte[] {};

	/**
	 * The Base64 encoded public key for the licensing checks.
	 */
	public static final String BASE64_PUBLIC_KEY = "";

}
