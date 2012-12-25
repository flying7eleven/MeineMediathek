package com.halcyonwaves.apps.meinemediathek;

public class Consts {

	/**
	 * This string is used as a user-agent string while requesting a website.
	 */
	public final static String DESKTOP_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.122 Safari/534.30";

	public static final String EXTRA_NAME_MOVIE_TITLE = "title";
	public static final String EXTRA_NAME_MOVIE_DESCRIPTION = "description";
	public static final String EXTRA_NAME_MOVIE_DOWNLOADLINK = "downloadlink";
	public static final String EXTRA_NAME_MOVIE_PRVIEWIMAGEPATH = "previewimagepath";
	public static final String EXTRA_NAME_MOVIE_UNIQUE_ID = "uniqueid";

	/**
	 * This constant is used to identify notifications which are used to show a onging download of a movie.
	 */
	public static final int NOTIFICATION_DOWNLOADING_MOVIE = 1;

	/**
	 * This constant is used to tweak the socket timeout which should be used in the whole application.
	 */
	public static final int SOCKET_TIMEOUT_IN_SECONDS = 10;

	/**
	 *  This preference key is used to determine if the movie images should be pre-loaded or not.
	 */
	public final static String PREFERENCE_PRELOAD_MOVIE_IMAGES = "download.preload.images";
	
	/**
	 * This preference key stores the application version code when the changelog was displayed the last time.
	 */
	public final static String PREFERENCE_CHANGELOG_DISPLAYED_LAST_TIME = "changelog.last_time_displayed";

	/**
	 * This preference key is used to store if the user accepted the license of this application.
	 */
	public final static String PREFERENCE_LICENSE_ACCEPTED = "license.accepted";

	/**
	 * This preference key stores the time when the user accepted the license (for legal purposes).
	 */
	public final static String PREFERENCE_LICENSE_AGREEMENT_TIME = "license.accepted.time";
	
	/**
	 *
	 */
	public final static String PREFERENCE_DOWNLOAD_NOTIFICATION_LAST_ID = "download.notification.last_id";
}
