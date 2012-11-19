package com.halcyonwaves.apps.meinemediathek.fragments;

import java.util.List;

import com.halcyonwaves.apps.meinemediathek.adapter.SearchResultsAdapter;
import com.halcyonwaves.apps.meinemediathek.loaders.SearchLoader;

import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;

public class SearchResultsFragment extends ListFragment implements LoaderCallbacks< List< String > > {

	private final static String TAG = "SearchResultsFragment";
	private String searchFor = "";
	private SearchResultsAdapter searchResultsAdapter = null;

	@Override
	public void onActivityCreated( Bundle savedInstanceState ) {
		super.onActivityCreated( savedInstanceState );

		// get the supplied information from the intent which started this fragment
		this.searchFor = this.getActivity().getIntent().getExtras().getString( "searchFor" );
		Log.v( SearchResultsFragment.TAG, "The user is searching for: " + this.searchFor );

		//
		this.searchResultsAdapter = new SearchResultsAdapter( this.getActivity() );
		this.setListAdapter( this.searchResultsAdapter );

		// start out with a progress indicator.
		this.setListShown( false );

		// prepare the loader. Either re-connect with an existing one, or start a new one.
		this.getLoaderManager().initLoader( 0, null, this );
	}

	@Override
	public Loader< List< String >> onCreateLoader( int id, Bundle args ) {
		return new SearchLoader( this.getActivity() ); // gets just called if there is no existing loader
	}

	@Override
	public void onLoadFinished( Loader< List< String >> loader, List< String > data ) {
		// set the new data in the adapter and show the list
		this.searchResultsAdapter.setData( data );
		if( this.isResumed() ) {
			this.setListShown( true );
		} else {
			this.setListShownNoAnimation( true );
		}
	}

	@Override
	public void onLoaderReset( Loader< List< String >> loader ) {
		this.searchResultsAdapter.setData( null ); // clear the data in the adapter.
	}
}
