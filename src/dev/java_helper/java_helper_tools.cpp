# include "java_helper_header.h"

JavaVM *g_vm;

jstring chartojstring(JNIEnv* env, const char* str)
{
    jclass clazz = env->FindClass("Ljava/lang/String;");
    jmethodID method = env->GetMethodID(clazz, "<init>", "([BLjava/lang/String;)V");
    jbyteArray bytes = env->NewByteArray(strlen(str));
    env->SetByteArrayRegion(bytes, 0, strlen(str), (jbyte*)str);
    jstring encoding = env->NewStringUTF("utf-8");
    return (jstring)env->NewObject(clazz, method, bytes, encoding);
}
//
//jbyteArray Address_To_Jbyte(JNIEnv* env, dsn_address_t addr)
//{
//    int len = sizeof(addr);
//    jbyteArray addrbyte = env->NewByteArray(len + 1);
//    env->SetByteArrayRegion(addrbyte, 0, len, (jbyte*)&addr);
//    return addrbyte;
//}
//
//dsn_address_t* Jbyte_To_Address(JNIEnv* env, jbyteArray addrbyte)
//{
//    return (dsn_address_t*)env->GetByteArrayElements(addrbyte, false);
//}

/*void Get_JavaVM(JNIEnv* env)
{
    if (jvm.magic != 0xdeadbeef)
    {
        env->GetJavaVM(&jvm.vm);
    }
    return;
}*/