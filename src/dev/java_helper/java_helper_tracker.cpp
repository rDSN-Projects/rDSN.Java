# include "java_helper_header.h"

JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1task_1tracker_1create
(JNIEnv *env, jclass cla, jint task_bucket_count)
{
    dsn_task_tracker_t tracker = dsn_task_tracker_create(task_bucket_count);
    return (jlong)tracker;
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1task_1tracker_1destroy
(JNIEnv *env, jclass cla, jlong handle)
{
    dsn_task_tracker_destroy((dsn_task_tracker_t)handle);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1task_1tracker_1wait_1all
(JNIEnv *env, jclass cla, jlong handle)
{
    dsn_task_tracker_wait_all((dsn_task_tracker_t)handle);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1task_1tracker_1cancel_1all
(JNIEnv *env, jclass cla, jlong handle)
{
    dsn_task_tracker_cancel_all((dsn_task_tracker_t)handle);
}