package dsn.dev.java;

public class RpcAddress {
	public RpcAddress(long ad)
    {
        addr = ad;
    }

    public RpcAddress(String host, short port)
    {
        addr = Nativecalls.dsn_address_build(host, port);
    }

    public long Addr()
    {
        return addr;
    }

    public long addr;
}
