package dsn.dev.java;

public abstract class RpcRequestHandler {
	public abstract void run(RpcReadStream rms, RpcWriteStream wms);
}
