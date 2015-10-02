# include "java_helper_header.h"
extern JavaVM *g_vm;

JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1msg_1create_1request
(JNIEnv *env, jclass cla, jint rpc_code, jint timeout_milliseconds, jint hash)
{
    return (jlong)dsn_msg_create_request(rpc_code, timeout_milliseconds, hash);
}

JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1msg_1create_1response
(JNIEnv *env, jclass cla, jlong req)
{
    return (jlong)dsn_msg_create_response((dsn_message_t)req);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1msg_1add_1ref
(JNIEnv *env, jclass cla, jlong msg)
{
    dsn_msg_release_ref((dsn_message_t)msg);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1msg_1release_1ref
(JNIEnv *env, jclass cla, jlong msg)
{
    dsn_msg_release_ref((dsn_message_t)msg);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1msg_1update_1request
(JNIEnv *env, jclass cla, jlong msg, jint timeout_milliseconds, jint hash)
{
    dsn_msg_update_request((dsn_message_t)msg, timeout_milliseconds, hash);
}

JNIEXPORT jint JNICALL Java_dsn_dev_java_Nativecalls_dsn_1msg_1query_1request2
(JNIEnv *env, jclass cla, jlong msg, jint timeout_milliseconds, jintArray phash)
{
    int hash = 0, timeout = timeout_milliseconds;
    dsn_msg_query_request((dsn_message_t)msg, &timeout, &hash);
    jint hash_jint = hash;
    env->SetIntArrayRegion(phash, 0, 1, &hash_jint);
    return timeout;
}

JNIEXPORT jbyteArray JNICALL Java_dsn_dev_java_Nativecalls_dsn_1msg_1read
(JNIEnv *env, jclass cla, jlong msg)
{
    void *ptr;
    size_t length;
    dsn_msg_read_next((dsn_message_t)msg, &ptr, &length);
    jbyteArray buffer = env->NewByteArray(length);
    env->SetByteArrayRegion(buffer, 0, length, (jbyte*)ptr);
    dsn_msg_read_commit((dsn_message_t)msg, length);
    return buffer;
}

JNIEXPORT jint JNICALL Java_dsn_dev_java_Nativecalls_dsn_1msg_1write
(JNIEnv *env, jclass cla, jlong msg, jbyteArray buffer, jint len, jint min_size)
{    
    jint off = 0;
    jbyte *buf = env->GetByteArrayElements(buffer, 0);

    while (off < len)
    {
        void *ptr = NULL;
        size_t buf_length, write_length;

        dsn_msg_write_next((dsn_message_t)msg, &ptr, &buf_length, min_size);     
        write_length = buf_length < (len - off) ? buf_length : (len - off);
        memcpy(ptr, (const void*)(buf + off), write_length);
        dsn_msg_write_commit((dsn_message_t)msg, write_length);

        off += write_length;
    }

    return off;
}

JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1msg_1body_1size
(JNIEnv *env, jclass cla, jlong msg)
{
    return (jlong)dsn_msg_body_size((dsn_message_t)msg);
}

JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1msg_1rw_1ptr
(JNIEnv *env, jclass cla, jlong msg, jlong offset_begin)
{
    return (jlong)dsn_msg_rw_ptr((dsn_message_t)msg, offset_begin);
}

JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1msg_1from_1address
(JNIEnv *env, jclass cla, jlong msg)
{
    auto ret = dsn_msg_from_address((dsn_message_t)msg);
    return *(uint64_t*)&ret;
}

JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1msg_1to_1address
(JNIEnv *env, jclass cla, jlong msg)
{
    auto ret = dsn_msg_to_address((dsn_message_t)msg);
    return *(uint64_t*)&ret;
}

