package com.halcyonwaves.apps.meinemediathek.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.halcyonwaves.apps.meinemediathek.R;

public class MovieSearchFragment extends Fragment {

	@Override
	public View onCreateView( final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState ) {
		return inflater.inflate( R.layout.fragment_moviesearch, container );
	}
}
