package dsn.dev.java;

import dsn.dev.java.utils.*;

public abstract class RpcResponseCallbackHandler {
	public abstract void run(ErrorCode err, RpcReadStream rms);
}
