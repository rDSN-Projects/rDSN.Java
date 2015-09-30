package dsn.app.echo;

import dsn.dev.java.*;

public class ResponseMessage {
	ResponseMessage() {}
	ResponseMessage(String resp, ErrorCode err)
	{
		_resp = resp;
		_err = err;
	}
	
	public String _resp;
	public ErrorCode _err;
}
