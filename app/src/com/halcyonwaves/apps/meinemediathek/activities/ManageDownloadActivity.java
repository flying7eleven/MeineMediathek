package com.halcyonwaves.apps.meinemediathek.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.halcyonwaves.apps.meinemediathek.Consts;
import com.halcyonwaves.apps.meinemediathek.R;
import com.halcyonwaves.apps.meinemediathek.services.BackgroundDownloadService;

public class ManageDownloadActivity extends BaseActivity {

	private Button cancelDownload = null;
	private TextView movieTitle = null;
	private TextView movieDescription = null;

	private static final String TAG = "ManageDownloadActivity";
	
	private Messenger serviceMessanger = null;
	private final ServiceConnection serviceConnection = new ServiceConnection() {

		public void onServiceConnected( final ComponentName className, final IBinder service ) {
			ManageDownloadActivity.this.serviceMessanger = new Messenger( service );
			Log.d( ManageDownloadActivity.TAG, "Conntected to download service" );
		}

		public void onServiceDisconnected( final ComponentName className ) {
			ManageDownloadActivity.this.serviceMessanger = null;
			Log.d( ManageDownloadActivity.TAG, "Disconnected from download service" );
		}
	};

	private void doBindService() {
		if( null == this.serviceMessanger ) {
			this.bindService( new Intent( this, BackgroundDownloadService.class ), this.serviceConnection, Context.BIND_AUTO_CREATE );
		}
	}

	private void doUnbindService() {
		if( null != this.serviceMessanger ) {
			this.unbindService( this.serviceConnection );
			this.serviceMessanger = null; // we have to do this because the onServiceDisconnected method gets just called if the service was killed
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.doUnbindService();
	}

	@Override
	protected void onCreate( final Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		
		// bind to the download service
		this.doBindService();

		// set the layout of the activity (which is use as a dialog)
		this.setContentView( R.layout.activity_managedownload );

		//
		final Bundle intentExtras = this.getIntent().getExtras();

		//
		this.movieTitle = (TextView) this.findViewById( R.id.tv_movie_title_content );
		this.movieTitle.setText( intentExtras.getString( Consts.EXTRA_NAME_MOVIE_TITLE ) );
		
		//
		this.movieDescription = (TextView) this.findViewById( R.id.tv_movie_description_content );
		this.movieDescription.setText( intentExtras.getString( Consts.EXTRA_NAME_MOVIE_DESCRIPTION ) );

		//
		this.cancelDownload = (Button) this.findViewById( R.id.btn_cancel_download );
		this.cancelDownload.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick( final View v ) {
				// TODO: cancel the download
				ManageDownloadActivity.this.finish();

			}
		} );
	}

}
