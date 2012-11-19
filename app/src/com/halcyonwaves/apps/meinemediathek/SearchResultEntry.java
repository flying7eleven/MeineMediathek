package com.halcyonwaves.apps.meinemediathek;

import java.io.File;

public class SearchResultEntry {

	public final String title;
	public final String description;
	public final File previewImage;

	public SearchResultEntry( String title, String description, File previewImage ) {
		this.title = title;
		this.description = description;
		this.previewImage = previewImage;
	}
}
