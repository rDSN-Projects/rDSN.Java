package dsn.app.echo;
import dsn.dev.java.*;

public class EchoServerApp extends ServiceApp{
	
	public ErrorCode Start(String[] args)
    {
        _server = new EchoServiceServer();
        _server.OpenService();
        return ErrorCode.ERR_OK;
    }

    public void Stop(boolean cleanup)
    {
        _server.CloseService();
        _server.Unregister();
    }

    private EchoServiceServer _server;
}
