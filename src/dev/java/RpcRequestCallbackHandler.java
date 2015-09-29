package dsn.dev.java;

public class RpcRequestCallbackHandler {
	public RpcRequestCallbackHandler(RpcRequestHandler handler)
	{
		_handler = handler;
		_oneway = false;
	}
	
	public RpcRequestCallbackHandler(RpcRequestHandlerOneWay handler)
	{
		_handler = handler;
		_oneway = true;
	}
	
	public void callback(long req)
	{
        if(_oneway)
        {
        	RpcReadStream rms = new RpcReadStream(req, false);
        	((RpcRequestHandlerOneWay) _handler).run(rms);
        }
        else
        {
        	RpcReadStream rms = new RpcReadStream(req, false);
            RpcWriteStream wms = new RpcWriteStream(Nativecalls.dsn_msg_create_response(req), false, 256);
            ((RpcRequestHandler) _handler).run(rms, wms);    
        }
	}
	
	Object _handler;
	boolean _oneway;
}
