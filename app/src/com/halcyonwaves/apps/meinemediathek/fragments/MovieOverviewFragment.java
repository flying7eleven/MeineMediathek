package com.halcyonwaves.apps.meinemediathek.fragments;

import com.halcyonwaves.apps.meinemediathek.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MovieOverviewFragment extends Fragment {

	@Override
	public View onCreateView( final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState ) {
		final View v = inflater.inflate( R.layout.fragment_movieoverview, container );

		return v;
	}
}
