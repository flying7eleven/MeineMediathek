package com.halcyonwaves.apps.meinemediathek.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SearchResultsAdapter extends ArrayAdapter< String > {

	private final LayoutInflater mInflater;

	public SearchResultsAdapter( Context context ) {
		super( context, android.R.layout.simple_list_item_1 );
		this.mInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
	}

	public void setData( List< String > data ) {
		clear();
		if( data != null ) {
			addAll( data );
		}
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent ) {
		View view;

		if( convertView == null ) {
			view = this.mInflater.inflate( android.R.layout.simple_list_item_1, parent, false );
		} else {
			view = convertView;
		}

		String item = getItem( position );
		((TextView) view.findViewById( android.R.id.text1 )).setText( item );

		return view;
	}
}
