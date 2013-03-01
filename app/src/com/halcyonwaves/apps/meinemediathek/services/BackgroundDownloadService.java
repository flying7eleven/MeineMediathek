package com.halcyonwaves.apps.meinemediathek.services;

import java.util.HashMap;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.halcyonwaves.apps.meinemediathek.Consts;
import com.halcyonwaves.apps.meinemediathek.threads.DownloadStreamThread;

/**
 * This class describes a service which is responsible for all movie downloads of the
 * application. If an Activity wants to initiate a download, it has to connect to this
 * service and send a request message with the required information.
 * 
 * @author Tim Huetz
 */
public class BackgroundDownloadService extends Service {

	/**
	 * 
	 * @author Tim Huetz
	 */
	private class IncomingHandler extends Handler {

		@Override
		public void handleMessage( final Message msg ) {
			switch( msg.what ) {
				case BackgroundDownloadService.SERVICE_MSG_INITIATE_DOWNLOAD:
					// first of all do some cleanup operations
					BackgroundDownloadService.this.cleanupFinishedThreads();

					// get the additional data send with the download request
					final Bundle suppliedExtras = msg.getData();

					// get some required information to starting the download
					final String episodeTitle = suppliedExtras.getString( Consts.EXTRA_NAME_MOVIE_TITLE );
					final String downlaodURL = suppliedExtras.getString( Consts.EXTRA_NAME_MOVIE_DOWNLOADLINK );
					final int uniqueId = suppliedExtras.getInt( Consts.EXTRA_NAME_MOVIE_UNIQUE_ID );
					suppliedExtras.getString( Consts.EXTRA_NAME_MOVIE_PRVIEWIMAGEPATH );
					final String movieDescription = suppliedExtras.getString( Consts.EXTRA_NAME_MOVIE_DESCRIPTION );

					// start the download
					final Thread downloadThread = new DownloadStreamThread( BackgroundDownloadService.this.getApplicationContext(), uniqueId, downlaodURL, episodeTitle, movieDescription );
					BackgroundDownloadService.this.managedThreads.put( uniqueId, downloadThread );
					downloadThread.start();
					Log.d( BackgroundDownloadService.TAG, "The background downloader servies started a thread trying to download the following URL: " + downlaodURL );
					break;
				case BackgroundDownloadService.SERVICE_MSG_CANCEL_DOWNLOAD:
					// first of all do some cleanup operations
					BackgroundDownloadService.this.cleanupFinishedThreads();

					// get the additional data send with the cancel request
					final Bundle suppliedCancelExtras = msg.getData();

					// just log that we want to cancel now
					final int requestedCancelId = suppliedCancelExtras.getInt( Consts.EXTRA_NAME_MOVIE_UNIQUE_ID );
					Log.v( BackgroundDownloadService.TAG, String.format( "The user requested to cancel the download with the unique id %d.", requestedCancelId ) );

					// if we still have a thread with this id, interrupt it
					if( BackgroundDownloadService.this.managedThreads.containsKey( requestedCancelId ) ) {
						Log.v( BackgroundDownloadService.TAG, String.format( "Found the thread for the requested interruption in the internal map." ) );
						final Thread threadToCancel = BackgroundDownloadService.this.managedThreads.remove( requestedCancelId );
						if( null != threadToCancel ) {
							Log.v( BackgroundDownloadService.TAG, String.format( "The found thread seems to be a valid object." ) );
							if( threadToCancel.isAlive() ) {
								threadToCancel.interrupt();
								Log.v( BackgroundDownloadService.TAG, String.format( "Send interruption request to the download thread with the supplied id." ) );
							}
						}
					} else {
						Log.w( BackgroundDownloadService.TAG, String.format( "Cannot find the requested thread in the internal list. Canceling the cancel request (list length: %d)!", BackgroundDownloadService.this.managedThreads.keySet().size() ) );
					}
					break;
				default:
					super.handleMessage( msg );
			}
		}
	}

	/**
	 * This message is used to request to cancel a running download.
	 */
	public static final int SERVICE_MSG_CANCEL_DOWNLOAD = 2;

	/**
	 * This message is used to request to start a new download.
	 */
	public static final int SERVICE_MSG_INITIATE_DOWNLOAD = 1;

	/**
	 * This tag is used for all logging messages of this service.
	 */
	private static final String TAG = "BackgroundDownloadService";

	/**
	 * This map is used to store all running threads which are managed my this service.
	 */
	private Map< Integer, Thread > managedThreads = null;

	/**
	 * This is the messager which is used to communicate with this service.
	 */
	private final Messenger serviceMessenger = new Messenger( new IncomingHandler() );

	private void cleanupFinishedThreads() {
		for( final int currentThreadId : this.managedThreads.keySet() ) {
			if( null != this.managedThreads.get( currentThreadId ) ) {
				if( !this.managedThreads.get( currentThreadId ).isAlive() ) {
					this.managedThreads.remove( currentThreadId );
					Log.v( BackgroundDownloadService.TAG, String.format( "Removed thread %d from internal list because it was not alive anymore.", currentThreadId ) );
				}
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
		Log.v( BackgroundDownloadService.TAG, "The background downloading services was started with the following id: " + startid );
		this.managedThreads = new HashMap< Integer, Thread >();
		return Service.START_STICKY;
	}

}
