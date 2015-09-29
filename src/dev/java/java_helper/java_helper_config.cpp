# include "java_helper_header.h"
extern JavaVM *g_vm;
JNIEXPORT jstring JNICALL Java_dsn_dev_java_Nativecalls_dsn_1config_1get_1value_1string
(JNIEnv *env, jclass cla, jstring section, jstring key, jstring default_value, jstring dsptr)
{
    return chartojstring(env, dsn_config_get_value_string(env->GetStringUTFChars(section, false), env->GetStringUTFChars(key, false), env->GetStringUTFChars(default_value, false), env->GetStringUTFChars(dsptr, false)));
}

JNIEXPORT jboolean JNICALL Java_dsn_dev_java_Nativecalls_dsn_1config_1get_1value_1bool
(JNIEnv *env, jclass cla, jstring section, jstring key, jboolean default_value, jstring dsptr)
{
    return dsn_config_get_value_bool(env->GetStringUTFChars(section, false), env->GetStringUTFChars(key, false), default_value, env->GetStringUTFChars(dsptr, false));
}

/*
* Class:     dsn_dev_java_Nativecalls
* Method:    dsn_config_get_value_uint64
* Signature: (Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;)J
*/
JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1config_1get_1value_1uint64
(JNIEnv *env, jclass cla, jstring section, jstring key, jlong default_value, jstring dsptr)
{
    return dsn_config_get_value_uint64(env->GetStringUTFChars(section, false), env->GetStringUTFChars(key, false), default_value, env->GetStringUTFChars(dsptr, false));
}

/*
* Class:     dsn_dev_java_Nativecalls
* Method:    dsn_config_get_value_double
* Signature: (Ljava/lang/String;Ljava/lang/String;DLjava/lang/String;)D
*/
JNIEXPORT jdouble JNICALL Java_dsn_dev_java_Nativecalls_dsn_1config_1get_1value_1double
(JNIEnv *env, jclass cla, jstring section, jstring key, jdouble default_value, jstring dsptr)
{
    return dsn_config_get_value_double(env->GetStringUTFChars(section, false), env->GetStringUTFChars(key, false), default_value, env->GetStringUTFChars(dsptr, false));
}

/*
* Class:     dsn_dev_java_Nativecalls
* Method:    dsn_config_get_all_keys2
* Signature: (Ljava/lang/String;[Ljava/lang/String;[I)I
*/
JNIEXPORT jint JNICALL Java_dsn_dev_java_Nativecalls_dsn_1config_1get_1all_1keys2
(JNIEnv *env, jclass cla, jstring section, jobjectArray buffers, jintArray buffer_count)
{
    const char* buf[1000];
    int buf_cnt = 0;
    int ret = dsn_config_get_all_keys(env->GetStringUTFChars(section, false), buf, &buf_cnt);
    for (int i = 0; i < buf_cnt; i++)
    {
        jstring str = chartojstring(env, buf[i]);
        env->SetObjectArrayElement(buffers, i, str);
    }
    jint buf_cnt_jint = buf_cnt;
    env->SetIntArrayRegion(buffer_count, 0, 1, &buf_cnt_jint);
    return ret;
}