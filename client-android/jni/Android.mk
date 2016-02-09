LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := apkPatch
LOCAL_SRC_FILES := apkPatch.c
LOCAL_LDLIBS := -lz -llog
include $(BUILD_SHARED_LIBRARY)


#LOCAL_PATH:= $(call my-dir)
#include $(CLEAR_VARS)
#
#LOCAL_SRC_FILES:= zapp.c
#
#LOCAL_MODULE:= zapp
#
#LOCAL_FORCE_STATIC_EXECUTABLE := true
#
##LOCAL_STATIC_LIBRARIES := libc
#
##LOCAL_MODULE_PATH := $(TARGET_OUT_OPTIONAL_EXECUTABLES)
##LOCAL_MODULE_TAGS := debug
#
#include $(BUILD_EXECUTABLE)