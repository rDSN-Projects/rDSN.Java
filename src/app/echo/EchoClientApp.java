package dsn.app.echo;
import dsn.dev.java.*;
import dsn.dev.java.utils.*;

public class EchoClientApp extends ServiceApp {
    
    public ErrorCode Start(String[] args)
    {
    	if (args.length < 3)
        {
            System.out.println("wrong usage: server-host server-port");
            return ErrorCode.ERR_INVALID_PARAMETERS;
        }
    	
    	_server = new RpcAddress(Nativecalls.dsn_address_build(args[1], Short.parseShort(args[2])));
    	_client = new EchoServiceClient(_server);
        _timer = Clientlet.CallAsync2(EchoHelper.LPC_ECHO_TEST_TIMER, null, new OnTestTimer(), 0, 0, 1000);
        return ErrorCode.ERR_OK;
    }

    public void Stop(boolean cleanup)
    {
    	_timer.Cancel(true);
    	_client.ReleaseHandle();
    	_client = null;
    }
    
    
    private class OnTestTimer extends TaskCallbackHandler
    {
    	public void run()
        {
            String req = "PING";
            //sync:
            System.out.println("call RPC_ECHO_ECHO_PING begin");
            ResponseMessage rm = _client.ping(req, 0, 0, null);
            System.out.println("call RPC_ECHO_ECHO_PING end, return " + rm._err.ToString());
            //async: 
            // TODO:
       
        }
    }
    
    private TaskHandle _timer;
    private RpcAddress        _server;
    private EchoServiceClient _client;
}
