# include "java_helper_header.h"
extern JavaVM *g_vm;
//------------------------------APP REGISTER-------------------------------

static __thread struct java_function appcreate;

void get_appcreate_callback_env()
{
    if (appcreate.magic != 0xdeadbeef)
    {
        int getEnvStat = g_vm->GetEnv((void **)&appcreate.env, JNI_VERSION_1_8);
        if (getEnvStat == JNI_EDETACHED)
        {
            g_vm->AttachCurrentThread((void **)&appcreate.env, NULL);
        }

        appcreate.clazz = appcreate.env->FindClass("dsn/dev/java/ServiceApp");
        appcreate.method = appcreate.env->GetStaticMethodID(appcreate.clazz, "AppCreate", "(Ljava/lang/String;)I");
        appcreate.magic = 0xdeadbeef;
    }
    return;
}

void* CallBack_AppCreate(const char *app_name)
{
    int *ptr = new int;
    get_appcreate_callback_env();
    *ptr = appcreate.env->CallStaticIntMethod(appcreate.clazz, appcreate.method, chartojstring(appcreate.env, app_name));
    return (void*)ptr;
}

static __thread struct java_function appstart;

void get_appstart_callback_env()
{
    if (appstart.magic != 0xdeadbeef)
    {
        int getEnvStat = g_vm->GetEnv((void **)&appstart.env, JNI_VERSION_1_8);
        if (getEnvStat == JNI_EDETACHED)
        {
            g_vm->AttachCurrentThread((void **)&appstart.env, NULL);
        }
        appstart.clazz = appstart.env->FindClass("dsn/dev/java/ServiceApp");
        appstart.method = appstart.env->GetStaticMethodID(appstart.clazz, "AppStart", "(I[Ljava/lang/String;)I");
        appstart.magic = 0xdeadbeef;
    }
    return;
}

int CallBack_AppStart(void *app_index, int argc, char** argv)
{
    get_appstart_callback_env();
    jclass clazz = appstart.env->FindClass("Ljava/lang/String;");
    jobjectArray args = (appstart.env)->NewObjectArray(argc + 1, clazz, 0);
    for (int i = 0; i < argc; i++)
    {
        jstring str = chartojstring(appstart.env, argv[i]);
        appstart.env->SetObjectArrayElement(args, i, str);
    }

    int err = appstart.env->CallStaticIntMethod(clazz, appstart.method, *((int*)app_index), args);
    return err;
}

static __thread struct java_function appdestroy;

void get_appdestroy_callback_env()
{
    if (appdestroy.magic != 0xdeadbeef)
    {
        int getEnvStat = g_vm->GetEnv((void **)&appdestroy.env, JNI_VERSION_1_8);
        if (getEnvStat == JNI_EDETACHED)
        {
            g_vm->AttachCurrentThread((void **)&appdestroy.env, NULL);
        }

        appdestroy.clazz = appdestroy.env->FindClass("dsn/dev/java/ServiceApp");
        appdestroy.method = appdestroy.env->GetStaticMethodID(appdestroy.clazz, "AppDestroy", "(IB)V");
        appdestroy.magic = 0xdeadbeef;
    }
    return;
}

void CallBack_AppDestroy(void *app_index, bool cleanup)
{
    get_appdestroy_callback_env();
    appdestroy.env->CallStaticVoidMethod(appdestroy.clazz, appdestroy.method, *((int*)app_index), cleanup);
    return;
}

JNIEXPORT jboolean JNICALL Java_dsn_dev_java_Nativecalls_dsn_1register_1app_1role_1managed
(JNIEnv *env, jclass cla, jstring type_name)
{
    env->GetJavaVM(&g_vm);
    const char* name = env->GetStringUTFChars(type_name, 0);
    return dsn_register_app_role(name, CallBack_AppCreate, CallBack_AppStart, CallBack_AppDestroy);
}

//----------------------------------APP----------------------------------

JNIEXPORT jboolean JNICALL Java_dsn_dev_java_Nativecalls_dsn_1mimic_1app
(JNIEnv *env, jclass cla, jstring app_name, jint index)
{
    return dsn_mimic_app(env->GetStringUTFChars(app_name, 0), index);
}

//----------------------------------RUN----------------------------------

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1run
(JNIEnv *env, jclass cla, jint argc, jobjectArray argv, jboolean sleep_after_init)
{
    env->GetJavaVM(&g_vm);
    std::vector<std::string> args;
    std::vector<char*> args_ptr;
    args_ptr.resize(argc);
    args.resize(argc);
    for (int i = 0; i < argc; i++)
    {
        jstring jstr = (jstring)env->GetObjectArrayElement(argv, i);
        const char* tmps = env->GetStringUTFChars(jstr, 0);
        args[i] = std::string(tmps);
        args_ptr[i] = (char*)args[i].c_str();
        env->DeleteLocalRef(jstr);
    }
    dsn_run(argc, &args_ptr[0], sleep_after_init);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1run_1config
(JNIEnv *env, jclass cla, jstring config, jboolean sleep_after_init)
{
    dsn_run_config(env->GetStringUTFChars(config, 0), sleep_after_init);
}

//--------------------------------ADDR------------------------------------

JNIEXPORT jbyteArray JNICALL Java_dsn_dev_java_Nativecalls_dsn_1address_1build
(JNIEnv *env, jclass cla, jstring hosts, jshort ports)
{
    dsn_address_t addr;
    dsn_address_build(&addr, env->GetStringUTFChars(hosts, 0), ports);
    return Address_To_Jbyte(env, addr);
}

JNIEXPORT jbyteArray JNICALL Java_dsn_dev_java_Nativecalls_dsn_1primary_1address2
(JNIEnv *env, jclass cla)
{
    dsn_address_t addr;
    dsn_primary_address2(&addr);
    return Address_To_Jbyte(env, addr);
}

//------------------------time/rand--------------------------------------

JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1now_1ns
(JNIEnv *env, jclass cla)
{
    return dsn_now_ns();
}

JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1random64
(JNIEnv *env, jclass cla, jlong min, jlong max)
{
    return dsn_random64(min, max);
}

//-------------------------other----------------------------------------

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1coredump
(JNIEnv *env, jclass cla)
{
    dsn_coredump();
}

JNIEXPORT jint JNICALL Java_dsn_dev_java_Nativecalls_dsn_1crc32_1compute
(JNIEnv *env, jclass cla, jlong ptr, jlong size, jint init_crc)
{
    return dsn_crc32_compute((void*)ptr, size, init_crc);
}

JNIEXPORT jint JNICALL Java_dsn_dev_java_Nativecalls_dsn_1crc32_1concatenate
(JNIEnv *env, jclass cla, jint xy_init, jint x_init, jint x_final, jlong x_size, jint y_init, jint y_final, jlong y_size)
{
    return dsn_crc32_concatenate(xy_init, x_init, x_final, x_size, y_init, y_final, y_size);
}