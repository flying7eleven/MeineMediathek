package com.halcyonwaves.apps.meinemediathek.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.halcyonwaves.apps.meinemediathek.Consts;
import com.halcyonwaves.apps.meinemediathek.R;

public class ManageDownloadActivity extends BaseActivity {

	private Button cancelDownload = null;
	private TextView movieTitle = null;
	private TextView movieDescription = null;

	@Override
	protected void onCreate( final Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		// set the layout of the activity (which is use as a dialog)
		this.setContentView( R.layout.activity_managedownload );

		//
		final Bundle intentExtras = this.getIntent().getExtras();

		//
		this.movieTitle = (TextView) this.findViewById( R.id.tv_movie_title_content );
		this.movieTitle.setText( intentExtras.getString( Consts.EXTRA_NAME_MOVIE_TITLE ) );
		
		//
		this.movieDescription = (TextView) this.findViewById( R.id.tv_movie_description_content );
		this.movieDescription.setText( intentExtras.getString( Consts.EXTRA_NAME_MOVIE_DESCRIPTION ) );

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
