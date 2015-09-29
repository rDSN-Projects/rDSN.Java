package dsn.dev.java;

import java.io.*;
import dsn.dev.java.utils.*;

public class RpcWriteStream extends OutputStream {

    private byte[] _buffer;
    private int _offset;
    private int _length;
	protected long _msg;
    
	public RpcWriteStream(TaskCode code, int timeoutMilliseconds, int hash)
	{
		Nativecalls.dsn_random32(0, 1000);
		_msg = Nativecalls.dsn_msg_create_request(code.Code(), timeoutMilliseconds, hash);
		_offset = 0;
	    _length = 256;
	    _buffer = new byte[_length];
	}
	
	public RpcWriteStream(long msg, boolean owner, int minSize)
	{
		_msg = msg;
		_offset = 0;
	    _length = minSize;
	    _buffer = new byte[_length];
	}
	
    public void Flush()
    {
        if (_offset > 0)
        {
            Nativecalls.dsn_msg_write(_msg, _buffer, _offset, _length);
        }
        _offset = 0;
    }

    public boolean IsFlushed()
    {
        return _offset == 0;
    }

    public void write(byte[] buffer, int offset, int count)
    {
        while (count > 0)
        {
            if (count + _offset > _length)
            {
                Flush();
            }

            int cp = count > (_length - _offset) ? (_length - _offset) : count;

            System.arraycopy(buffer, offset, _buffer, _offset, cp);

            offset += cp;
            count -= cp;
            _offset += cp;
        }
    }
    
	public void write(int s)
	{
		if (_offset >= _length)
        {
            Flush();
        }
		_buffer[_offset++] = (byte)s;
		return;
	}
	
	public long handle()
	{
		return _msg;
	}
	
	public void WriteLong(long val) throws IOException
	{
		DataOutputStream ostream = new DataOutputStream(this);
		ostream.writeLong(val);
	}
	
	public void WriteInt(int val) throws IOException
	{
		DataOutputStream ostream = new DataOutputStream(this);
		ostream.writeInt(val);
	}
	
	public void WriteDouble(double val) throws IOException
	{
		DataOutputStream ostream = new DataOutputStream(this);
		ostream.writeDouble(val);
	}

	public void WriteFloat(float val) throws IOException
	{
		DataOutputStream ostream = new DataOutputStream(this);
		ostream.writeFloat(val);
	}

	public void WriteShort(short val) throws IOException
	{
		DataOutputStream ostream = new DataOutputStream(this);
		ostream.writeShort(val);
	}

	public void WriteByte(byte val) throws IOException
	{
		DataOutputStream ostream = new DataOutputStream(this);
		ostream.writeByte(val);
	}
	
	public void WriteBoolean(boolean val) throws IOException
	{
		DataOutputStream ostream = new DataOutputStream(this);
		ostream.writeBoolean(val);
	}

	public void WriteLine(String val) throws IOException
	{
		String tmp = val + '\0';
		byte b[] = tmp.getBytes();
		write(b, 0, b.length);
	}
	
	public static void WriteLong(RpcWriteStream wms, long val) throws IOException
	{
		DataOutputStream ostream = new DataOutputStream(wms);
		ostream.writeLong(val);
	}
	
	public static void WriteInt(RpcWriteStream wms, int val) throws IOException
	{
		DataOutputStream ostream = new DataOutputStream(wms);
		ostream.writeInt(val);
	}
	
	public static void WriteDouble(RpcWriteStream wms, double val) throws IOException
	{
		DataOutputStream ostream = new DataOutputStream(wms);
		ostream.writeDouble(val);
	}

	public static void WriteFloat(RpcWriteStream wms, float val) throws IOException
	{
		DataOutputStream ostream = new DataOutputStream(wms);
		ostream.writeFloat(val);
	}

	public static void WriteShort(RpcWriteStream wms, short val) throws IOException
	{
		DataOutputStream ostream = new DataOutputStream(wms);
		ostream.writeShort(val);
	}

	public static void WriteByte(RpcWriteStream wms, byte val) throws IOException
	{
		DataOutputStream ostream = new DataOutputStream(wms);
		ostream.writeByte(val);
	}
	
	public static void WriteBoolean(RpcWriteStream wms, boolean val) throws IOException
	{
		DataOutputStream ostream = new DataOutputStream(wms);
		ostream.writeBoolean(val);
	}

	public static void WriteLine(RpcWriteStream wms, String val) throws IOException
	{
		String tmp = val + '\0';
		byte b[] = tmp.getBytes();
		wms.write(b, 0, b.length);
	}
}
