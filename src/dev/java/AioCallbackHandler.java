package dsn.dev.java;

public abstract class AioCallbackHandler {
	public abstract void run(ErrorCode err, int size);
}
