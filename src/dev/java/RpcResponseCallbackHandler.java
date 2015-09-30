package dsn.dev.java;

public abstract class RpcResponseCallbackHandler {
	public abstract void run(ErrorCode err, RpcReadStream rms);
}
