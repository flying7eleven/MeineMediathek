package com.halcyonwaves.apps.meinemediathek;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;

@ReportsCrashes(
	formKey = "",
	mailTo = "meinemediathek@halcyonwaves.com",
	customReportContent = {
			ReportField.ANDROID_VERSION,
			ReportField.APP_VERSION_CODE,
			ReportField.APP_VERSION_NAME,
			ReportField.APPLICATION_LOG,
			ReportField.AVAILABLE_MEM_SIZE,
			ReportField.BRAND,
			ReportField.BUILD,
			ReportField.CRASH_CONFIGURATION,
			ReportField.CUSTOM_DATA,
			ReportField.DEVICE_FEATURES,
			// ReportField.DEVICE_ID,
			ReportField.DISPLAY,
			// ReportField.DROPBOX,
			// ReportField.DUMPSYS_MEMINFO,
			ReportField.ENVIRONMENT,
			ReportField.EVENTSLOG,
			ReportField.FILE_PATH,
			ReportField.INITIAL_CONFIGURATION,
			// ReportField.INSTALLATION_ID,
			ReportField.IS_SILENT,
			ReportField.LOGCAT,
			ReportField.MEDIA_CODEC_LIST,
			ReportField.PACKAGE_NAME,
			ReportField.PHONE_MODEL,
			ReportField.PRODUCT,
			ReportField.RADIOLOG,
			ReportField.REPORT_ID,
			// ReportField.SETTINGS_SECURE,
			// ReportField.SETTINGS_SYSTEM,
			ReportField.SHARED_PREFERENCES,
			ReportField.STACK_TRACE,
			ReportField.THREAD_DETAILS,
			ReportField.TOTAL_MEM_SIZE,
			ReportField.USER_APP_START_DATE,
			ReportField.USER_COMMENT,
			ReportField.USER_CRASH_DATE,
	// ReportField.USER_EMAIL
	},
	mode = ReportingInteractionMode.DIALOG,
	sendReportsInDevMode = false,
	logcatArguments = {
			"-t",
			"500",
			"-v",
			"time" },
	resDialogTitle = R.string.dlg_title_appcrash,
	resDialogText = R.string.dlg_msg_appcrash )
public class ApplicationEntryPoint extends Application {

	private static final X500Principal DEBUG_DN = new X500Principal( "CN=Android Debug,O=Android,C=US" );

	public static boolean isApplicationDebuggable( final Context ctx ) {
		boolean debuggable = false;

		try {
			final PackageInfo pinfo = ctx.getPackageManager().getPackageInfo( ctx.getPackageName(), PackageManager.GET_SIGNATURES );
			final Signature signatures[] = pinfo.signatures;

			for( final Signature signature : signatures ) {
				final CertificateFactory cf = CertificateFactory.getInstance( "X.509" );
				final ByteArrayInputStream stream = new ByteArrayInputStream( signature.toByteArray() );
				final X509Certificate cert = (X509Certificate) cf.generateCertificate( stream );
				debuggable = cert.getSubjectX500Principal().equals( ApplicationEntryPoint.DEBUG_DN );
				if( debuggable ) {
					break;
				}
			}

		} catch( final NameNotFoundException e ) {
			// debuggable variable will remain false
		} catch( final CertificateException e ) {
			// debuggable variable will remain false
		}
		return debuggable;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		// ensure that ACRA can be used for bug tracking
		ACRA.init( this );
	}
}
