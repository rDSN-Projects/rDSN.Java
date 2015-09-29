# include "java_helper_header.h"

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1log2
(JNIEnv *env, jclass cla, jstring file, jstring function, jint line, jint log_level, jstring title)
{
    dsn_log(env->GetStringUTFChars(file, 0), env->GetStringUTFChars(function, 0), line, (dsn_log_level_t)log_level, env->GetStringUTFChars(title, 0));
}

JNIEXPORT jint JNICALL Java_dsn_dev_java_Nativecalls_dsn_1log_1get_1start_1level2
(JNIEnv *env, jclass cla)
{
    return (jint)dsn_log_start_level;
}