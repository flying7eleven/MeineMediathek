package com.halcyonwaves.apps.meinemediathek.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.halcyonwaves.apps.meinemediathek.R;

public class ManageDownloadActivity extends Activity {

	@Override
	protected void onCreate( final Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		// set the layout of the activity (which is use as a dialog)
		this.setContentView( R.layout.activity_managedownload );

		//
		final Bundle intentExtras = this.getIntent().getExtras();

		//
		final TextView movieTitle = (TextView) this.findViewById( R.id.tv_movie_title_content );
		movieTitle.setText( intentExtras.getString( "movieTitle" ) );
	}

}
