# include "java_helper_header.h"
extern JavaVM *g_vm;

JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1file_1open
(JNIEnv *env, jclass cla, jstring file_name, jint flag, jint pmode)
{
    return (jlong)dsn_file_open(env->GetStringUTFChars(file_name, 0), flag, pmode);
}

JNIEXPORT jint JNICALL Java_dsn_dev_java_Nativecalls_dsn_1file_1close
(JNIEnv *env, jclass cla, jlong file)
{
    return dsn_file_close((dsn_handle_t)file);
}

static __thread struct java_function aio_callback;

void get_aio_callback_env()
{
    if (aio_callback.magic != 0xdeadbeef)
    {
        int getEnvStat = g_vm->GetEnv((void **)&aio_callback.env, DSN_JNI_VERSION);
        if (getEnvStat == JNI_EDETACHED)
        {
            g_vm->AttachCurrentThread((void **)&aio_callback.env, NULL);
        }

        aio_callback.clazz = aio_callback.env->FindClass("dsn/dev/java/Serverlet");
        aio_callback.method = aio_callback.env->GetStaticMethodID(aio_callback.clazz, "AioCallback", "(III)V");
        aio_callback.magic = 0xdeadbeef;
    }
    return;
}

void Aio_Callback(dsn_error_t err, size_t size, void* pram)
{
    int index = *((int*)pram); 
    get_aio_callback_env();
    aio_callback.env->CallStaticVoidMethod(aio_callback.clazz, aio_callback.method, err, size, index);
}

JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1file_1create_1aio_1task
(JNIEnv *env, jclass cla, jint code, jint pram, jint hash)
{
    int *ptr = new int;
    *ptr = pram;
    return (jlong)dsn_file_create_aio_task(code, Aio_Callback, ptr, hash);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1file_1read
(JNIEnv *env, jclass cla, jlong file, jbyteArray buffer, jint count, jlong offset, jlong cbtask, jlong tracker)
{
    char *bufferc = new char[count];
    dsn_file_read((dsn_handle_t)file, bufferc, count, offset, (dsn_task_t)cbtask, (dsn_task_tracker_t)tracker);
    env->SetByteArrayRegion(buffer, 0, count, (jbyte*)buffer);
    return;
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1file_1write
(JNIEnv *env, jclass cla, jlong file, jbyteArray buffer, jint count, jlong offset, jlong cbtask, jlong tracker)
{
    char *bufferc = (char*)env->GetByteArrayElements(buffer, 0);
    dsn_file_write((dsn_handle_t)file, bufferc, count, offset, (dsn_task_t)cbtask, (dsn_task_tracker_t)tracker);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1file_1copy_1remote_1directory
(JNIEnv *env, jclass cla, jlong remote, jstring source_dir, jstring dest_dir, jboolean overwrite, jlong cbtask, jlong tracker)
{
    dsn_file_copy_remote_directory(
        *(dsn_address_t*)&remote, 
        env->GetStringUTFChars(source_dir, 0), 
        env->GetStringUTFChars(dest_dir, 0), 
        overwrite, 
        (dsn_task_t)cbtask, 
        (dsn_task_tracker_t)tracker
        );
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1file_1copy_1remote_1files
(JNIEnv *env, jclass cla, jlong remote, jstring source_dir, jobjectArray source_files, jstring dest_dir, jboolean overwrite, jlong cbtask, jlong tracker)
{
    int cnt = env->GetArrayLength(source_files);
    std::vector<std::string> files;
    std::vector<const char*> files_ptr;
    files_ptr.resize(cnt);
    files.resize(cnt);
    for (int i = 0; i < cnt; i++)
    {
        jstring jstr = (jstring)env->GetObjectArrayElement(source_files, i);
        const char* tmps = env->GetStringUTFChars(jstr, 0);
        files[i] = std::string(tmps);
        files_ptr[i] = files[i].c_str();
        env->DeleteLocalRef(jstr);
    }

    dsn_file_copy_remote_files(
        *(dsn_address_t*)&remote,
        env->GetStringUTFChars(source_dir, 0),
        &files_ptr[0], 
        env->GetStringUTFChars(dest_dir, 0), 
        overwrite,
        (dsn_task_t)cbtask,
        (dsn_task_tracker_t)tracker
        );
}

JNIEXPORT jlong JNICALL Java_dsn_dev_java_Nativecalls_dsn_1file_1get_1io_1size
(JNIEnv *env, jclass cla, jlong cb_task)
{
    return (jlong)dsn_rpc_get_response((void*)cb_task);
}

JNIEXPORT void JNICALL Java_dsn_dev_java_Nativecalls_dsn_1file_1task_1enqueue
(JNIEnv *env, jclass cla, jlong cb_task, jint err, jlong size)
{
    dsn_file_task_enqueue((void*)cb_task, err, size);
}
