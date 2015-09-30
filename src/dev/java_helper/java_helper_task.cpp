# include "java_helper_header.h"
extern JavaVM *g_vm;

static __thread struct java_function task_callback;

void get_task_callback_env()
{
    if (task_callback.magic != 0xdeadbeef)
    {
        int getEnvStat = g_vm->GetEnv((void **)&task_callback.env, DSN_JNI_VERSION);
        if (getEnvStat == JNI_EDETACHED)
        {
            g_vm->AttachCurrentThread((void **)&task_callback.env, NULL);
        }

        task_callback.clazz = task_callback.env->FindClass("dsn/dev/java/Clientlet");
        task_callback.method = task_callback.env->GetStaticMethodID(task_callback.clazz, "TaskCallback", "(I)V");
        task_callback.magic = 0xdeadbeef;
    }
    return;
}

void Task_Callback(void* pram)
{
    int index = *((int*)pram);
    get_task_callback_env();
    task_callback.env->CallStaticVoidMethod(task_callback.clazz, task_callback.method, index);
}

JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1task_1create
(JNIEnv *env, jclass cla, jint code, jint pram, jint hash)
{
    int *ptr = new int;
    *ptr = pram;
    return (jlong)dsn_task_create(code, Task_Callback, ptr, hash);
}

static __thread struct java_function task_timer_callback;

void get_task_timer_callback_env()
{
    if (task_timer_callback.magic != 0xdeadbeef)
    {
        int getEnvStat = g_vm->GetEnv((void **)&task_timer_callback.env, DSN_JNI_VERSION);
        if (getEnvStat == JNI_EDETACHED)
        {
            g_vm->AttachCurrentThread((void **)&task_timer_callback.env, NULL);
        }

        task_timer_callback.clazz = task_timer_callback.env->FindClass("dsn/dev/java/Clientlet");
        task_timer_callback.method = task_timer_callback.env->GetStaticMethodID(task_timer_callback.clazz, "TaskTimerCallback", "(I)V");
        task_timer_callback.magic = 0xdeadbeef;
    }
    return;
}

void Task_Timer_Callback(void* pram)
{
    int index = *((int*)pram);
    get_task_timer_callback_env();
    task_timer_callback.env->CallStaticVoidMethod(task_timer_callback.clazz, task_timer_callback.method, index);
}

JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1task_1create_1timer
(JNIEnv *env, jclass cla, jint code, jint pram, jint hash, jint interval_milliseconds)
{
    int *ptr = new int;
    *ptr = pram;
    return (jlong)dsn_task_create_timer(code, Task_Timer_Callback, ptr, hash, interval_milliseconds);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1task_1add_1ref
(JNIEnv *env, jclass cla, jlong task)
{
    dsn_task_add_ref((dsn_task_t)task);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1task_1release_1ref
(JNIEnv *env, jclass cla, jlong task)
{
    dsn_task_release_ref((dsn_task_t)task);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1task_1call
(JNIEnv *env, jclass cla, jlong task, jlong tracker, jint delay_milliseconds)
{
    dsn_task_call((dsn_task_t)task, (dsn_task_tracker_t)tracker, delay_milliseconds);
}

JNIEXPORT jboolean JNICALL Java_dsn_dev_java_Nativecalls_dsn_1task_1cancel
(JNIEnv *env, jclass cla, jlong task, jboolean wait_until_finished)
{
    return dsn_task_cancel((dsn_task_tracker_t)task, wait_until_finished);
}

JNIEXPORT jboolean JNICALL Java_dsn_dev_java_Nativecalls_dsn_1task_1cancel3
(JNIEnv *env, jclass cla, jlong task, jboolean wait_until_finished, jbooleanArray finished)
{
    if (finished == NULL)
        return dsn_task_cancel2((dsn_task_t)task, wait_until_finished, NULL);
    bool finish;
    bool ret = dsn_task_cancel2((dsn_task_t)task, wait_until_finished, &finish);
    jboolean finish_jbool = finish;
    env->SetBooleanArrayRegion(finished, 0, 1, &finish_jbool);
    return ret;
}

JNIEXPORT jboolean JNICALL Java_dsn_dev_java_Nativecalls_dsn_1task_1wait
(JNIEnv *env, jclass cla, jlong task)
{
    return dsn_task_wait((dsn_task_t)task);
}

JNIEXPORT jboolean JNICALL Java_dsn_dev_java_Nativecalls_dsn_1task_1wait_1timeout
(JNIEnv *env, jclass cla, jlong task, jint timeout_milliseconds)
{
    return dsn_task_wait_timeout((dsn_task_t)task, timeout_milliseconds);
}

JNIEXPORT jint JNICALL Java_dsn_dev_java_Nativecalls_dsn_1task_1error
(JNIEnv *env, jclass cla, jlong task)
{
    return dsn_task_error((dsn_task_t)task);
}
