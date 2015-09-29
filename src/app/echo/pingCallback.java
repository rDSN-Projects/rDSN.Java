package dsn.app.echo;

import dsn.dev.java.utils.ErrorCode;

public interface pingCallback {
	public void run(ErrorCode err, String resp);
}
