package com.halcyonwaves.apps.meinemediathek.threads;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;

import org.acra.ACRA;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.halcyonwaves.apps.meinemediathek.Consts;
import com.halcyonwaves.apps.meinemediathek.R;
import com.halcyonwaves.apps.meinemediathek.activities.ManageDownloadActivity;
import com.halcyonwaves.apps.meinemediathek.ndk.MMSInputStream;

/**
 * This class is used to process a download request of a movie. Its a generalized class
 * and can be used for downloading a movie from any supported content provider.â
 * 
 * @author Tim Huetz
 */
public class DownloadStreamThread extends Thread {

	/**
	 * The tag which is used to identify the class in logging calls.â
	 */
	private static final String TAG = "DownloadStreamThread";

	/**
	 * The actual download thread.
	 */
	private Thread actualDownloadThread = null;

	/**
	 * The link to the file which should be downloaded.
	 */
	private String downloadLink = null;

	/**
	 * The wake lock which is used to prevent the device from falling asleep during the downloading process.
	 */
	private WakeLock downloadWakeLock = null;

	/**
	 * 
	 */
	private MMSInputStream mmsInputStream = null;

	/**
	 * The notification builder which is used to construct the download notification.
	 */
	private NotificationCompat.Builder notificationBuilder = null;

	/**
	 * The notification id which is used for the current download process.
	 */
	private int notificationId = -1;

	/**
	 * A handle to the Android notification manager to show our notification.
	 */
	private NotificationManager notificationManager = null;

	/**
	 * The handle to the output file for the downloaded movie.
	 */
	private File outputFile = null;

	/**
	 * The context in which the thread was created.
	 */
	private Context threadContext = null;

	/**
	 * Constructor for the download thread.
	 * 
	 * @param context The context in which the thread was created.
	 * @param notificationId The id which should be used for the notification (must be unique).
	 * @param downloadLink The link to the file which should be downloaded.
	 * @param movieTitle The title of the movie to download (displayed in the notification).
	 * @param movieDescription The description of the movie (will be displayed in the cancel dialog).
	 */
	public DownloadStreamThread( final Context context, final int notificationId, final String downloadLink, final String movieTitle, final String movieDescription ) {
		this.downloadLink = downloadLink;
		this.threadContext = context;
		this.notificationId = notificationId;

		// just log the used notification id
		Log.v( DownloadStreamThread.TAG, "Using the following notification id for the next download: " + this.notificationId );

		// try to obtain a wake lock object
		final PowerManager pm = (PowerManager) context.getSystemService( Context.POWER_SERVICE );
		this.downloadWakeLock = pm.newWakeLock( PowerManager.PARTIAL_WAKE_LOCK, String.format( "%s(%s)", DownloadStreamThread.TAG, movieTitle ) );

		// be sure that the output directory we are trying to use exists
		this.outputFile = new File( Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_MOVIES ), "MeineMediathek" );
		this.outputFile.mkdirs();

		// define what should happen if the user clicks on the notification item
		final Intent intent = new Intent( this.threadContext, ManageDownloadActivity.class );
		intent.putExtra( Consts.EXTRA_NAME_MOVIE_TITLE, movieTitle );
		intent.putExtra( Consts.EXTRA_NAME_MOVIE_DESCRIPTION, movieDescription );
		intent.putExtra( Consts.EXTRA_NAME_MOVIE_UNIQUE_ID, this.notificationId );
		final PendingIntent contentIntent = PendingIntent.getActivity( this.threadContext, this.notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT );

		// prepare the notification for the download
		this.notificationManager = (NotificationManager) context.getSystemService( Context.NOTIFICATION_SERVICE );
		this.notificationBuilder = new NotificationCompat.Builder( context );
		this.notificationBuilder.setContentTitle( String.format( context.getString( R.string.not_title_download_of_movie ), movieTitle ) );
		this.notificationBuilder.setContentText( context.getString( R.string.not_desc_download_of_movie ) );
		this.notificationBuilder.setSmallIcon( android.R.drawable.stat_sys_download );
		this.notificationBuilder.setOngoing( true );
		this.notificationBuilder.setContentIntent( contentIntent );

	}

	/**
	 * 
	 */
	@Override
	public void run() {
		// give the download thread a name
		Thread.currentThread().setName( String.format( "StreamDownloadTask(%s)", this.notificationId ) );

		// create the thread which does the actual downloading
		this.actualDownloadThread = new Thread() {

			@Override
			public void run() {
				//
				String extractedURL = "";
				Document fetchedResults = null;
				byte[] downloadBuffer = null;
				boolean reachedDueToException = false;
				int movieFullLength = 0;
				int comReadB = 0;
				int readB = 0;

				try {

					// be sure that the device will not sleep while we try to download our movie
					DownloadStreamThread.this.downloadWakeLock.acquire();

					// since we currently don't know how big the file is, show a progress with an undefined state
					DownloadStreamThread.this.notificationBuilder.setProgress( 100, 0, true );
					DownloadStreamThread.this.notificationManager.notify( DownloadStreamThread.TAG, Consts.NOTIFICATION_DOWNLOADING_MOVIE + DownloadStreamThread.this.notificationId, DownloadStreamThread.this.notificationBuilder.build() );

					// try it two times to fetch the file (if the first time fails for a socket timeout)
					try {
						fetchedResults = Jsoup.connect( DownloadStreamThread.this.downloadLink ).ignoreContentType( true ).userAgent( Consts.DESKTOP_USER_AGENT ).timeout( Consts.SOCKET_TIMEOUT_IN_SECONDS * 1000 ).get();
					} catch( final SocketTimeoutException e ) {
						try {
							fetchedResults = Jsoup.connect( DownloadStreamThread.this.downloadLink ).ignoreContentType( true ).userAgent( Consts.DESKTOP_USER_AGENT ).timeout( Consts.SOCKET_TIMEOUT_IN_SECONDS * 1000 ).get();
						} catch( final SocketTimeoutException innerE ) {
							throw e;
						}

					}
					final Elements foundLinks = fetchedResults.select( "Ref[href]" );
					for( final Element currentLink : foundLinks ) {
						Log.v( DownloadStreamThread.TAG, "Found a media link inside the ASX file: " + currentLink.attr( "href" ) );
						if( currentLink.attr( "href" ).startsWith( "mms://" ) ) {
							extractedURL = currentLink.attr( "href" );
							final String[] splittedURL = extractedURL.split( "/" );
							if( splittedURL.length > 0 ) {
								if( splittedURL[ splittedURL.length - 1 ].endsWith( "wmv" ) ) {
									DownloadStreamThread.this.outputFile = new File( DownloadStreamThread.this.outputFile, splittedURL[ splittedURL.length - 1 ] );
								} else {
									DownloadStreamThread.this.outputFile = new File( DownloadStreamThread.this.outputFile, "test.wmv" );
								}
							} else {
								DownloadStreamThread.this.outputFile = new File( DownloadStreamThread.this.outputFile, "test.wmv" );
							}
							break;
						}
					}

					//
					DownloadStreamThread.this.mmsInputStream = new MMSInputStream( extractedURL );
					final FileOutputStream outputStream = new FileOutputStream( DownloadStreamThread.this.outputFile );
					movieFullLength = DownloadStreamThread.this.mmsInputStream.length();

					// since we know the length of the full movie now, we can set the progress bar to a known state
					DownloadStreamThread.this.notificationBuilder.setProgress( 100, 0, false );
					DownloadStreamThread.this.notificationManager.notify( DownloadStreamThread.TAG, Consts.NOTIFICATION_DOWNLOADING_MOVIE + DownloadStreamThread.this.notificationId, DownloadStreamThread.this.notificationBuilder.build() );

					// select the buffer which is the best for the estimated file size
					if( movieFullLength < (1024 * 1024 * 10) ) { // if the file is smaller than 10 MB,
						downloadBuffer = new byte[ 1024 * 128 ]; // use a 128k buffer
					} else if( movieFullLength < (1024 * 1024 * 50) ) { // if the file is smaller than 50 MB,
						downloadBuffer = new byte[ 1024 * 256 ]; // use a 256k buffer
					} else if( movieFullLength < (1024 * 1024 * 100) ) { // if the file is smaller than 100 MB,
						downloadBuffer = new byte[ 1024 * 512 ]; // use a 512 buffer
					} else { // if the file is bigger than 100MB
						downloadBuffer = new byte[ 1024 * 1024 * 1 ]; // use a 1MB buffer
					}
					Log.v( DownloadStreamThread.TAG, String.format( "Selected a download buffer size of %d bytes", downloadBuffer.length ) );

					// read the whole movie
					while( (comReadB < movieFullLength) && !this.isInterrupted() ) {
						// get a data chunk
						readB = DownloadStreamThread.this.mmsInputStream.read( downloadBuffer, 0, downloadBuffer.length );
						if( readB <= 0 ) {
							Log.v( DownloadStreamThread.TAG, "The last read request returned with 0 bytes, it seems that we finished!" );
							break;
						}

						// write the data chunk into the output file
						outputStream.write( downloadBuffer, 0, readB );
						comReadB += readB;

						// update the notification bar entry
						DownloadStreamThread.this.notificationBuilder.setProgress( movieFullLength, comReadB, false );
						DownloadStreamThread.this.notificationManager.notify( DownloadStreamThread.TAG, Consts.NOTIFICATION_DOWNLOADING_MOVIE + DownloadStreamThread.this.notificationId, DownloadStreamThread.this.notificationBuilder.build() );
					}

					// close all opened streams
					downloadBuffer = null;
					outputStream.flush();
					outputStream.close();
					DownloadStreamThread.this.mmsInputStream.close();
					DownloadStreamThread.this.mmsInputStream = null;

					// if the thread was interrupted, delete the downloaded stuff again
					if( this.isInterrupted() ) {
						if( DownloadStreamThread.this.outputFile.delete() ) {
							Log.v( DownloadStreamThread.TAG, "Deleted file which was created before the download thread was interrupted!" );
						} else {
							Log.w( DownloadStreamThread.TAG, "Failed to delete the file which was created before the download thread was interrupted!" );
						}
					}

					// otherwise check the file size and then ensure that the media scanner sees the file we have added
					else {
						// if the size of the downloaded file is not the same as the expected size, delete the file and notify the user
						// it can happen that the actual file size is bigger than the expected size (TODO: why?)
						if( /* movieFullLength > DownloadStreamThread.this.outputFile.length() || */DownloadStreamThread.this.outputFile.length() != comReadB ) {
							Log.w( DownloadStreamThread.TAG, String.format( "The downloaded file has a size of %d bytes and %d bytes were received, but we expected %d bytes. Deleting file again!", DownloadStreamThread.this.outputFile.length(), comReadB, movieFullLength ) );
							DownloadStreamThread.this.outputFile.delete();
							reachedDueToException = true;
						} else {
							// tell the media scanner about the new file
							MediaScannerConnection.scanFile( DownloadStreamThread.this.threadContext, new String[] { DownloadStreamThread.this.outputFile.getAbsolutePath() }, null, null );
						}
					}

				} catch( final IOException e ) {
					Log.e( DownloadStreamThread.TAG, "Failed to fetch the movie file from the MMS stream.", e );
					reachedDueToException = true;
					ACRA.getErrorReporter().putCustomData( "downloadLink", DownloadStreamThread.this.downloadLink );
					ACRA.getErrorReporter().putCustomData( "outputFileAbsolutePath", DownloadStreamThread.this.outputFile != null ? DownloadStreamThread.this.outputFile.getAbsolutePath() : "" );
					ACRA.getErrorReporter().putCustomData( "downloadBufferSize", String.format( "%d", downloadBuffer.length ) );
					ACRA.getErrorReporter().putCustomData( "extractedURL", extractedURL );
					ACRA.getErrorReporter().putCustomData( "movieFullLength", String.format( "%d", movieFullLength ) );
					ACRA.getErrorReporter().putCustomData( "readBytesFromMovie", String.format( "%d", comReadB ) );
					ACRA.getErrorReporter().putCustomData( "lastReadBytes", String.format( "%d", readB ) );
					ACRA.getErrorReporter().putCustomData( "exceptionMessage", e.getMessage() );
					ACRA.getErrorReporter().handleException( e );
				} finally {
					// be sure that we ALWAYS release the wake lock
					DownloadStreamThread.this.downloadWakeLock.release();

					// ensure that the notification item can be removed if we finished or crashed
					if( !reachedDueToException ) {
						if( !this.isInterrupted() ) {
							DownloadStreamThread.this.notificationBuilder.setContentText( DownloadStreamThread.this.threadContext.getString( R.string.not_desc_download_of_movie_finished ) ).setSmallIcon( android.R.drawable.stat_sys_download_done ).setOngoing( false ).setProgress( 0, 0, false );
						} else {
							DownloadStreamThread.this.notificationBuilder.setContentText( DownloadStreamThread.this.threadContext.getString( R.string.not_desc_download_of_movie_canceled ) ).setSmallIcon( android.R.drawable.stat_sys_download_done ).setOngoing( false ).setProgress( 0, 0, false );
						}
					} else {
						DownloadStreamThread.this.notificationBuilder.setContentText( DownloadStreamThread.this.threadContext.getString( R.string.not_desc_download_of_movie_failed ) ).setSmallIcon( android.R.drawable.stat_sys_download_done ).setOngoing( false ).setProgress( 0, 0, false );
					}
					DownloadStreamThread.this.notificationManager.notify( DownloadStreamThread.TAG, Consts.NOTIFICATION_DOWNLOADING_MOVIE + DownloadStreamThread.this.notificationId, DownloadStreamThread.this.notificationBuilder.build() );

				}

			}
		};

		// run the actual download thread
		this.actualDownloadThread.start();

		// wait some time and check if our thread was interrupted, if so, terminate the actual
		// downlaod thread and return
		while( !this.isInterrupted() && this.actualDownloadThread.isAlive() ) {
			// Log.v( DownloadStreamThread.TAG, "RUNNING LOOP!" );

			// wait for a while (until the download finished or we get to our max. waiting time)
			try {
				this.actualDownloadThread.join( 1000 );
			} catch( final InterruptedException e ) {
				this.actualDownloadThread.interrupt();
				/*
				 * try { Log.v( DownloadStreamThread.TAG, "Waiting for the download thread to terminate gracefully..." ); this.actualDownloadThread.join( 5000 ); Log.v( DownloadStreamThread.TAG, "Sending forceful termation request to the download thread..." ); this.mmsInputStream.close(); } catch( IOException innerEx ) { // this is allowed to happen because we try to close any blocking call Log.v( DownloadStreamThread.TAG, "Socket was forcefully closed!" ); } catch( InterruptedException innerEx ) { // this is allowed to happen because we try to close any blocking call Log.v( DownloadStreamThread.TAG, "Thread already terminated gracefully!" ); } catch( Exception innerEx ) { Log.w( DownloadStreamThread.TAG, "Thread already terminated gracefully!!!!!!!!!!!!!!" ); }
				 */
			}
		}
	}
}
