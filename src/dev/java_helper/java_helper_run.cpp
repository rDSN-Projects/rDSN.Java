# include "java_helper_header.h"
extern JavaVM *g_vm;
//------------------------------APP REGISTER-------------------------------

static __thread struct java_function appcreate;

void get_appcreate_callback_env()
{
    if (appcreate.magic != 0xdeadbeef)
    {
        int getEnvStat = g_vm->GetEnv((void **)&appcreate.env, DSN_JNI_VERSION);
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
        int getEnvStat = g_vm->GetEnv((void **)&appstart.env, DSN_JNI_VERSION);
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
        int getEnvStat = g_vm->GetEnv((void **)&appdestroy.env, DSN_JNI_VERSION);
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

JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1address_1build
(JNIEnv *env, jclass cla, jstring hosts, jshort ports)
{
    return *(uint64_t*)&dsn_address_build(env->GetStringUTFChars(hosts, 0), ports);
}


JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1address_1build_1ipv4
(JNIEnv *env, jclass cla, jint ipv4, jshort port)
{
    return *(uint64_t*)&dsn_address_build_ipv4(ipv4, port);
}

JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1address_1build_1uri
(JNIEnv *env, jclass cla, jlong uri)
{
    return *(uint64_t*)&dsn_address_build_uri((void*)uri);
}

JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1address_1build_1group
(JNIEnv *env, jclass cla, jlong g)
{
    return *(uint64_t*)&dsn_address_build_group((void*)g);
}

JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1uri_1build
(JNIEnv *env, jclass cla, jstring url)
{
    return (jlong)dsn_uri_build(env->GetStringUTFChars(url, false));
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1uri_1destroy
(JNIEnv *env, jclass cla, jlong uri)
{
    dsn_uri_destroy((void*)uri);
}

JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1group_1build
(JNIEnv *env, jclass cla, jstring name)
{
    return (jlong)dsn_group_build(env->GetStringUTFChars(name, false));
}

JNIEXPORT jboolean JNICALL Java_dsn_dev_java_Nativecalls_dsn_1group_1add
(JNIEnv *env, jclass cla, jlong g, jlong ep)
{
    return dsn_group_add((void*)g, *(dsn_address_t*)&ep);
}

JNIEXPORT jboolean JNICALL Java_dsn_dev_java_Nativecalls_dsn_1group_1remove
(JNIEnv *env, jclass cla, jlong g, jlong ep)
{
    return dsn_group_remove((void*)g, *(dsn_address_t*)&ep);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1group_1set_1leader
(JNIEnv *env, jclass cla, jlong g, jlong ep)
{
    dsn_group_set_leader((void*)g, *(dsn_address_t*)&ep);
}

JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1group_1get_1leader
(JNIEnv *env, jclass cla, jlong g)
{
    return *(uint64_t*)&dsn_group_get_leader((void*)g);
}

JNIEXPORT jboolean JNICALL Java_dsn_dev_java_Nativecalls_dsn_1group_1is_1leader
(JNIEnv *env, jclass cla, jlong g, jlong ep)
{
    return dsn_group_is_leader((void*)g, *(dsn_address_t*)&ep);
}

JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1group_1next
(JNIEnv *env, jclass cla, jlong g, jlong ep)
{
    return *(uint64_t*)&dsn_group_next((void*)g, *(dsn_address_t*)&ep);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1group_1destroy
(JNIEnv *env, jclass cla, jlong g)
{
    dsn_group_destroy((void*)g);
}

JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1primary_1address
(JNIEnv *env, jclass cla)
{
    return *(uint64_t*)&dsn_primary_address();
}

JNIEXPORT jint JNICALL Java_dsn_dev_java_Nativecalls_dsn_1ipv4_1from_1host
(JNIEnv *env, jclass cla, jstring name)
{
    return dsn_ipv4_from_host(env->GetStringUTFChars(name, false));
}

JNIEXPORT jint JNICALL Java_dsn_dev_java_Nativecalls_dsn_1ipv4_1local
(JNIEnv *env, jclass cla, jstring network_interface)
{
    return dsn_ipv4_local(env->GetStringUTFChars(network_interface, false));
}

JNIEXPORT jstring JNICALL Java_dsn_dev_java_Nativecalls_dsn_1address_1to_1string
(JNIEnv *env, jclass cla, jlong addr)
{
    return chartojstring(env, dsn_address_to_string(*(dsn_address_t*)&addr));
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