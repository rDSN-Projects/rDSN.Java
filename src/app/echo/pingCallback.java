package dsn.app.echo;

import dsn.dev.java.*;

public interface pingCallback {
	public void run(ErrorCode err, String resp);
}
