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

	private int rtmpHandle = 0;

	/**
	 * 
	 * @param rtmpUrl
	 * @return 
	 * @throws IOException
	 */
	private native int nativeInitStream( String rtmpUrl ) throws IOException;

	/**
	 * 
	 * @param rtmpHandle
	 * @return
	 * @throws IOException
	 */
	private native int nativeGetStreamLength( int rtmpHandle ) throws IOException;

	/**
	 * 
	 * @param rtmpHandle
	 * @throws IOException
	 */
	private native void nativeCloseStream( int rtmpHandle ) throws IOException;

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
		this.rtmpHandle = this.nativeInitStream( url );
	}

	/**
	 * 
	 * @see java.io.InputStream#close()
	 */
	@Override
	public void close() throws IOException {
		super.close();
		this.nativeCloseStream( this.rtmpHandle );
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public int getDurationInMs() throws IOException {
		return this.nativeGetStreamLength( this.rtmpHandle );
	}

	/**
	 * 
	 * @see java.io.InputStream#read()
	 */
	@Override
	public int read() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}
}
