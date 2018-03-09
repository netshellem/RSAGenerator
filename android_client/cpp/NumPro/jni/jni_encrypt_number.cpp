
#include <string.h>
#include <jni.h>
#include <android/log.h>
#include "data.h"
#include "base64.h"

#define  LOG_TAG    "numpro"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define CLASS_NAME "com/cmgc/NumPro"
#define _NATIVE_SET_PHONENUM "native_setPhoneNumber"



jstring Java_com_cmgc_numpro_native_setPhoneNumber(JNIEnv* env, jobject thiz, jstring num)
{
    jmethodID constructor;
    jmethodID getInstance;
    jmethodID generatePublic;
    if(num == NULL)
        return NULL;
    int length = sizeof(c_data)/sizeof(c_data[0]);
    
    jclass KeySpecCLASS = env->FindClass("java/security/spec/X509EncodedKeySpec");
    constructor = env->GetMethodID( KeySpecCLASS, "<init>", "([B)V");
    jbyteArray c_result = env->NewByteArray(length);
    env->SetByteArrayRegion(c_result, 0, length, reinterpret_cast<jbyte*>(c_data));
    jobject keySpecobj = env->NewObject( KeySpecCLASS, constructor, c_result);


    jclass KeyFactoryCLASS = env->FindClass("java/security/KeyFactory");
    getInstance = env->GetStaticMethodID(KeyFactoryCLASS, "getInstance","(Ljava/lang/String;)Ljava/security/KeyFactory;");
    jstring algoName = env->NewStringUTF("RSA");
    jobject keyFactoryobj = env->CallStaticObjectMethod(KeyFactoryCLASS, getInstance, algoName);
    generatePublic = env->GetMethodID(KeyFactoryCLASS, "generatePublic", "(Ljava/security/spec/KeySpec;)Ljava/security/PublicKey;");
    jobject pubKeyobj = env->CallObjectMethod(keyFactoryobj, generatePublic, keySpecobj);

    jclass CipherCLASS = env->FindClass("javax/crypto/Cipher");
    getInstance = env->GetStaticMethodID(CipherCLASS, "getInstance","(Ljava/lang/String;)Ljavax/crypto/Cipher;");
    jstring cipherAlgoName = env->NewStringUTF("RSA/ECB/PKCS1Padding");
    jobject cipherobj = env->CallStaticObjectMethod(CipherCLASS, getInstance, cipherAlgoName);

    jmethodID initCipher = env->GetMethodID(CipherCLASS, "init", "(ILjava/security/Key;)V");
    env->CallVoidMethod(cipherobj, initCipher, 1, pubKeyobj);

    jclass     jstrObj   = env->FindClass("java/lang/String");
    jmethodID  getBytes  = env->GetMethodID(jstrObj, "getBytes", "()[B");
    jbyteArray byteArray =  (jbyteArray)env->CallObjectMethod(num, getBytes);

    jmethodID doFinal = env->GetMethodID(CipherCLASS, "doFinal","([B)[B");
    jbyteArray arr = (jbyteArray)env->CallObjectMethod(cipherobj, doFinal, byteArray);


    int len = env->GetArrayLength (arr);
    char* buf = new char[len];
    env->GetByteArrayRegion (arr, 0, len, reinterpret_cast<jbyte*>(buf));

    //std::string data = base64_encode((const unsigned char*)c_data, length);
    std::string data = base64_encode((unsigned char*)buf, len);
    std::string ver(version);
    data =  ver + data;
    jstring result = env->NewStringUTF(data.c_str());
   
    env->DeleteLocalRef(algoName);
    env->DeleteLocalRef(cipherAlgoName);
    env->DeleteLocalRef(KeySpecCLASS);
    env->DeleteLocalRef(KeyFactoryCLASS);
    env->DeleteLocalRef(CipherCLASS);
    //env->ReleaseByteArrayElements( arr,reinterpret_cast<jbyte*>(buf), 0);
    
    return result;
}

static JNINativeMethod g_methods[] = {

        { _NATIVE_SET_PHONENUM, "(Ljava/lang/String;)Ljava/lang/String;", (void*) Java_com_cmgc_numpro_native_setPhoneNumber }
};

jint register_com_cmgc_jni(JavaVM* jvm) {
    jint result = -1;
    JNIEnv* env = NULL;
    LOGE("start jni...");

    if (jvm->GetEnv((void **)&env, JNI_VERSION_1_4) == JNI_OK) {
        jclass clazz = env->FindClass( CLASS_NAME);

        if (clazz != NULL) {
            if (env->RegisterNatives(clazz, g_methods, sizeof(g_methods) / sizeof(JNINativeMethod)) == 0) {
                result = JNI_VERSION_1_4;
            }

            env->DeleteLocalRef( clazz);
        }
    }

    return result;
}


void unregister_com_cmgc_jni(JavaVM* jvm) {
    JNIEnv* env = NULL;
    if (jvm->GetEnv((void **)&env, JNI_VERSION_1_4) == JNI_OK) {
        jclass clazz = env->FindClass(CLASS_NAME);
        if (clazz != NULL) {
            env->UnregisterNatives(clazz);
            env->DeleteLocalRef( clazz);
        }
    }
}

jint JNI_OnLoad(JavaVM* jvm, void* reserved) {
    return register_com_cmgc_jni(jvm);
}

void JNI_OnUnload(JavaVM* jvm, void* reserved) {

    unregister_com_cmgc_jni(jvm);
}
