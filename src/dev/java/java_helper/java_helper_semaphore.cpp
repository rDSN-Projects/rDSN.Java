# include "java_helper_header.h"

JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1exlock_1create
(JNIEnv *env, jclass cla, jboolean recursive)
{
    return (jlong)dsn_exlock_create(recursive);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1exlock_1destroy
(JNIEnv *env, jclass cla, jlong l)
{
    dsn_exlock_destroy((void*)l);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1exlock_1lock
(JNIEnv *env, jclass cla, jlong l)
{
    dsn_exlock_lock((void*)l);
}

JNIEXPORT jboolean JNICALL Java_dsn_dev_java_Nativecalls_dsn_1exlock_1try_1lock
(JNIEnv *env, jclass cla, jlong l)
{
    return dsn_exlock_try_lock((void*)l);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1exlock_1unlock
(JNIEnv *env, jclass cla, jlong l)
{
    dsn_exlock_unlock((void*)l);
}

JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1rwlock_1nr_1create
(JNIEnv *env, jclass cla)
{
    return (jlong)dsn_rwlock_nr_create();
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1rwlock_1nr_1destroy
(JNIEnv *env, jclass cla, jlong l)
{
    dsn_rwlock_nr_destroy((void*)l);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1rwlock_1nr_1lock_1read
(JNIEnv *env, jclass cla, jlong l)
{
    dsn_rwlock_nr_lock_read((void*)l);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1rwlock_1nr_1unlock_1read
(JNIEnv *env, jclass cla, jlong l)
{
    dsn_rwlock_nr_unlock_read((void*)l);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1rwlock_1nr_1lock_1write
(JNIEnv *env, jclass cla, jlong l)
{
    dsn_rwlock_nr_lock_write((void*)l);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1rwlock_1nr_1unlock_1write
(JNIEnv *env, jclass, jlong l)
{
    dsn_rwlock_nr_unlock_write((void*)l);
}

JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1semaphore_1create
(JNIEnv *env, jclass, jint initial_count)
{
    return (jlong)dsn_semaphore_create(initial_count);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1semaphore_1destroy
(JNIEnv *env, jclass, jlong s)
{
    dsn_semaphore_destroy((void*)s);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1semaphore_1signal
(JNIEnv *env, jclass, jlong s, jint count)
{
    dsn_semaphore_signal((void*)s, count);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1semaphore_1wait
(JNIEnv *env, jclass, jlong s)
{
    dsn_semaphore_wait((void*)s);
}

JNIEXPORT jboolean JNICALL Java_dsn_dev_java_Nativecalls_dsn_1semaphore_1wait_1timeout
(JNIEnv *env, jclass, jlong s, jint timeout_milliseconds)
{
    return dsn_semaphore_wait_timeout((void*)s, timeout_milliseconds);
}