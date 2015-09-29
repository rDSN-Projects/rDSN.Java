# include <string.h>
# include <vector>
# include "dsn_dev_java_Nativecalls.h"
# include <dsn/service_api_c.h>
# include <dsn/ports.h>

jstring chartojstring(JNIEnv* env, const char* str);

static __thread struct java_function
{
    int magic;
    JNIEnv *env;
    jclass clazz;
    jmethodID method;
};

jbyteArray Address_To_Jbyte(JNIEnv* env, dsn_address_t addr);

dsn_address_t* Jbyte_To_Address(JNIEnv* env, jbyteArray addrbyte);