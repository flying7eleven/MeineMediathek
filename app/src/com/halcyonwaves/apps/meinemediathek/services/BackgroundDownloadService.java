package com.halcyonwaves.apps.meinemediathek.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.halcyonwaves.apps.meinemediathek.threads.DownloadThreadExecutorThread;

/**
 * This class describes a service which is responsible for all movie downloads of the
 * application. If an Activity wants to initiate a download, it has to connect to this
 * service and send a request message with the required information.
 * 
 * @author Tim Huetz
 */
public class BackgroundDownloadService extends Service {

	/**
	 * This tag is used for all logging messages of this service.
	 */
	private static final String TAG = "BackgroundDownloadService";

	/**
	 * This message is used to request to start a new download.
	 */
	public static final int SERVICE_MSG_INITIATE_DOWNLOAD = 1;

	/**
	 * This message is used to request to cancel a running download.
	 */
	public static final int SERVICE_MSG_CANCEL_DOWNLOAD = 2;

	/**
	 * This is the messager which is used to communicate with this service.
	 */
	private final Messenger serviceMessenger = new Messenger( new IncomingHandler() );

	/**
	 * 
	 * @author Tim Huetz
	 */
	private class IncomingHandler extends Handler {

		@Override
		public void handleMessage( final Message msg ) {
			switch( msg.what ) {
				case BackgroundDownloadService.SERVICE_MSG_INITIATE_DOWNLOAD:
					// get the additional data send with the download request
					final Bundle suppliedExtras = msg.getData();
					
					// get some required information to starting the download
					final String episodeTitle = suppliedExtras.getString( "movieTitle" );
					final String downlaodURL = suppliedExtras.getString( "downloadLink" );
					
					// start the download
					Log.d( BackgroundDownloadService.TAG, "Service started with request to download following URL: " + downlaodURL );
					new DownloadThreadExecutorThread( BackgroundDownloadService.this.getApplicationContext(), downlaodURL, episodeTitle ).start();

					// TODO: rest
					break;
				case BackgroundDownloadService.SERVICE_MSG_CANCEL_DOWNLOAD:
					break; // TODO: this
				default:
					super.handleMessage( msg );
			}
		}
	}

	/**
	 * 
	 */
	@Override
	public IBinder onBind( final Intent intent ) {
		return this.serviceMessenger.getBinder();
	}

	/**
	 * 
	 */
	@Override
	public int onStartCommand( final Intent intent, final int flags, final int startid ) {
		return Service.START_STICKY;
	}

}
