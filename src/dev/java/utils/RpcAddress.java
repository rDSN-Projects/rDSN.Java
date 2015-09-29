package dsn.dev.java.utils;
import dsn.dev.java.Nativecalls;

public class RpcAddress {
	public RpcAddress(byte[] ad)
    {
        addr = ad;
    }

    public RpcAddress(String host, short port)
    {
        addr = Nativecalls.dsn_address_build(host, port);
    }

    public byte[] Addr()
    {
        return addr;
    }

    public byte[] addr;
}
