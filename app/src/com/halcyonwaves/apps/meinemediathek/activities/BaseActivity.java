package com.halcyonwaves.apps.meinemediathek.activities;

import java.io.FileOutputStream;
import java.io.IOException;

import org.acra.ACRA;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.halcyonwaves.apps.meinemediathek.ApplicationEntryPoint;
import com.halcyonwaves.apps.meinemediathek.R;
import com.halcyonwaves.apps.meinemediathek.ndk.RTMPInputStream;

public abstract class BaseActivity extends Activity {

	@Override
	public boolean onCreateOptionsMenu( final Menu menu ) {
		// inflate the basic menu
		final MenuInflater usedInflater = this.getMenuInflater();
		usedInflater.inflate( R.menu.main_menu, menu );

		// if this is a debug build, add an option to send a custom bug report
		if( ApplicationEntryPoint.isApplicationDebuggable( this.getApplicationContext() ) ) {
			final MenuItem bugreportMenu = menu.findItem( R.id.mnu_send_bugreport );
			final MenuItem testDownloadMenu = menu.findItem( R.id.mnu_test_download );
			if( null != testDownloadMenu ) {
				testDownloadMenu.setVisible( true );
			}
			if( null != bugreportMenu ) {
				bugreportMenu.setVisible( true );
			}
		}

		// we succeeded
		return true;
	}

	@Override
	public boolean onMenuItemSelected( final int featureId, final MenuItem item ) {
		switch( item.getItemId() ) {
			case R.id.mnu_send_bugreport:
				ACRA.getErrorReporter().handleException( null );
				break;
			case R.id.mnu_test_download:
				try {
					final String filename = getFilesDir() + "/test.mp4";
					FileOutputStream fostream = new FileOutputStream( filename );
					RTMPInputStream testStream = new RTMPInputStream( "rtmp://gffstream.fcod.llnwd.net/a792/e2/CMS2010/mdb/9/94998/shorttrackrobertseifertmiterstemdeutscheneinzelsieg2_830270.mp4" );
					int bread = 0;
					byte[] buffer = new byte[ 4096 ];
					while( 0 < (bread = testStream.read( buffer, 0, 4096 )) ) {
						fostream.write( buffer, 0, bread );
					}
					buffer = null;
					fostream.flush();
					fostream.close();
					testStream.close();
				} catch( final IOException e ) {
					// do nothing, just a test
				}
				break;
		}
		return true;
	}

}
