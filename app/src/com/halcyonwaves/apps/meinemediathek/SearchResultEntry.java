package com.halcyonwaves.apps.meinemediathek;

import java.io.File;
import java.util.List;

public class SearchResultEntry {

	public final String title;
	public final String description;
	public final File previewImage;
	public final String downloadLink;

	public SearchResultEntry( String title, String description, File previewImage, String downloadLink ) {
		this.title = title;
		this.description = description;
		this.previewImage = previewImage;
		this.downloadLink = downloadLink;
	}
}
