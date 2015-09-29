package dsn.app.echo;

import dsn.dev.java.Nativecalls.*;
import dsn.dev.java.utils.*;

public class EchoHelper {
    public static TaskCode RPC_ECHO_ECHO_PING;
    public static TaskCode LPC_ECHO_TEST_TIMER;
    

    public static void InitCodes()
    {
        RPC_ECHO_ECHO_PING = new TaskCode("RPC_ECHO_ECHO_PING", dsn_task_type_t.TASK_TYPE_RPC_REQUEST, dsn_task_priority_t.TASK_PRIORITY_COMMON, ThreadPoolCode.THREAD_POOL_DEFAULT);
        LPC_ECHO_TEST_TIMER = new TaskCode("LPC_ECHO_TEST_TIMER", dsn_task_type_t.TASK_TYPE_COMPUTE, dsn_task_priority_t.TASK_PRIORITY_COMMON, ThreadPoolCode.THREAD_POOL_DEFAULT);
    }
}
