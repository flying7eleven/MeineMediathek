package com.halcyonwaves.apps.meinemediathek.fragments;

import com.halcyonwaves.apps.meinemediathek.R;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		this.addPreferencesFromResource( R.xml.app_prefs );
	}
}
