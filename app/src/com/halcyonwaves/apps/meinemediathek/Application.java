package com.halcyonwaves.apps.meinemediathek;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

@ReportsCrashes( formKey = "", mailTo = "tim@huetz.biz", mode = ReportingInteractionMode.DIALOG )
public class Application extends android.app.Application {

	@Override
	public void onCreate() {
		ACRA.init( this );
		super.onCreate();
	}

}
