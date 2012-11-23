package com.halcyonwaves.apps.meinemediathek.activities;

import com.halcyonwaves.apps.meinemediathek.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ManageDownloadActivity extends Activity {

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		
		// set the layout of the activity (which is use as a dialog)
		this.setContentView( R.layout.activity_managedownload );
		
		//
		Bundle intentExtras = this.getIntent().getExtras();
		
		//
		TextView movieTitle = (TextView)this.findViewById( R.id.tv_movie_title_content );
		movieTitle.setText( intentExtras.getString( "movieTitle" ) );
	}

}
