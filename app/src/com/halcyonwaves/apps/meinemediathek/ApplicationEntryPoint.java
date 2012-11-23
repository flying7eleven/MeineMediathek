package com.halcyonwaves.apps.meinemediathek;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

@ReportsCrashes( formKey = "", mailTo = "meinemediathek@halcyonwaves.com", customReportContent = { ReportField.APP_VERSION_NAME, ReportField.APP_VERSION_CODE, ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT }, mode = ReportingInteractionMode.DIALOG, resDialogText = R.string.app_name )
public class ApplicationEntryPoint extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		ACRA.init( this );
	}
}
