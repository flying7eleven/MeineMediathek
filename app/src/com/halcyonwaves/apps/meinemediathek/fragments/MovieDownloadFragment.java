package com.halcyonwaves.apps.meinemediathek.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.halcyonwaves.apps.meinemediathek.R;
import com.halcyonwaves.apps.meinemediathek.worker.DownloadStream;

public class MovieDownloadFragment extends Fragment {

	private Button btn = null;
	private ProgressBar pb = null;
	private TextView tv = null;

	@Override
	public View onCreateView( final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState ) {
		final View v = inflater.inflate( R.layout.fragment_moviedownload, container );
		this.btn = (Button) v.findViewById( R.id.btn_download );
		this.pb = (ProgressBar) v.findViewById( R.id.pb_download_progress_pct );
		this.tv = (TextView) v.findViewById( R.id.tv_download_progress_bytes );

		this.btn.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick( final View v ) {

				final DownloadStream dst = new DownloadStream( MovieDownloadFragment.this.pb, MovieDownloadFragment.this.tv );
				dst.execute( "mms://a1014.v1252931.c125293.g.vm.akamaistream.net/7/1014/125293/v0001/wm.od.origin.zdf.de.gl-systemhaus.de/none/zdf/12/08/120827_trailer_staffel13_kio_sok4_vh.wmv" );

			}
		} );

		return v;
	}
}
