package dsn.dev.java;

import dsn.dev.java.utils.ErrorCode;

public abstract class AioCallbackHandler {
	public abstract void run(ErrorCode err, int size);
}
