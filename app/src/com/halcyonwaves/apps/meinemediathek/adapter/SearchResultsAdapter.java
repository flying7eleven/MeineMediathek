package com.halcyonwaves.apps.meinemediathek.adapter;

import java.util.List;

import com.halcyonwaves.apps.meinemediathek.SearchResultEntry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SearchResultsAdapter extends ArrayAdapter< SearchResultEntry > {

	private final LayoutInflater mInflater;

	public SearchResultsAdapter( Context context ) {
		super( context, android.R.layout.simple_list_item_1 );
		this.mInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
	}

	public void setData( List< SearchResultEntry > data ) {
		this.clear();
		if( data != null ) {
			this.addAll( data );
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

		SearchResultEntry item = getItem( position );
		((TextView) view.findViewById( android.R.id.text1 )).setText( item.title );

		return view;
	}
}
