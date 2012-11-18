package com.halcyonwaves.apps.meinemediathek.fragments;

import java.io.File;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.halcyonwaves.apps.meinemediathek.R;
import com.halcyonwaves.apps.meinemediathek.worker.DownloadStreamTask;

public class MovieDownloadFragment extends Fragment {

	private ProgressBar pb = null;
	private TextView tv = null;

	@Override
	public View onCreateView( final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState ) {
		final View v = inflater.inflate( R.layout.fragment_moviedownload, container );
		this.pb = (ProgressBar) v.findViewById( R.id.pb_download_progress_pct );
		this.tv = (TextView) v.findViewById( R.id.tv_download_progress_bytes );

		return v;
	}

	@Override
	public void onViewCreated( View view, Bundle savedInstanceState ) {
		//
		super.onViewCreated( view, savedInstanceState );

		//
		File storagePath = this.getActivity().getExternalFilesDir( Environment.DIRECTORY_MOVIES );
		File movieFile = new File( storagePath, "test.wmv" );

		//
		final DownloadStreamTask dst = new DownloadStreamTask( MovieDownloadFragment.this.pb, MovieDownloadFragment.this.tv, movieFile );
		dst.execute( "mms://a1014.v1252931.c125293.g.vm.akamaistream.net/7/1014/125293/v0001/wm.od.origin.zdf.de.gl-systemhaus.de/none/zdf/12/08/120827_trailer_staffel13_kio_sok4_vh.wmv" );

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		this.getActivity().sendBroadcast( new Intent( Intent.ACTION_MEDIA_MOUNTED, Uri.parse( "file://" + this.getActivity().getExternalFilesDir( Environment.DIRECTORY_MOVIES ) ) ) );
	}
}
