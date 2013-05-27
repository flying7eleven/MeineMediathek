#
# Compile libmms
# 
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := rtmp
LOCAL_SRC_FILES := src/amf.c src/hashswf.c src/log.c src/parseurl.c src/rtmp.c rtmp-inputstream.c
LOCAL_CFLAGS	:= -DNO_CRYPTO $(cflags_loglevels)
LOCAL_C_INCLUDES := $(LOCAL_PATH)
LOCAL_LDLIBS 	:= -llog

include $(BUILD_SHARED_LIBRARY)

