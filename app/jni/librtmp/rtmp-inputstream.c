#define ANDROID_LOG_MODULE "RTMPInputStream[native]"

#include <jni.h>
#include <string.h>
#include "android-log.h"
#include "src/rtmp.h"

#define TMP_BUFFER_SIZE_BYTES 64 * 1024
#define TMP_BUFFER_SIZE_TIME 2 * 3600 * 1000

typedef struct RTMPInfo {
	unsigned char* buffer;
	unsigned long bsize;
	double duration;
	RTMP *rtmpHandle;
} RTMPInfo;

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
	
	// allocate the info structure
	RTMPInfo *rtmpInfo = (RTMPInfo*)calloc( 1, sizeof( struct RTMPInfo ) );
	if( NULL == rtmpInfo ) {
		ALOG_ERROR( "nativeInitStream() failed to allocate %d byes of memory for the info structure!", sizeof( struct RTMPInfo ) );
		return (jint)0;
	}
	ALOG_TRACE( "nativeInitStream() rtmpInfoStructure=%p", rtmpInfo );
	
	// try to allocate the structure we need for the RTMP library
	rtmpInfo->rtmpHandle = RTMP_Alloc();
	if( 0 == rtmpInfo->rtmpHandle ) {
		ALOG_ERROR( "nativeInitStream() failed to allocate the RTMP handle!" );
		free( rtmpInfo );
		return (jint)0;
	}
	ALOG_TRACE( "nativeInitStream() rtmpHandle=%p", rtmpInfo->rtmpHandle );
	
	// allocate a temprary buffer for reading some bytes
	rtmpInfo->buffer = (unsigned char*) malloc( TMP_BUFFER_SIZE_BYTES * sizeof( unsigned char ) );
	rtmpInfo->bsize = TMP_BUFFER_SIZE_BYTES * sizeof( unsigned char );
	if( 0 == rtmpInfo->buffer ) {
		ALOG_ERROR( "nativeInitStream() failed to allocate %d byes of memory!", TMP_BUFFER_SIZE_BYTES * sizeof( unsigned char ) );
		RTMP_Free( rtmpInfo->rtmpHandle );
		free( rtmpInfo );
		return (jint)0;
	}
	ALOG_TRACE( "nativeInitStream() rtmpInfoStructureBuffer=%p", rtmpInfo->buffer );
	
	// initialize the allocated strucutre and setup the URL to download
	RTMP_Init( rtmpInfo->rtmpHandle );
	RTMP_SetupURL( rtmpInfo->rtmpHandle, url );
	
	// try to connect to the remote host
	if( FALSE == RTMP_Connect( rtmpInfo->rtmpHandle, NULL ) ) {
		ALOG_ERROR( "nativeInitStream() failed to connect to the stream!" );
		RTMP_Close( rtmpInfo->rtmpHandle );
		RTMP_Free( rtmpInfo->rtmpHandle );
		free( rtmpInfo->buffer );
		free( rtmpInfo );
		return (jint)0;
	}
	RTMP_ConnectStream( rtmpInfo->rtmpHandle, 0 );
	
	// prepare the temporary buffer
	RTMP_SetBufferMS( rtmpInfo->rtmpHandle, (uint32_t)TMP_BUFFER_SIZE_TIME );
	RTMP_UpdateBufferMS( rtmpInfo->rtmpHandle );
	
	// read the first few frames to figure out the duration of the stream
	int read = 0;
	int valid = 0;
	while( 0 < ( read = RTMP_Read( rtmpInfo->rtmpHandle, rtmpInfo->buffer, TMP_BUFFER_SIZE_BYTES ) ) && 0 == valid ) {
		ALOG_TRACE( "nativeInitStream() read %d bytes", read );
		if( 0 == valid && 0 < ( rtmpInfo->duration = RTMP_GetDuration( rtmpInfo->rtmpHandle ) ) ) {
			ALOG_TRACE( "nativeInitStream() duration before increasing = %fs", rtmpInfo->duration );
			rtmpInfo->duration = (rtmpInfo->duration + 5) * 1000;
			ALOG_TRACE( "nativeInitStream() duration after increasing = %fms", rtmpInfo->duration );
			valid = 1;
			
			//
			RTMP_SetBufferMS( rtmpInfo->rtmpHandle, (uint32_t)rtmpInfo->duration );
			RTMP_UpdateBufferMS( rtmpInfo->rtmpHandle );
			
			//
			ALOG_DEBUG( "nativeInitStream() duration = %fms", rtmpInfo->duration );
		}
	}
	
	// seek back to the beginning of the stream
	if( FALSE == RTMP_SendSeek( rtmpInfo->rtmpHandle, 0 ) ) {
		ALOG_WARN( "nativeInitStream() failed to seek back the stream to position 0" );
	}
	
	ALOG_TRACE( "nativeInitStream() end" );
	return (jint)rtmpInfo;
}

/*
 * Class:     com.halcyonwaves.apps.meinemediathek.ndk.RTMPInputStream
 * Method:    nativeReadStream
 * Signature: (I[BII)I
 */
JNIEXPORT jint JNICALL Java_com_halcyonwaves_apps_meinemediathek_ndk_RTMPInputStream_nativeReadStream( JNIEnv *env, jobject thiz, jint jminfo, jbyteArray jbuf, jint off, jint len ) {
	ALOG_TRACE( "nativeReadStream() start" );
	
	// get the handle to the RTMP object
	RTMPInfo *rtmp = (RTMPInfo*)jminfo;
	ALOG_DEBUG( "nativeReadStream() rtmpHandle=%p", rtmp->rtmpHandle );
	
	// be sure that our temporary buffer is big enough
	if( len > rtmp->bsize ) {
		rtmp->bsize = len;
		free( rtmp->buffer );
		rtmp->buffer = calloc( len, sizeof( unsigned char ) );
		if( 0 == rtmp->buffer ) {
			return -1;
		}
	}
	
	// try to read the stream
	ALOG_TRACE( "nativeReadStream() calling RTMP_Read len=%d", len );
	int bytesRead = RTMP_Read( rtmp->rtmpHandle, rtmp->buffer, len );
	ALOG_TRACE( "nativeReadStream() got %d bytes from RTMP_Read", bytesRead );

	//
	(*env)->SetByteArrayRegion( env, jbuf, off, bytesRead, rtmp->buffer );
		
	// return
	ALOG_TRACE( "nativeReadStream() end" );
	return bytesRead;
}


/*
 * Class:     com.halcyonwaves.apps.meinemediathek.ndk.RTMPInputStream
 * Method:    nativeCloseStream
 * Signature: (I)I
 */
JNIEXPORT void JNICALL Java_com_halcyonwaves_apps_meinemediathek_ndk_RTMPInputStream_nativeCloseStream( JNIEnv *env, jobject thiz, jint jminfo ) {
	ALOG_TRACE( "nativeCloseStream() start" );
	
	// get the handle to the RTMP object
	RTMPInfo *rtmp = (RTMPInfo*)jminfo;
	ALOG_DEBUG( "nativeCloseStream() rtmpHandle=%p", rtmp->rtmpHandle );
	
	// close the stream and free all allocated resources for this handle
	RTMP_Close( rtmp->rtmpHandle );
	RTMP_Free( rtmp->rtmpHandle );
	
	// free the info structure
	free( rtmp->buffer );
	free( rtmp );
	
	// return to the caller
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
	RTMPInfo *rtmp = (RTMPInfo*)jminfo;
	ALOG_DEBUG( "nativeGetStreamLength() rtmpInfoStruct=%p, rtmpDuration=%d", rtmp, rtmp->duration );
	
	ALOG_TRACE( "nativeGetStreamLength() end" );
	return (jint)rtmp->duration;
}
