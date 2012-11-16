/*
 * * AACPlayer - Freeware Advanced Audio (AAC) Player for Android* Copyright (C) 2011 Spolecne s.r.o., http://www.spoledge.com** This program is free software; you can redistribute it and/or modify* it under the terms of the GNU General Public License as published by* the Free Software Foundation; either version 3 of the License, or* (at your option) any later version.** This program is distributed in the hope that it will be useful,* but WITHOUT ANY WARRANTY; without even the implied warranty of* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the* GNU General Public License for more details.** You should have received a copy of the GNU General Public License* along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.halcyonwaves.apps.meinemediathek.ndk;

import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

/**
 * This is a MMS input stream - reads data from a MMS stream.
 * <pre>
 *  MMSInputStream mmsis = new MMSInputStream( "mms://..." );
 *  ...
 *  mmsis.read( buffer );
 *  ...
 *  mmsis.close();
 * </pre>
 */
public class MMSInputStream extends InputStream {

	private static final String CHARSET_NAME_UNICODE = "UTF-16LE";

	private static boolean libLoaded = false;

	// //////////////////////////////////////////////////////////////////////////
	// Attributes
	// //////////////////////////////////////////////////////////////////////////

	private static final String LOG = "MMSInputStream";

	/**
	 * This is a conversion method used by libmms.
	 */
	private static synchronized void ensureLibLoaded() {
		if( !MMSInputStream.libLoaded ) {
			System.loadLibrary( "mms" );
			MMSInputStream.libLoaded = true;
		}
	}

	// //////////////////////////////////////////////////////////////////////////
	// Constructors
	// //////////////////////////////////////////////////////////////////////////

	/**
	 * This is a conversion method used by libmms.
	 */
	private static byte[] string2unicode( final String s ) {
		Log.d( MMSInputStream.LOG, "string2unicode(): '" + s + "'" );

		// return s.getBytes( CHARSET_UNICODE );

		try {
			return s.getBytes( MMSInputStream.CHARSET_NAME_UNICODE );
		} catch( final java.io.UnsupportedEncodingException e ) {
			Log.e( MMSInputStream.LOG, "Cannot convert string --> unicode", e );
			throw new RuntimeException( e );
		}
	}

	// //////////////////////////////////////////////////////////////////////////
	// Public - InputStream
	// //////////////////////////////////////////////////////////////////////////

	/**
	 * This is a conversion method used by libmms.
	 */
	private static String unicode2string( final byte[] bytes ) {
		try {
			final String ret = new String( bytes, MMSInputStream.CHARSET_NAME_UNICODE );
			Log.d( MMSInputStream.LOG, "unicode2string(): '" + ret + "'" );

			return ret;
		} catch( final java.io.UnsupportedEncodingException e ) {
			Log.e( MMSInputStream.LOG, "Cannot convert unicode --> string", e );
			throw new RuntimeException( e );
		}
	}

	/**
	 * 
	 */
	private final int len;

	/**
	 * The native handler.
	 */
	private final int mms;

	public MMSInputStream( final String url ) throws IOException {
		MMSInputStream.ensureLibLoaded();

		this.mms = this.nativeConnect( url );
		this.len = this.nativeGetLength( this.mms );
	}

	// //////////////////////////////////////////////////////////////////////////
	// Private
	// //////////////////////////////////////////////////////////////////////////

	@Override
	public void close() throws IOException {
		this.nativeClose( this.mms );
	}

	public int length() {
		return this.len;
	}

	private native void nativeClose( int mms ) throws IOException;

	private native int nativeConnect( String url ) throws IOException;

	private native int nativeGetLength( int mms ) throws IOException;

	private native int nativeRead( int mms, byte[] b, int off, int len ) throws IOException;

	/**
	 * Please do not use this method - this is not efficient !
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

	@Override
	public int read( final byte[] b, final int off, final int len ) throws IOException {
		return this.nativeRead( this.mms, b, off, len );
	}

}
