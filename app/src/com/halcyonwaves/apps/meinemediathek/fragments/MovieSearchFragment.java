package com.halcyonwaves.apps.meinemediathek.fragments;

import java.io.File;
import java.io.FileOutputStream;
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

	private File file = null;
	private FileOutputStream out = null;
	
    private void createOutput() throws IOException {
        file = File.createTempFile( "streamer-downloading", ".wmv");
        out = new FileOutputStream( file );   
    }
	
	@Override
	public View onCreateView( final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState ) {
		View v = inflater.inflate( R.layout.fragment_moviesearch, container );
		this.btn = (Button) v.findViewById( R.id.button1 );
		this.btn.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick( View v ) {
				try {
					MMSInputStream st = new MMSInputStream( "mms://a1014.v1252931.c125293.g.vm.akamaistream.net/7/1014/125293/v0001/wm.od.origin.zdf.de.gl-systemhaus.de/none/zdf/12/08/120827_trailer_staffel13_kio_sok4_vh.wmv" );
					MovieSearchFragment.this.createOutput();
					byte[] buffer = new byte[ 8192 ];
					int comReadB  = 0;
					while( comReadB < st.length() ) {
						int readB = st.read( buffer, 0, buffer.length );
						if( readB <= 0 ) {
							break;
						}
						out.write( buffer, 0, readB );
						comReadB += readB;
					}
					out.close();
				} catch( IOException e ) {
					Log.e( "SearchMoveFragment", e.getMessage() );
				}

			}
		} );

		return v;
	}
}
