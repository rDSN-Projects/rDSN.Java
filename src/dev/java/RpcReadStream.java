package dsn.dev.java;

import java.io.*;

public class RpcReadStream extends InputStream{
	
    private byte _buffer[];
    private int _pos;
    private int _length;
	protected long _msg;
    
	public RpcReadStream(long msg, boolean owner)
	{
		_buffer = Nativecalls.dsn_msg_read(msg);
		_length = _buffer.length;
	    _pos = 0;
	}
	
	public int read() throws IOException
	{
		if(_pos >= _length)
			throw new EOFException();
		return ((int)_buffer[_pos++]) & 0xff;
	}
	
	public int read(byte[] buffer, int offset, int count)
    {
        count = (_pos + count > _length) ? (_length - _pos) : count;
        System.arraycopy(_buffer, _pos, buffer, offset, count);
        _pos += count;
        return count;
    }

	public long handle()
	{
		return _msg;
	}

	public long ReadLong() throws IOException
	{
		DataInputStream istream = new DataInputStream(this);
		return istream.readLong();
	}
	
	public int ReadInt() throws IOException
	{
		DataInputStream istream = new DataInputStream(this);
		return istream.readInt();
	}
	
	public double ReadDouble() throws IOException
	{
		DataInputStream istream = new DataInputStream(this);
		return istream.readDouble();
	}

	public float ReadFloat() throws IOException
	{
		DataInputStream istream = new DataInputStream(this);
		return istream.readFloat();
	}

	public short ReadShort() throws IOException
	{
		DataInputStream istream = new DataInputStream(this);
		return istream.readShort();
	}

	public byte ReadByte() throws IOException
	{
		DataInputStream istream = new DataInputStream(this);
		return istream.readByte();
	}
	
	public boolean ReadBoolean() throws IOException
	{
		DataInputStream istream = new DataInputStream(this);
		return istream.readBoolean();
	}

	public String ReadString() throws IOException
	{
		int count = 0;
		byte buffer[];
		while((_buffer[_pos + count] == 0) && (_pos + count < _length)) count++;
		while((_buffer[_pos + count] != 0) && (_pos + count < _length)) count++;
		buffer = new byte[count + 1];
		System.arraycopy(_buffer, _pos, buffer, 0, count);
		_pos += count;
		return new String(buffer);
	}
	
	public static long ReadLong(RpcReadStream rms) throws IOException
	{
		DataInputStream istream = new DataInputStream(rms);
		return istream.readLong();
	}
	
	public static int ReadInt(RpcReadStream rms) throws IOException
	{
		DataInputStream istream = new DataInputStream(rms);
		return istream.readInt();
	}
	
	public static double ReadDouble(RpcReadStream rms) throws IOException
	{
		DataInputStream istream = new DataInputStream(rms);
		return istream.readDouble();
	}

	public static float ReadFloat(RpcReadStream rms) throws IOException
	{
		DataInputStream istream = new DataInputStream(rms);
		return istream.readFloat();
	}

	public static short ReadShort(RpcReadStream rms) throws IOException
	{
		DataInputStream istream = new DataInputStream(rms);
		return istream.readShort();
	}

	public static byte ReadByte(RpcReadStream rms) throws IOException
	{
		DataInputStream istream = new DataInputStream(rms);
		return istream.readByte();
	}
	
	public static boolean ReadBoolean(RpcReadStream rms) throws IOException
	{
		DataInputStream istream = new DataInputStream(rms);
		return istream.readBoolean();
	}

	public static String ReadString(RpcReadStream rms) throws IOException
	{
		return rms.ReadString();
	}
}
