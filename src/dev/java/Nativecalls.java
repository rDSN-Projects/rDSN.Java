package dsn.dev.java;

public class Nativecalls 
{
	public final int DSN_MAX_TASK_CODE_NAME_LENGTH  = 48 ;
    public final int DSN_MAX_ADDRESS_NAME_LENGTH    = 16 ;
    public final int DSN_MAX_BUFFER_COUNT_IN_MESSAGE= 64 ;
    public final int DSN_INVALID_HASH               = 0xdeadbeef ;
    public final int DSN_MAX_APP_TYPE_NAME_LENGTH   = 32 ;
    
    public class dsn_app_info
    {
        public Object app_context_ptr; // returned by dsn_app_create
        public int app_id;

        public String type; // size = DSN_MAX_APP_TYPE_NAME_LENGTH
        public String name; // size = DSN_MAX_APP_TYPE_NAME_LENGTH
    };

    public static enum dsn_task_type_t
    {
        TASK_TYPE_RPC_REQUEST,
        TASK_TYPE_RPC_RESPONSE,
        TASK_TYPE_COMPUTE,
        TASK_TYPE_AIO,
        TASK_TYPE_CONTINUATION,
        TASK_TYPE_COUNT,
        TASK_TYPE_INVALID,
    };

    public static enum dsn_task_priority_t
    {
        TASK_PRIORITY_LOW,
        TASK_PRIORITY_COMMON,
        TASK_PRIORITY_HIGH,
        TASK_PRIORITY_COUNT,
        TASK_PRIORITY_INVALID,
    };

    public static enum dsn_log_level_t
    {
        LOG_LEVEL_INFORMATION,
        LOG_LEVEL_DEBUG,
        LOG_LEVEL_WARNING,
        LOG_LEVEL_ERROR,
        LOG_LEVEL_FATAL,
        LOG_LEVEL_COUNT,
        LOG_LEVEL_INVALID
    };

    public enum dsn_host_type_t
    {
        HOST_TYPE_IPV4,  // 4 bytes
        HOST_TYPE_IPV6,  // 16 bytes
        HOST_TYPE_URI,   // customized bytes
        HOST_TYPE_COUNT,
        HOST_TYPE_INVALID
    };
    
    //------------------------native run-----------------------------------------
    public native static boolean       dsn_register_app_role_managed(String type_name);
    public native static boolean       dsn_mimic_app(String app_name, int index); 
    public native static void          dsn_run(int argc, String[] argv, boolean sleep_after_init);
    public native static void          dsn_run_config(String config, boolean sleep_after_init);
    
    //-----------------------------address--------------------------------------
    public native static long          dsn_address_build(String host, short port);
    public native static long          dsn_address_build_ipv4(int ipv4, short port);
    public native static long          dsn_address_build_uri(long uri);
    public native static long          dsn_address_build_group(long g);
    public native static long          dsn_uri_build(String url);
    public native static void          dsn_uri_destroy(long uri);
    public native static long          dsn_group_build(String name);
    public native static boolean       dsn_group_add(long g, long ep);
    public native static boolean       dsn_group_remove(long g, long ep);
    public native static void          dsn_group_set_leader(long g, long ep);
    public native static long          dsn_group_get_leader(long g);
    public native static boolean       dsn_group_is_leader(long g, long ep);
    public native static long          dsn_group_next(long g, long ep);
    public native static void          dsn_group_destroy(long g);
    public native static long          dsn_primary_address();
    public native static int           dsn_ipv4_from_host(String name);
    public native static int           dsn_ipv4_local(String network_interface);
    public native static String        dsn_address_to_string(long addr);
    
    //---------------------------time/rand-----------------------------------
    public native static long dsn_now_ns();
    public native static long dsn_random64(long min, long max);
    public static long dsn_now_us() { return dsn_now_ns() / 1000; }
    public static long dsn_now_ms() { return dsn_now_ns() / 1000000; }
    public static int dsn_random32(int min, int max) { return (int)(dsn_random64(min, max)); }
    public static double dsn_probability() { return (double)(dsn_random64(0, 1000000000)) / 1000000000.0; }
    
    //-------------------------------others------------------------------------
    public native static void           dsn_coredump();
    public native static int            dsn_crc32_compute(long ptr, long size, int init_crc);
    public native static int            dsn_crc32_concatenate(int xy_init, int x_init, int x_final, long x_size, int y_init, int y_final, long y_size);
    
    //------------------------------log------------------------------------------
    public native static void           dsn_log2(String file, String function, int line, int log_level, String title);
    public native static int            dsn_log_get_start_level2();
    public static void                  dsn_log(String file, String function, int line, dsn_log_level_t log_level, String title)
    {
    	dsn_log2(file, function, line,log_level.ordinal(), title);
    }
    public static dsn_log_level_t       dsn_log_get_start_level()
    {
    	int ret = dsn_log_get_start_level2();
    	return dsn_log_level_t.values()[ret];
    }
    
    //----------------------------utils---------------------------------------
    public native static int           	dsn_error_register(String name);
    public native static String         dsn_error_to_string(int err);    
    public native static int            dsn_threadpool_code_register(String name);
    public native static String         dsn_threadpool_code_to_string(int pool_code);
    public native static int            dsn_threadpool_code_from_string(String s, int default_code);
    public native static int            dsn_threadpool_code_max();
    public native static int            dsn_task_code_register2(String name, int type, int pri, int pool);
    public native static void           dsn_task_code_query2(int code, int[] type, int[] pri, int[] pool);
    public native static void           dsn_task_code_set_threadpool(int code, int pool);
    public native static void           dsn_task_code_set_priority2(int code, int pri);
    public native static String         dsn_task_code_to_string(int code);
    public native static int            dsn_task_code_from_string(String s, int default_code);
    public native static int            dsn_task_code_max();
    public native static String         dsn_task_type_to_string2(int type);
    public native static String         dsn_task_priority_to_string2(int pri);
    public static int                   dsn_task_code_register(String name, dsn_task_type_t type, dsn_task_priority_t pri, int pool)
    {
    	return dsn_task_code_register2(name, type.ordinal(), pri.ordinal(), pool);
    }
    public static void                  dsn_task_code_query(int code, dsn_task_type_t[] type, dsn_task_priority_t[] pri, int[] pool)
    {
    	type = new dsn_task_type_t[1];
    	pri = new dsn_task_priority_t[1];
    	pool = new int[1];
    	int[] tmptype = new int[1];
    	int[] tmppri = new int[1];
    	dsn_task_code_query2(code, tmptype, tmppri, pool);
    	type[1] = dsn_task_type_t.values()[tmptype[1]];
    	pri[1] = dsn_task_priority_t.values()[tmppri[1]];
    }
    public static void           dsn_task_code_set_priority(int code, dsn_task_priority_t pri)
    {
    	dsn_task_code_set_priority2(code, pri.ordinal());
    }
    public static String         dsn_task_type_to_string(dsn_task_type_t type)
    {
    	return dsn_task_type_to_string2(type.ordinal());
    }
    public static String         dsn_task_priority_to_string(dsn_task_priority_t pri)
    {
    	return dsn_task_priority_to_string2(pri.ordinal());
    }
    
    //----------------------------config------------------------------------

    public native static String         dsn_config_get_value_string(String section, String key, String default_value, String dsptr);
    public native static boolean        dsn_config_get_value_bool(String section, String key, boolean default_value, String dsptr);
    public native static long           dsn_config_get_value_uint64(String section, String key, long default_value, String dsptr);
    public native static double         dsn_config_get_value_double(String section, String key, double default_value, String dsptr);
    public native static int            dsn_config_get_all_keys2(String section, String[] buffers, int[] buffer_count); 
    public static int                   dsn_config_get_all_keys(String section, String[] buffers, int[] buffer_count)
    {
    	buffer_count = new int[1];
    	return dsn_config_get_all_keys2(section, buffers, buffer_count); 
    }
    
    //----------------------------semaphore-----------------------------
    public native static long           dsn_exlock_create(boolean recursive);
    public native static void           dsn_exlock_destroy(long l);
    public native static void           dsn_exlock_lock(long l);
    public native static boolean        dsn_exlock_try_lock(long l);
    public native static void           dsn_exlock_unlock(long l);
    public native static long           dsn_rwlock_nr_create();
    public native static void           dsn_rwlock_nr_destroy(long l);
    public native static void           dsn_rwlock_nr_lock_read(long l);
    public native static void           dsn_rwlock_nr_unlock_read(long l);
    public native static void           dsn_rwlock_nr_lock_write(long l);
    public native static void           dsn_rwlock_nr_unlock_write(long l);
    public native static long           dsn_semaphore_create(int initial_count);
    public native static void           dsn_semaphore_destroy(long s);
    public native static void           dsn_semaphore_signal(long s, int count);
    public native static void           dsn_semaphore_wait(long s);
    public native static boolean        dsn_semaphore_wait_timeout(long s, int timeout_milliseconds);
    
    //----------------------------task------------------------------------
    public native static long           dsn_task_create(int code, int pram, int hash);
    public native static long           dsn_task_create_timer(int code, int pram, int hash, int interval_milliseconds);
    public native static void           dsn_task_add_ref(long task);
    public native static void           dsn_task_release_ref(long task);
    public native static void           dsn_task_call(long task, long tracker, int delay_milliseconds);
    public native static boolean        dsn_task_cancel(long task, boolean wait_until_finished);
    public native static boolean        dsn_task_cancel3(long task, boolean wait_until_finished, boolean[] finished);
    public native static boolean        dsn_task_wait(long task); 
    public native static boolean        dsn_task_wait_timeout(long task, int timeout_milliseconds);
    public native static int            dsn_task_error(long task);
    public static boolean               dsn_task_cancel2(long task, boolean wait_until_finished, boolean[] finished)
    {
    	return dsn_task_cancel3(task, wait_until_finished, finished);
    }

    //------------------------tracker---------------------------------------
    public native static long          dsn_task_tracker_create(int task_bucket_count);
    public native static void          dsn_task_tracker_destroy(long handle);
    public native static void          dsn_task_tracker_wait_all(long handle);
    public native static void          dsn_task_tracker_cancel_all(long handle);

    //------------------------------rpc------------------------------------
    public native static long           dsn_msg_create_request(int rpc_code, int timeout_milliseconds, int hash);
    public native static long           dsn_msg_create_response(long request);
    public native static void           dsn_msg_add_ref(long msg);
    public native static void           dsn_msg_release_ref(long msg);
    public native static void           dsn_msg_update_request(long msg, int timeout_milliseconds, int hash);
    public native static int            dsn_msg_query_request2(long msg, int timeout_milliseconds, int[] phash);
    public native static byte[]         dsn_msg_read(long msg);
    public native static int            dsn_msg_write(long msg, byte[] buffer, int size, int min_size);
    public native static long           dsn_msg_body_size(long msg);
    public native static long           dsn_msg_rw_ptr(long msg, long offset_begin);
    public native static long           dsn_msg_from_address(long msg);
    public native static long           dsn_msg_to_address(long msg);
    public native static boolean        dsn_rpc_register_handler(int code, String name, int index);
    public native static void           dsn_rpc_unregiser_handler(int code); 
    public native static long           dsn_rpc_create_response_task(long request, int pram, int reply_hash);
    public native static void           dsn_rpc_call(long server, long task, long tracker);
    public native static long           dsn_rpc_call_wait(long server, long request);
    public native static void           dsn_rpc_call_one_way(long server, long request);
    public native static void           dsn_rpc_reply(long resp);
    public native static long           dsn_rpc_get_response(long rpc_call);
    public native static void           dsn_rpc_enqueue_response(long rpc_call, int err, long response);
    public static int                   dsn_msg_query_request(long msg, int timeout_milliseconds, int[] hash)
    {
    	hash = new int[1];
    	return dsn_msg_query_request2(msg, timeout_milliseconds, hash);
    }
    
    //----------------------------aio------------------------------------
    public native static long           dsn_file_open(String file_name, int flag, int pmode);
    public native static int            dsn_file_close(long file);
    public native static long           dsn_file_create_aio_task(int code, int index, int hash);
    public native static void           dsn_file_read(long file, byte[] buffer, int count, long offset, long cb, long tracker);
    public native static void           dsn_file_write(long file, byte[] buffer, int count, long offset, long cb, long tracker);
    public native static void           dsn_file_copy_remote_directory(long remote, String source_dir, String dest_dir, boolean overwrite, long cb, long tracker);
    public native static void           dsn_file_copy_remote_files(long remote, String source_dir, String[] source_files, String dest_dir, boolean overwrite, long cb, long tracker);
    public native static long           dsn_file_get_io_size(long cb_task);
    public native static void           dsn_file_task_enqueue(long cb_task, int err, long size);
    
}
