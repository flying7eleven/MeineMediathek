package com.halcyonwaves.apps.meinemediathek.fragments;

import com.halcyonwaves.apps.meinemediathek.R;

import android.app.Fragment;
import android.app.LoaderManager;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.SearchView.OnQueryTextListener;

public class MovieSearchFragment extends Fragment implements OnQueryTextListener {

	private SimpleCursorAdapter cursorAdapter;
	private String currentFilter;

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
		return inflater.inflate( R.layout.fragment_moviesearch, container );
	}

	@Override
	public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
		MenuItem item = menu.add( "Search" );
		item.setIcon( android.R.drawable.ic_menu_search );
		item.setShowAsAction( MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW );
		SearchView sv = new SearchView( this.getActivity() );
		sv.setOnQueryTextListener( this );
		item.setActionView( sv );
	}

	@Override
	public boolean onQueryTextChange( String newText ) {
		String newFilter = !TextUtils.isEmpty( newText ) ? newText : null;
		if( this.currentFilter == null && newFilter == null ) {
			return true;
		}
		if( this.currentFilter != null && this.currentFilter.equals( newFilter ) ) {
			return true;
		}
		this.currentFilter = newFilter;
		//this.getLoaderManager().restartLoader( 0, null, this );
		return true;
	}

	@Override
	public boolean onQueryTextSubmit( String query ) {
		return true;
	}
}
