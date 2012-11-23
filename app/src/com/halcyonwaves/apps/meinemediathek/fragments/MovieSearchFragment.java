package com.halcyonwaves.apps.meinemediathek.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.halcyonwaves.apps.meinemediathek.R;
import com.halcyonwaves.apps.meinemediathek.activities.SearchResultsActivity;

public class MovieSearchFragment extends Fragment {

	private Button btnSearch = null;
	private EditText etTitleToSearchFor = null;

	private final static String TAG = "MovieSearchFragment";

	@Override
	public View onCreateView( final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState ) {
		// get the basic view layout from the application resources
		final View v = inflater.inflate( R.layout.fragment_moviesearch, container );

		// get the handles to the controls we have to access
		this.btnSearch = (Button) v.findViewById( R.id.btn_search );
		this.etTitleToSearchFor = (EditText) v.findViewById( R.id.et_searchfortitle );

		// set the behavior for the search button
		this.btnSearch.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick( final View v ) {
				final Intent intent = new Intent( MovieSearchFragment.this.getActivity(), SearchResultsActivity.class );
				intent.putExtra( "searchFor", MovieSearchFragment.this.etTitleToSearchFor.getText().toString() );
				MovieSearchFragment.this.startActivity( intent );
			}
		} );

		// if this is the first time the user uses this application , he or she has to agree that he or she
		// will not do anything harmful
		if( true ) {
			
			// prepare a dialog asking the user he or she really wants to do the download on a mobile connection
			final AlertDialog.Builder builder = new AlertDialog.Builder( MovieSearchFragment.this.getActivity() );
			builder.setMessage( R.string.dlg_msg_license ).setTitle( R.string.dlg_title_license ).setPositiveButton( R.string.btn_agree, new DialogInterface.OnClickListener() {

				@Override
				public void onClick( final DialogInterface dialog, final int id ) {
					// the user accepted the license, so store this in the application settings and proceed

				}
			} ).setNegativeButton( R.string.btn_disagree, new DialogInterface.OnClickListener() {

				@Override
				public void onClick( final DialogInterface dialog, final int id ) {
					
					// if the user disagreed, we have to show him the play store for uninstalling the application
					try {
						final Intent intent = new Intent( Intent.ACTION_VIEW );
						intent.setData( Uri.parse( "market://details?id=com.halcyonwaves.apps.meinemediathek" ) );
						MovieSearchFragment.this.startActivity( intent );
						MovieSearchFragment.this.getActivity().finish();
					} catch( final Exception e ) {
						Log.e( MovieSearchFragment.TAG, "Failed to open the Google Play store to rate the application!" );
					}
				}
			} ).setCancelable( false );

			// show the dialog to the user
			final AlertDialog askUserDialog = builder.create();
			askUserDialog.show();
		}

		// return the created view for the fragment
		return v;
	}

}
