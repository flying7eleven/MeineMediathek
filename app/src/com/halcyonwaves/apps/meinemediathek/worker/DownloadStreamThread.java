package com.halcyonwaves.apps.meinemediathek.worker;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

public class DownloadStreamThread extends Thread {

	private String downloadLink = null;
	private String movieTitle = null;
	
	private static final int NOTIFICATION_ID = 1;
	private static final String TAG = "DownloadStreamThread";

	private NotificationManager notificationManager = null;
	private NotificationCompat.Builder notificationBuilder = null;

	public DownloadStreamThread( Context context, String downloadLink, String movieTitle ) {
		this.downloadLink = downloadLink;
		this.movieTitle = movieTitle;
		
		// prepare the notification for the download
		this.notificationManager = (NotificationManager) context.getSystemService( Context.NOTIFICATION_SERVICE );
		this.notificationBuilder = new NotificationCompat.Builder( context );
		this.notificationBuilder.setContentTitle( "Download of " + this.movieTitle ).setContentText( "Download in progress" ).setSmallIcon( android.R.drawable.ic_menu_directions ); // TODO: translations

	}
	
	@Override
	public void run() {
		// since we currently don't know how big the file is, show a progress with an undefined state
		this.notificationBuilder.setProgress( 100, 0, true );
		this.notificationManager.notify( this.downloadLink, DownloadStreamThread.NOTIFICATION_ID, this.notificationBuilder.build() );

		// TODO

		// we finished download the episode, change the notification again
		this.notificationBuilder.setContentText( "Download complete" ).setProgress( 0, 0, false );
		this.notificationManager.notify( this.downloadLink, DownloadStreamThread.NOTIFICATION_ID, this.notificationBuilder.build() );
	}

}
