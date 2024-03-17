#
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

# compile cpp and put in in FibNative
LOCAL_MODULE    := FibNative

LOCAL_SRC_FILES := FibNative.cpp
#LOCAL_SRC_FILES := FibNative.c

# output - shared library so
include $(BUILD_SHARED_LIBRARY)
