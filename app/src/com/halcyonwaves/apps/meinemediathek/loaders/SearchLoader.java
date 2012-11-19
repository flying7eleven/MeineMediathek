package com.halcyonwaves.apps.meinemediathek.loaders;

import java.util.ArrayList;
import java.util.List;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

public class SearchLoader extends AsyncTaskLoader< List< String > > {

	private List< String > searchResults = null;
	private static final String TAG = "SearchLoader";

	public SearchLoader( Context context ) {
		super( context );
	}

	@Override
	protected void onStartLoading() {
		// bugfix for this issue:
		// http://code.google.com/p/android/issues/detail?id=14944
		// http://blog.blundell-apps.com/tut-asynctask-loader-using-support-library/
		super.onStartLoading();
		if( null != this.searchResults ) {
			this.deliverResult( this.searchResults );
		} else {
			this.forceLoad();
		}
	}

	@Override
	public List< String > loadInBackground() {
		List< String > returnList = new ArrayList< String >();
		returnList.add( "First" );
		returnList.add( "Second" );
		returnList.add( "Third" );
		Log.v( SearchLoader.TAG, "Loading the data in background was finished." );
		return returnList;
	}

	@Override
	public void deliverResult( List< String > results ) {
		// a async query came in while the loader is stopped. We don't need the result.
		if( this.isReset() ) {

			if( results != null ) {
				this.onReleaseResources( results );
			}
		}
		List< String > oldResults = results;
		this.searchResults = results;

		// if the Loader is currently started, we can immediately deliver its results.
		if( this.isStarted() ) {
			super.deliverResult( results );
			Log.v( SearchLoader.TAG, "The search results were delivered to the client!" );
		}

		// at this point we can release the resources associated with old object
		if( oldResults != null ) {
			this.onReleaseResources( oldResults );
		}
	}

	protected void onReleaseResources( List< String > apps ) {
		// for a simple List<> there is nothing to do. For something
	}

}
