#include <jni.h>

/* As mantioned in the PDF explantions about the use of C++ as native
 * code mostly as namespaces
 * */

namespace com_marakana_android_fibonacciservice{

	/* inside the namespace you define you function, Pure C*/
	static long fib(long n){

		if(n == 0)return 0;
		if(n == 1)return 1;
		return fib(n-1) + fib(n-2);
	}

	/*JNI has specifiec data types. you can find good reference in marakana site
	 * long --> jlong.
	 * JNI has two arguments that is allways there: the first one is JNIEnv and the second
	 * is either a pointer to caller (class or a object).
	 * Ower input decelred static which is class propertie and not an object property.
	 * */

	/* This is the JNI wrapper and thats will call the c code*/
	static jlong fibN(JNIEnv *env, jclass clazz,jlong n){
		return fib(n);
	}

	/* This table Maps what is gone known in java  to what we have in C
	 * map java type to a native type
	 * */
	static JNINativeMethod method_table[] = {
		{
				"fibN",/* this is how it known in java*/
				"(J)J",/* our fibN in java takes input a long and output as long*/
				(void*)fibN /* pointer to c implementation*/
		}
	};

	using namespace com_marakana_android_fibonacciservice;

	/* system callback. get called at init time */
	extern "C" jint JNI_OnLoad(JavaVM* vm, void* reserved) {
	    JNIEnv* env;
	    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
	        return JNI_ERR;
	    } else {
	    	/* for this java library,  */
	        jclass clazz = env->FindClass("com/marakana/android/fibonacciservice/Fiblib");
	        if (clazz) {
	                jint ret = env->RegisterNatives(clazz, method_table, sizeof(method_table) /sizeof(method_table[0]));
	                env->DeleteLocalRef(clazz);
	                return ret == 0 ? JNI_VERSION_1_6 : JNI_ERR;
	        } else {
	                return JNI_ERR;
	        }
	    }
	}
}
