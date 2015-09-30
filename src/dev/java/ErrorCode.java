package dsn.dev.java;

public class ErrorCode {
	public static ErrorCode ERR_OK = new ErrorCode("ERR_OK");
    public static ErrorCode ERR_TIMEOUT = new ErrorCode("ERR_TIMEOUT");
    public static ErrorCode ERR_INVALID_PARAMETERS = new ErrorCode("ERR_INVALID_PARAMETERS");
    
    public ErrorCode(int err)
    {
        _error = err;
    }

    public ErrorCode(ErrorCode err)
    {
        _error = err._error;
    }

    public ErrorCode(String err)
    {
        _error = Nativecalls.dsn_error_register(err);
    }

    public int Code()
    {
        return _error;
    }

    public String ToString()
    {
        return Nativecalls.dsn_error_to_string(_error);
    }

    public static int Code(ErrorCode err)
    {
        return err._error;
    }
    
    public boolean Equals(Object obj)
    {
        return this._error == ((ErrorCode)obj)._error;
    }

    private int _error;
}
