# include "java_helper_header.h"
extern JavaVM *g_vm;
JNIEXPORT jstring JNICALL Java_dsn_dev_java_Nativecalls_dsn_1config_1get_1value_1string
(JNIEnv *env, jclass cla, jstring section, jstring key, jstring default_value, jstring dsptr)
{
    return chartojstring(env, dsn_config_get_value_string(env->GetStringUTFChars(section, 0), env->GetStringUTFChars(key, 0), env->GetStringUTFChars(default_value, 0), env->GetStringUTFChars(dsptr, 0)));
}

JNIEXPORT jboolean JNICALL Java_dsn_dev_java_Nativecalls_dsn_1config_1get_1value_1bool
(JNIEnv *env, jclass cla, jstring section, jstring key, jboolean default_value, jstring dsptr)
{
    return dsn_config_get_value_bool(env->GetStringUTFChars(section, 0), env->GetStringUTFChars(key, 0), default_value, env->GetStringUTFChars(dsptr, 0));
}

/*
* Class:     dsn_dev_java_Nativecalls
* Method:    dsn_config_get_value_uint64
* Signature: (Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;)J
*/
JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1config_1get_1value_1uint64
(JNIEnv *env, jclass cla, jstring section, jstring key, jlong default_value, jstring dsptr)
{
    return dsn_config_get_value_uint64(env->GetStringUTFChars(section, 0), env->GetStringUTFChars(key, 0), default_value, env->GetStringUTFChars(dsptr, 0));
}

/*
* Class:     dsn_dev_java_Nativecalls
* Method:    dsn_config_get_value_double
* Signature: (Ljava/lang/String;Ljava/lang/String;DLjava/lang/String;)D
*/
JNIEXPORT jdouble JNICALL Java_dsn_dev_java_Nativecalls_dsn_1config_1get_1value_1double
(JNIEnv *env, jclass cla, jstring section, jstring key, jdouble default_value, jstring dsptr)
{
    return dsn_config_get_value_double(env->GetStringUTFChars(section, 0), env->GetStringUTFChars(key, 0), default_value, env->GetStringUTFChars(dsptr, 0));
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
    int ret = dsn_config_get_all_keys(env->GetStringUTFChars(section, 0), buf, &buf_cnt);
    for (int i = 0; i < buf_cnt; i++)
    {
        jstring str = chartojstring(env, buf[i]);
        env->SetObjectArrayElement(buffers, i, str);
    }
    jint buf_cnt_jint = buf_cnt;
    env->SetIntArrayRegion(buffer_count, 0, 1, &buf_cnt_jint);
    return ret;
}