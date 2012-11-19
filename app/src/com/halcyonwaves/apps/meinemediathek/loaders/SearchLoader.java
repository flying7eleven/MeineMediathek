package com.halcyonwaves.apps.meinemediathek.loaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public class SearchLoader extends AsyncTaskLoader< List< String > > {

	private List< String > searchResults = null;
	private String searchFor = null;
	private static final String TAG = "SearchLoader";

	private final static String BASE_SEARCH_URL = "http://www.zdf.de/ZDFmediathek/suche?flash=off&sucheText=";

	private final static Pattern VIDEO_TITLE_REGEX = Pattern.compile( ".*\\=\\\"beitragHeadline\\\".*\\>(.*)\\<.*" );

	public SearchLoader( Context context, String searchFor ) {
		super( context );
		this.searchFor = searchFor;
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
		// just write the search keyword into the logfile
		Log.v( SearchLoader.TAG, "Starting to load data for the following search query: " + this.searchFor );

		// be sure that the search keyword is well-formed
		final String preparedSearchKeyword = TextUtils.htmlEncode( this.searchFor ); // TODO: i think its not working as expected

		// create the list we want to return
		List< String > foundTitles = new ArrayList< String >();

		// try to download the response of the webpage to the search query
		try {
			// query for the results and get a handle to the returned HTML code
			Document fetchedResults = Jsoup.connect( SearchLoader.BASE_SEARCH_URL + preparedSearchKeyword ).get();
			Elements foundLinks = fetchedResults.select( "a[href]" );
			for( Element currentLink : foundLinks ) {
				if( currentLink.attr( "abs:href" ).contains( "/ZDFmediathek/beitrag/video" ) ) {
					foundTitles.add( currentLink.text() );
				}
			}

		} catch( IOException e ) {
			Log.e( SearchLoader.TAG, "Failed to fetch the search results from the website.", e );
		}

		// return the list of found items
		return foundTitles;
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