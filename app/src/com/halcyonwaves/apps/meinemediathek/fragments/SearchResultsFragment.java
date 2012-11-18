package com.halcyonwaves.apps.meinemediathek.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.halcyonwaves.apps.meinemediathek.R;

public class SearchResultsFragment extends Fragment {

	@Override
	public View onCreateView( final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState ) {
		// get the basic view layout from the application resources
		final View v = inflater.inflate( R.layout.fragment_searchresults, container );

		// return the created view for the fragment
		return v;
	}

}
