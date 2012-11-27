package com.halcyonwaves.apps.meinemediathek.threads;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.acra.ACRA;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class DownloadThreadExecutorThread extends Thread {

	private final static int DOWNLOAD_TIMEOUT_IN_MINUTES = 120;
	private final static String TAG = "DownloadThreadExecutorThread";
	private String downloadLink = null;
	private String movieTitle = null;
	private Context threadContext = null;
	private WakeLock usedWakelock = null;

	public DownloadThreadExecutorThread( final Context context, final String downloadLink, final String movieTitle ) {
		this.downloadLink = downloadLink;
		this.movieTitle = movieTitle;
		this.threadContext = context;

		//
		final PowerManager pm = (PowerManager) context.getSystemService( Context.POWER_SERVICE );
		this.usedWakelock = pm.newWakeLock( PowerManager.PARTIAL_WAKE_LOCK, DownloadThreadExecutorThread.TAG );
	}

	@Override
	public void run() {
		// get a wake lock that we can download the complete file
		this.usedWakelock.acquire();

		// do the actual download in a separate thread
		try {
			final ExecutorService currentExecutor = Executors.newSingleThreadExecutor();
			currentExecutor.submit( new DownloadStreamThread( this.threadContext, this.downloadLink, this.movieTitle ) ).get( DownloadThreadExecutorThread.DOWNLOAD_TIMEOUT_IN_MINUTES, TimeUnit.MINUTES );
			currentExecutor.shutdown();
		} catch( final InterruptedException e ) {
			ACRA.getErrorReporter().putCustomData( "exceptionMessage", e.getMessage() );
			ACRA.getErrorReporter().putCustomData( "threadExecutionTimeoutInMinutes", String.format( "%d", DownloadThreadExecutorThread.DOWNLOAD_TIMEOUT_IN_MINUTES ) );
			ACRA.getErrorReporter().handleException( e );
		} catch( final TimeoutException e ) {
			ACRA.getErrorReporter().putCustomData( "exceptionMessage", e.getMessage() );
			ACRA.getErrorReporter().putCustomData( "threadExecutionTimeoutInMinutes", String.format( "%d", DownloadThreadExecutorThread.DOWNLOAD_TIMEOUT_IN_MINUTES ) );
			ACRA.getErrorReporter().handleException( e );
		} catch( final ExecutionException e ) {
			ACRA.getErrorReporter().putCustomData( "exceptionMessage", e.getMessage() );
			ACRA.getErrorReporter().putCustomData( "threadExecutionTimeoutInMinutes", String.format( "%d", DownloadThreadExecutorThread.DOWNLOAD_TIMEOUT_IN_MINUTES ) );
			ACRA.getErrorReporter().handleException( e );
		}

		// release the acquired wake lock again
		this.usedWakelock.release();
	}

}
