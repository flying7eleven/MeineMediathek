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
	 * @param rtmpHandle
	 * @param bytes
	 * @param offset
	 * @param len
	 * @return
	 * @throws IOException
	 */
	private native int nativeReadStream( int rtmpHandle, byte[] bytes, int offset, int len ) throws IOException;

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
	 * Please do not use this method because its very inefficient.
	 * 
	 * @see java.io.InputStream#read()
	 */
	@Override
	public int read() throws IOException {
		final byte[] buf = new byte[ 1 ];

		int n;
		while( (n = this.read( buf, 0, 1 )) == 0 ) {
		}

		if( n == 1 ) {
			return (buf[ 0 ]) & 0xff;
		}
		return n;
	}

	/**
	 * 
	 */
	@Override
	public int read( final byte[] b, final int off, final int len ) throws IOException {
		return this.nativeReadStream( this.rtmpHandle, b, off, len );
	}
}
