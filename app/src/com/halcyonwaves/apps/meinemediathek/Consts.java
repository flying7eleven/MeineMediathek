package com.halcyonwaves.apps.meinemediathek;

public class Consts {

	/**
	 * This string is used as a user-agent string while requesting a website.
	 */
	public final static String DESKTOP_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.122 Safari/534.30";

	public static final String EXTRA_NAME_MOVIE_DESCRIPTION = "description2";
	public static final String EXTRA_NAME_MOVIE_DOWNLOADLINK = "downloadlinkg2";
	public static final String EXTRA_NAME_MOVIE_PRVIEWIMAGEPATH = "previewimagepath2";
	public static final String EXTRA_NAME_MOVIE_TITLE = "title2";
	public static final String EXTRA_NAME_MOVIE_UNIQUE_ID = "uniqueid2";

	/**
	 * This constant is used to identify notifications which are used to show a onging download of a movie.
	 */
	public static final int NOTIFICATION_DOWNLOADING_MOVIE = 1;

	/**
	 * This preference key stores the application version code when the changelog was displayed the last time.
	 */
	public final static String PREFERENCE_CHANGELOG_DISPLAYED_LAST_TIME = "changelog.last_time_displayed";

	/**
	 *
	 */
	public final static String PREFERENCE_DOWNLOAD_NOTIFICATION_LAST_ID = "download.notification.last_id";

	/**
	 * This preference key is used to store if the user accepted the license of this application.
	 */
	public final static String PREFERENCE_LICENSE_ACCEPTED = "license.accepted";

	/**
	 * This preference key stores the time when the user accepted the license (for legal purposes).
	 */
	public final static String PREFERENCE_LICENSE_AGREEMENT_TIME = "license.accepted.time";

	/**
	 * This constant is used to tweak the socket timeout which should be used in the whole application.
	 */
	public static final int SOCKET_TIMEOUT_IN_SECONDS = 10;
}
