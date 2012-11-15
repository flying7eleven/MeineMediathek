package com.halcyonwaves.apps.meinemediathek.fragments;

import com.halcyonwaves.apps.meinemediathek.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MovieSearchFragment extends Fragment {

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
		return inflater.inflate( R.layout.fragment_moviesearch, container );
	}
}
