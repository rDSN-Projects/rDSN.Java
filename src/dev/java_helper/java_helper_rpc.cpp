# include "java_helper_header.h"
extern JavaVM *g_vm;

static __thread struct java_function request_callback;

void get_request_callback_env()
{
    if (request_callback.magic != 0xdeadbeef)
    {
        int getEnvStat = g_vm->GetEnv((void **)&request_callback.env, DSN_JNI_VERSION);
        if (getEnvStat == JNI_EDETACHED)
        {
            g_vm->AttachCurrentThread((void **)&request_callback.env, NULL);
        }

        request_callback.clazz = request_callback.env->FindClass("dsn/dev/java/Serverlet");
        request_callback.method = request_callback.env->GetStaticMethodID(request_callback.clazz, "RpcRequestCallback", "(JI)V");
        request_callback.magic = 0xdeadbeef;
    }
    return;
}

void Rpc_Request_Callback(dsn_message_t msg, void* pram)
{
    get_request_callback_env();
    int index = *((int*)pram);
    request_callback.env->CallStaticVoidMethod(request_callback.clazz, request_callback.method, (jlong)msg, index);
}

JNIEXPORT jboolean JNICALL Java_dsn_dev_java_Nativecalls_dsn_1rpc_1register_1handler
(JNIEnv *env, jclass cla, jint code, jstring name, jint pram)
{
    int *ptr = new int;
    *ptr = pram;
    return dsn_rpc_register_handler(code, env->GetStringUTFChars(name, 0), Rpc_Request_Callback, (void*)ptr);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1rpc_1unregiser_1handler
(JNIEnv *env, jclass cla, jint code)
{
    dsn_rpc_unregiser_handler(code);
}

static __thread struct java_function response_callback;

void get_response_callback_env()
{
    if (response_callback.magic != 0xdeadbeef)
    {
        int getEnvStat = g_vm->GetEnv((void **)&response_callback.env, DSN_JNI_VERSION);
        if (getEnvStat == JNI_EDETACHED)
        {
            g_vm->AttachCurrentThread((void **)&response_callback.env, NULL);
        }

        response_callback.clazz = response_callback.env->FindClass("dsn/dev/java/Clientlet");
        response_callback.method = response_callback.env->GetStaticMethodID(response_callback.clazz, "RpcResponseCallback", "(IJJI)V");
        response_callback.magic = 0xdeadbeef;
    }
    return;
}

void Rpc_Response_Callback(dsn_error_t err, dsn_message_t req, dsn_message_t resp, void* pram)
{
    get_response_callback_env();
    int index = *((int*)pram);
    response_callback.env->CallStaticVoidMethod(response_callback.clazz, response_callback.method, err, (jlong)req, (jlong)resp, index);
}

JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1rpc_1create_1response_1task
(JNIEnv *env, jclass cla, jlong request, jint pram, jint reply_hash)
{
    int *ptr = new int;
    *ptr = pram;
    return (jlong)dsn_rpc_create_response_task((dsn_message_t)request, Rpc_Response_Callback, ptr, reply_hash);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1rpc_1call
(JNIEnv *env, jclass cla, jlong server, jlong task, jlong tracker)
{
    dsn_rpc_call(*(dsn_address_t*)&server, (dsn_task_t)task, (dsn_task_tracker_t)tracker);
}

JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1rpc_1call_1wait
(JNIEnv *env, jclass cla, jlong server, jlong request)
{
    return (jlong)dsn_rpc_call_wait(*(dsn_address_t*)&server, (dsn_message_t)request);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1rpc_1call_1one_1way
(JNIEnv *env, jclass cla, jlong server, jlong request)
{
    dsn_rpc_call_one_way(*(dsn_address_t*)&server, (dsn_message_t)request);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1rpc_1reply
(JNIEnv *env, jclass cla, jlong resp)
{
    dsn_rpc_reply((dsn_message_t)resp);
}

JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1rpc_1get_1response
(JNIEnv *env, jclass cla, jlong rpc_call)
{
    return (jlong)dsn_rpc_get_response((void*)rpc_call);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1rpc_1enqueue_1response
(JNIEnv *env, jclass cla, jlong rpc_call, jint err, jlong response)
{
    dsn_rpc_enqueue_response((void*)rpc_call, err, (void*)response);
}
