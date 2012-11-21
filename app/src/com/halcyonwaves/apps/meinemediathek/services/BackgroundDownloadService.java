package com.halcyonwaves.apps.meinemediathek.services;

import java.io.File;

import com.halcyonwaves.apps.meinemediathek.worker.DownloadStreamThread;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

public class BackgroundDownloadService extends Service {

	private static final String TAG = "BackgroundDownloadService";

	@Override
	public IBinder onBind( Intent intent ) {
		return null;
	}

	@Override
	public int onStartCommand( Intent intent, int flags, int startid ) {
		// get some required information to starting the download
		String episodeTitle = intent.getExtras().getString( "movieTitle" );
		String downlaodURL = intent.getExtras().getString( "downloadLink" );

		// just log the event that we received a download request
		Log.d( BackgroundDownloadService.TAG, "Service started with request to download following URL: " + downlaodURL );

		// we have to check if the download was already started and skip the rest if it was
		// TODO: this

		// do the actual download in a separate thread
		new DownloadStreamThread( this.getApplicationContext(), downlaodURL, episodeTitle ).start();

		//
		return START_STICKY;
	}

}
