package com.halcyonwaves.apps.meinemediathek.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.halcyonwaves.apps.meinemediathek.R;

public class ManageDownloadActivity extends BaseActivity {

	private Button cancelDownload = null;
	private TextView movieTitle = null;

	@Override
	protected void onCreate( final Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		// set the layout of the activity (which is use as a dialog)
		this.setContentView( R.layout.activity_managedownload );

		//
		final Bundle intentExtras = this.getIntent().getExtras();

		//
		this.movieTitle = (TextView) this.findViewById( R.id.tv_movie_title_content );
		this.movieTitle.setText( intentExtras.getString( "movieTitle" ) );

		//
		this.cancelDownload = (Button) this.findViewById( R.id.btn_cancel_download );
		this.cancelDownload.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick( final View v ) {
				// TODO: cancel the download
				ManageDownloadActivity.this.finish();

			}
		} );
	}

}
