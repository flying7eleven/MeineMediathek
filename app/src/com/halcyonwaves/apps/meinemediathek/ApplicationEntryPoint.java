package com.halcyonwaves.apps.meinemediathek;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;

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
	}
}
