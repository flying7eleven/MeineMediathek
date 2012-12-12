package com.halcyonwaves.apps.meinemediathek.activities;

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
					RTMPInputStream testStream = new RTMPInputStream( "rtmp://vod.daserste.de/ardfs/videoportal/mediathek/Reportage+Dokumentation/c_280000/280346/format348285.mp4" );
					testStream.getDurationInMs();
					testStream.read();
					testStream.close();
				} catch( final IOException e ) {
					// do nothing, just a test
				}
				break;
		}
		return true;
	}

}
