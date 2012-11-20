package com.halcyonwaves.apps.meinemediathek.fragments;

import com.halcyonwaves.apps.meinemediathek.R;

import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieOverviewFragment extends Fragment {

	private TextView tvMovieTitle = null;
	private TextView tvMovieDescription = null;
	private ImageView ivPreviewImage = null;

	@Override
	public View onCreateView( final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState ) {
		final View v = inflater.inflate( R.layout.fragment_movieoverview, container );

		// get the information passed to the fragment
		final Bundle passedInformation = this.getActivity().getIntent().getExtras();

		// get the handles to the controls we need to access
		this.tvMovieTitle = (TextView) v.findViewById( R.id.tv_movie_title_content );
		this.tvMovieDescription = (TextView) v.findViewById( R.id.tv_movie_description_content );
		this.ivPreviewImage = (ImageView) v.findViewById( R.id.iv_movie_preview_image );

		// set the content for all of the fetched controls
		this.tvMovieTitle.setText( passedInformation.getString( "title" ) );
		this.tvMovieDescription.setText( passedInformation.getString( "description" ) );
		this.ivPreviewImage.setImageBitmap( BitmapFactory.decodeFile( passedInformation.getString( "previewImage" ) ) );

		// return the created view
		return v;
	}
}
