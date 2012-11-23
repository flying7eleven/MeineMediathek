package com.halcyonwaves.apps.meinemediathek.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.halcyonwaves.apps.meinemediathek.R;
import com.halcyonwaves.apps.meinemediathek.services.BackgroundDownloadService;

public class MovieOverviewFragment extends Fragment {

	private Button btnDownloadMoview = null;
	private String downloadLink = "";
	private ImageView ivPreviewImage = null;
	private TextView tvMovieDescription = null;
	private TextView tvMovieTitle = null;

	@Override
	public View onCreateView( final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState ) {
		final View v = inflater.inflate( R.layout.fragment_movieoverview, container );

		// get the information passed to the fragment
		final Bundle passedInformation = this.getActivity().getIntent().getExtras();

		// get the handles to the controls we need to access
		this.tvMovieTitle = (TextView) v.findViewById( R.id.tv_movie_title_content );
		this.tvMovieDescription = (TextView) v.findViewById( R.id.tv_movie_description_content );
		this.ivPreviewImage = (ImageView) v.findViewById( R.id.iv_movie_preview_image );
		this.btnDownloadMoview = (Button) v.findViewById( R.id.btn_download_movie );

		// set the download link we want to use
		this.downloadLink = passedInformation.getString( "downloadLink" );

		// set the content for all of the fetched controls
		this.tvMovieTitle.setText( passedInformation.getString( "title" ) );
		this.tvMovieDescription.setText( passedInformation.getString( "description" ) );
		this.ivPreviewImage.setImageBitmap( BitmapFactory.decodeFile( passedInformation.getString( "previewImage" ) ) );

		// tell the button what to do as soon as it gets clicked
		this.btnDownloadMoview.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick( final View v ) {
				final ConnectivityManager cm = (ConnectivityManager) MovieOverviewFragment.this.getActivity().getSystemService( Context.CONNECTIVITY_SERVICE );
				final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
				if( activeNetwork.getType() != ConnectivityManager.TYPE_WIFI ) { // TODO: we have to deal with WiMAX too
					// prepare a dialog asking the user he or she really wants to do the download on a mobile connection
					final AlertDialog.Builder builder = new AlertDialog.Builder( MovieOverviewFragment.this.getActivity() );
					builder.setMessage( R.string.dialog_msg_download_on_mobile ).setTitle( R.string.dialog_title_download_on_mobile ).setPositiveButton( android.R.string.yes, new DialogInterface.OnClickListener() {

						@Override
						public void onClick( final DialogInterface dialog, final int id ) {
							// the user decided to start the download even on the mobile network, so do it
							MovieOverviewFragment.this.startEpisodeDownload();
						}
					} ).setNegativeButton( android.R.string.no, new DialogInterface.OnClickListener() {

						@Override
						public void onClick( final DialogInterface dialog, final int id ) {
							// the user said no, so do nothing here
						}
					} );

					// show the dialog to the user
					final AlertDialog askUserDialog = builder.create();
					askUserDialog.show();
				} else {
					// as the user is using Wifi, we can start the download without asking again
					MovieOverviewFragment.this.startEpisodeDownload();
				}

			}
		} );

		// return the created view
		return v;
	}

	private void startEpisodeDownload() {
		final Intent serviceIntent = new Intent( this.getActivity(), BackgroundDownloadService.class );
		serviceIntent.putExtra( "downloadLink", this.downloadLink );
		serviceIntent.putExtra( "movieTitle", this.tvMovieTitle.getText().toString() );
		this.getActivity().startService( serviceIntent );
	}
}
