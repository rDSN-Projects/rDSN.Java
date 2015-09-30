# include "java_helper_header.h"

JNIEXPORT jint JNICALL Java_dsn_dev_java_Nativecalls_dsn_1error_1register
(JNIEnv *env, jclass cla, jstring name)
{
    return dsn_error_register(env->GetStringUTFChars(name, 0));
}

JNIEXPORT jstring JNICALL Java_dsn_dev_java_Nativecalls_dsn_1error_1to_1string
(JNIEnv *env, jclass cla, jint err)
{
    return chartojstring(env, dsn_error_to_string(err));
}

JNIEXPORT jint JNICALL Java_dsn_dev_java_Nativecalls_dsn_1threadpool_1code_1register
(JNIEnv *env, jclass cla, jstring name)
{
    return dsn_threadpool_code_register(env->GetStringUTFChars(name, 0));
}

JNIEXPORT jstring JNICALL Java_dsn_dev_java_Nativecalls_dsn_1threadpool_1code_1to_1string
(JNIEnv *env, jclass cla, jint pool_code)
{
    return chartojstring(env, dsn_threadpool_code_to_string(pool_code));
}

JNIEXPORT jint JNICALL Java_dsn_dev_java_Nativecalls_dsn_1threadpool_1code_1from_1string
(JNIEnv *env, jclass cla, jstring s, jint default_code)
{
    return dsn_threadpool_code_from_string(env->GetStringUTFChars(s, 0), default_code);
}

JNIEXPORT jint JNICALL Java_dsn_dev_java_Nativecalls_dsn_1threadpool_1code_1max
(JNIEnv *env, jclass cla)
{
    return dsn_threadpool_code_max();
}

JNIEXPORT jint JNICALL Java_dsn_dev_java_Nativecalls_dsn_1task_1code_1register2
(JNIEnv *env, jclass cla, jstring name, jint type, jint pri, jint pool)
{
    return dsn_task_code_register(env->GetStringUTFChars(name, 0), (dsn_task_type_t)type, (dsn_task_priority_t)pri, pool);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1task_1code_1query2
(JNIEnv *env, jclass cla, jint code, jintArray type, jintArray pri, jintArray pool)
{
    dsn_task_type_t ttype;
    dsn_task_priority_t tpri;
    dsn_threadpool_code_t tpool;
    dsn_task_code_query(code, &ttype, &tpri, &tpool);
    jint itype = ttype;
    jint ipri = tpri;
    jint ipool = tpool;
    env->SetIntArrayRegion(type, 0, 1, &itype);
    env->SetIntArrayRegion(pri, 0, 1, &ipri);
    env->SetIntArrayRegion(pool, 0, 1, &ipool);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1task_1code_1set_1threadpool
(JNIEnv *env, jclass cla, jint code, jint pool)
{
    dsn_task_code_set_threadpool(code, pool);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1task_1code_1set_1priority2
(JNIEnv *env, jclass cla, jint code, jint pri)
{
    dsn_task_code_set_priority(code, (dsn_task_priority_t)pri);
}

JNIEXPORT jstring JNICALL Java_dsn_dev_java_Nativecalls_dsn_1task_1code_1to_1string
(JNIEnv *env, jclass cla, jint code)
{
    return chartojstring(env, dsn_task_code_to_string(code));
}

JNIEXPORT jint JNICALL Java_dsn_dev_java_Nativecalls_dsn_1task_1code_1from_1string
(JNIEnv *env, jclass cla, jstring s, jint default_code)
{
    return dsn_task_code_from_string(env->GetStringUTFChars(s, 0), default_code);
}

JNIEXPORT jint JNICALL Java_dsn_dev_java_Nativecalls_dsn_1task_1code_1max
(JNIEnv *env, jclass cla)
{
    return dsn_task_code_max();
}

JNIEXPORT jstring JNICALL Java_dsn_dev_java_Nativecalls_dsn_1task_1type_1to_1string2
(JNIEnv *env, jclass cla, jint type)
{
    return chartojstring(env, dsn_task_type_to_string((dsn_task_type_t)type));
}

JNIEXPORT jstring JNICALL Java_dsn_dev_java_Nativecalls_dsn_1task_1priority_1to_1string2
(JNIEnv *env, jclass cla, jint pri)
{
    return chartojstring(env, dsn_task_priority_to_string((dsn_task_priority_t)pri));
}