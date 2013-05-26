package com.halcyonwaves.apps.meinemediathek.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.halcyonwaves.apps.meinemediathek.Consts;
import com.halcyonwaves.apps.meinemediathek.R;
import com.halcyonwaves.apps.meinemediathek.services.BackgroundDownloadService;

public class ManageDownloadActivity extends BaseActivity {

	private static final String TAG = "ManageDownloadActivity";
	private Button cancelDownload = null;
	private int cancelId = -1;
	private TextView movieDescription = null;

	private TextView movieTitle = null;

	private final ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected( final ComponentName className, final IBinder service ) {
			ManageDownloadActivity.this.serviceMessanger = new Messenger( service );
			Log.d( ManageDownloadActivity.TAG, "Conntected to download service" );
		}

		@Override
		public void onServiceDisconnected( final ComponentName className ) {
			ManageDownloadActivity.this.serviceMessanger = null;
			Log.d( ManageDownloadActivity.TAG, "Disconnected from download service" );
		}
	};
	private Messenger serviceMessanger = null;

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
	protected void onCreate( final Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		// bind to the download service
		this.doBindService();

		// set the layout of the activity (which is use as a dialog)
		this.setContentView( R.layout.activity_managedownload );

		//
		final Bundle intentExtras = this.getIntent().getExtras();
		this.cancelId = intentExtras.getInt( Consts.EXTRA_NAME_MOVIE_UNIQUE_ID );

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
				// prepare the information we want to send to the service
				final Bundle downloadExtras = new Bundle();
				downloadExtras.putInt( Consts.EXTRA_NAME_MOVIE_UNIQUE_ID, ManageDownloadActivity.this.cancelId );

				// prepare the download request
				final Message downloadRequest = new Message();
				downloadRequest.setData( downloadExtras );
				downloadRequest.what = BackgroundDownloadService.SERVICE_MSG_CANCEL_DOWNLOAD;
				downloadRequest.replyTo = ManageDownloadActivity.this.serviceMessanger;

				// send the download request
				try {
					ManageDownloadActivity.this.serviceMessanger.send( downloadRequest );
				} catch( final RemoteException e ) {
					// TODO: ACRA.getErrorReporter().handleException( e );
				}

				// close the dialog because we send the cancel request to the service
				ManageDownloadActivity.this.finish();

			}
		} );
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.doUnbindService();
	}

}
