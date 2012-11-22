package com.halcyonwaves.apps.meinemediathek.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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
			public void onClick( View v ) {
				Intent intent = new Intent( MovieSearchFragment.this.getActivity(), SearchResultsActivity.class );
				intent.putExtra( "searchFor", MovieSearchFragment.this.etTitleToSearchFor.getText().toString() );
				MovieSearchFragment.this.startActivity( intent );
			}
		} );

		// return the created view for the fragment
		return v;
	}

}
