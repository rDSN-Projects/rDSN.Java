package dsn.app.echo;
import java.io.IOException;

import dsn.dev.java.*;

public class EchoServiceServer extends Serverlet
{
	public EchoServiceServer()
	{
		super("echo.server", 13);
	}
	
	public class Onping extends RpcRequestHandler
	{
		public void run(RpcReadStream request, RpcWriteStream response)
		{
			String val = null;
            
            try 
            {
            	System.out.println("... exec RPC_ECHO_ECHO_PING ... (not implemented) ");
            	val = request.ReadString();
            	response.WriteLine(val);
            	response.Flush();
            	request.close();
            	response.close();
            } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    Reply(response);
		}
	}
	
	public void OpenService()
	{
	    RegisterRpcHandler(EchoHelper.RPC_ECHO_ECHO_PING, "ping", new Onping());
	}
	
	public void CloseService()
	{
	    UnregisterRpcHandler(EchoHelper.RPC_ECHO_ECHO_PING);
	}
}
