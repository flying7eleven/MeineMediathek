package com.halcyonwaves.apps.meinemediathek.activities;

import com.halcyonwaves.apps.meinemediathek.fragments.SettingsFragment;

import android.app.Activity;
import android.os.Bundle;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate( final Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		this.getFragmentManager().beginTransaction().replace( android.R.id.content, new SettingsFragment() ).commit();
	}
}
