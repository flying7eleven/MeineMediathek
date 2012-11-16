package com.halcyonwaves.apps.meinemediathek.fragments;

import java.io.IOException;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.halcyonwaves.apps.meinemediathek.R;
import com.halcyonwaves.apps.meinemediathek.ndk.MMSInputStream;

public class MovieSearchFragment extends Fragment {

	private Button btn = null;

	@Override
	public View onCreateView( final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState ) {
		View v = inflater.inflate( R.layout.fragment_moviesearch, container );
		this.btn = (Button) v.findViewById( R.id.button1 );
		this.btn.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick( View v ) {
				try {
					MMSInputStream st = new MMSInputStream( "mms://a1014.v1252931.c125293.g.vm.akamaistream.net/7/1014/125293/v0001/wm.od.origin.zdf.de.gl-systemhaus.de/none/zdf/12/10/121019_brand_lied_mau_1592k_p24v10.wmv" );
				} catch( IOException e ) {
					Log.e( "SearchMoveFragment", e.getMessage() );
				}

			}
		} );

		return v;
	}
}
