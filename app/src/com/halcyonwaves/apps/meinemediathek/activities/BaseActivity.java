package com.halcyonwaves.apps.meinemediathek.activities;

import com.halcyonwaves.apps.meinemediathek.R;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;

public abstract class BaseActivity extends Activity {

	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		MenuInflater usedInflater = this.getMenuInflater();
		usedInflater.inflate( R.menu.main_menu, menu );
		return true;
	}

}
