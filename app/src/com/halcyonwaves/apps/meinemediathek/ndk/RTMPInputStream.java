package com.halcyonwaves.apps.meinemediathek.ndk;

import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author thuetz
 *
 */
public class RTMPInputStream extends InputStream {

	/**
	 * 
	 */
	private static final String LOG = "RTMPInputStream";

	/**
	 * 
	 */
	private static boolean libLoaded = false;

	/**
	 * 
	 */
	private static synchronized void ensureLibLoaded() {
		if( !RTMPInputStream.libLoaded ) {
			System.loadLibrary( "rtmp" );
			RTMPInputStream.libLoaded = true;
		}
	}

	/**
	 * 
	 * @param url
	 * @throws IOException
	 */
	public RTMPInputStream( final String url ) throws IOException {
		RTMPInputStream.ensureLibLoaded();
	}

	/**
	 * 
	 * @throws IOException
	 */
	@Override
	public int read() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}
}
