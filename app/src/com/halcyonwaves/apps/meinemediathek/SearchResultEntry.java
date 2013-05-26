package com.halcyonwaves.apps.meinemediathek;

import java.io.File;

public class SearchResultEntry {

	public final String description;
	public final String downloadLink;
	public final boolean isCurrentlyFskRestricted;
	public final File previewImage;
	public final String title;

	public SearchResultEntry( final String title, final String description, final File previewImage, final String downloadLink ) {
		this.title = title;
		this.description = description;
		this.previewImage = previewImage;
		this.downloadLink = downloadLink;
		if( downloadLink.toLowerCase().contains( "hinweis_fsk" ) ) {
			this.isCurrentlyFskRestricted = true;
		} else {
			this.isCurrentlyFskRestricted = false;
		}
	}
}
