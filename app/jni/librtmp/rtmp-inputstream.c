#define ANDROID_LOG_MODULE "RTMPInputStream[native]"

#include <jni.h>
#include <string.h>
#include "android-log.h"
#include "src/rtmp.h"

#define TMP_BUFFER_SIZE 64 * 1024

/*
 * Class:     com.halcyonwaves.apps.meinemediathek.ndk.RTMPInputStream
 * Method:    nativeInitStream
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_halcyonwaves_apps_meinemediathek_ndk_RTMPInputStream_nativeInitStream( JNIEnv *env, jobject thiz, jstring jurl ) {
	ALOG_TRACE( "nativeInitStream() start" );
	
	// get the supplied URL as a C string
	jsize url_clen = (*env)->GetStringLength( env, jurl );
	jsize url_blen = (*env)->GetStringUTFLength( env, jurl );
	char *url = calloc( 1, url_blen + 1 );
	(*env)->GetStringUTFRegion( env, jurl, 0, url_clen, url );
	ALOG_DEBUG( "nativeInitStream() url='%s'", url );
	
	// try to allocate the structure we need for the RTMP library
	RTMP *rtmp = RTMP_Alloc();
	if( NULL == rtmp ) {
		return (jint)0;
	}
	ALOG_TRACE( "nativeInitStream() rtmpHandle=%p", rtmp );
	
	// initialize the allocated strucutre and setup the URL to download
	RTMP_Init( rtmp );
	RTMP_SetupURL( rtmp, url );
	
	// try to connect to the remote host
	RTMP_Connect( rtmp, NULL );
	RTMP_ConnectStream( rtmp, 0 );
	
	// prepare the temporary buffer
	RTMP_SetBufferMS( rtmp, (uint32_t)2 * 3600 * 1000 );
	RTMP_UpdateBufferMS( rtmp );

	// allocate a temprary buffer for reading some bytes
	char *tmpBuffer = malloc( TMP_BUFFER_SIZE );
	if( 0 == tmpBuffer ) {
		ALOG_ERROR( "nativeInitStream() failed to allocate %d byes of memory!", TMP_BUFFER_SIZE );
		RTMP_Close( rtmp );
		RTMP_Free( rtmp );
		return (jint)0;
	}
	
	// read the first few frames to figure out the duration of the stream
	int read = 0;
	int valid = 0;
	double tmp_duration = 0.0;
	while( 0 < ( read = RTMP_Read( rtmp, tmpBuffer, TMP_BUFFER_SIZE ) ) && 0 == valid ) {
		if( 0 == valid && 0 < ( tmp_duration = RTMP_GetDuration( rtmp ) ) ) {
			// Now that we have a valid duration, report we have an extra few
			// seconds of buffer space to ensure we download the entire video
			tmp_duration = (tmp_duration + 5) * 1000;
			valid = 1;
			
			//
			RTMP_SetBufferMS( rtmp, (uint32_t)tmp_duration );
			RTMP_UpdateBufferMS( rtmp );
			
			//
			ALOG_DEBUG( "nativeInitStream() duration = %fms", tmp_duration );
		}
	}
	
	// free the temporary buffer
	free( tmpBuffer );
	
	// seek back to the beginning of the stream
	if( 0 == RTMP_SendSeek( rtmp, 0 ) ) {
		ALOG_WARN( "nativeInitStream() failed to seek back the stream to position 0" );
	}
	
	ALOG_TRACE( "nativeInitStream() end" );
	return (jint)rtmp;
}

/*
 * Class:     com.halcyonwaves.apps.meinemediathek.ndk.RTMPInputStream
 * Method:    nativeCloseStream
 * Signature: (I)I
 */
JNIEXPORT void JNICALL Java_com_halcyonwaves_apps_meinemediathek_ndk_RTMPInputStream_nativeCloseStream( JNIEnv *env, jobject thiz, jint jminfo ) {
	ALOG_TRACE( "nativeCloseStream() start" );
	
	// get the handle to the RTMP object
	RTMP *rtmp = (RTMP*)jminfo;
	ALOG_DEBUG( "nativeCloseStream() rtmpHandle=%p", rtmp );
	
	// close the stream and free all allocated resources
	RTMP_Close( rtmp );
	RTMP_Free( rtmp );
	
	ALOG_TRACE( "nativeCloseStream() end" );
}

/*
 * Class:     com.halcyonwaves.apps.meinemediathek.ndk.RTMPInputStream
 * Method:    nativeGetStreamLength
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_halcyonwaves_apps_meinemediathek_ndk_RTMPInputStream_nativeGetStreamLength( JNIEnv *env, jobject thiz, jint jminfo ) {
	ALOG_TRACE( "nativeGetStreamLength() start" );
	
	// get the handle to the RTMP object
	RTMP *rtmp = (RTMP*)jminfo;
	ALOG_DEBUG( "nativeGetStreamLength() rtmpHandle=%p", rtmp );
	
	// try to get the length of the stream
	double streamDuration = RTMP_GetDuration( rtmp ) * 1000.0;
	ALOG_DEBUG( "nativeGetStreamLength() duration=%f", streamDuration );
	
	ALOG_TRACE( "nativeGetStreamLength() end" );
	return (jint)streamDuration;
}
