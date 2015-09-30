package dsn.app.echo;
import dsn.dev.java.*;

import java.io.IOException;
import java.util.*;

public class EchoServiceClient extends Clientlet
{
    public EchoServiceClient() { }
	public EchoServiceClient(RpcAddress server) { _server = server; }
	
	public ResponseMessage ping(
        String val,
        int timeout_milliseconds, 
        int hash,
        RpcAddress server)
    {
        RpcWriteStream s = new RpcWriteStream(EchoHelper.RPC_ECHO_ECHO_PING, timeout_milliseconds, hash);
        try {
			s.WriteLine(val);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        s.Flush();
        
        RpcReadStream respStream = RpcCallSync(server != null ? server : _server, s);
        if (null != respStream)
        {
        	try {
				return new ResponseMessage(respStream.ReadString(), ErrorCode.ERR_OK);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }
        return new ResponseMessage(null, ErrorCode.ERR_TIMEOUT);
    }
    
	public class pingResponseHandler extends RpcResponseCallbackHandler
	{
		pingResponseHandler(pingCallback callback)
		{
			_callback = callback;
		}
		
		public void run(ErrorCode err, RpcReadStream rms)
		{
            String resp = null;
            try {
				resp = rms.ReadString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            _callback.run(err, resp);
		}
		pingCallback _callback;
	}
	
	public void ping(
        String val, 
        pingCallback callback,
        int timeout_milliseconds, 
        int reply_hash,
        int request_hash,
        RpcAddress server)
    {
        RpcWriteStream s = new RpcWriteStream(EchoHelper.RPC_ECHO_ECHO_PING,timeout_milliseconds, request_hash);
        try {
			s.WriteLine(val);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        s.Flush();
        
        RpcCallAsync(server != null ? server : _server, s, this, new pingResponseHandler(callback) , reply_hash);
    }
	
	public TaskHandle ping2(
	        String val, 
	        pingCallback callback,
	        int timeout_milliseconds, 
	        int reply_hash,
	        int request_hash,
	        RpcAddress server)
	    {
	        RpcWriteStream s = new RpcWriteStream(EchoHelper.RPC_ECHO_ECHO_PING,timeout_milliseconds, request_hash);
	        try {
				s.WriteLine(val);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        s.Flush();
	        
	        return RpcCallAsync2(server != null ? server : _server, s, this, new pingResponseHandler(callback) , reply_hash);
	    }       

    private RpcAddress _server;
}
