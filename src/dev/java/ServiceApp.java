package dsn.dev.java;

public abstract class ServiceApp {
    public ServiceApp()
    {
        _started = false;
    }
    
    public abstract ErrorCode Start(String[] args);

    public abstract void Stop(boolean cleanup);

    public boolean IsStarted() { return _started; }

    public RpcAddress PrimaryAddress() { return _address; }

    public String Name() { return _name; }

    private boolean       _started;
    private RpcAddress    _address;
    private String        _name;

    private static int AppCreate(String class_name) throws InstantiationException, IllegalAccessException, ClassNotFoundException
    {
    	Class<?> clazz = Class.forName(class_name);
    	Object app = (Object) clazz.newInstance();
    	int index = GlobalInterOpLookupTable.Put(app);
        return index;
    }

    private static int AppStart(int index, String[] argv)
    {
        ServiceApp sapp = (ServiceApp) GlobalInterOpLookupTable.Get(index);
        int r = sapp.Start(argv).Code();
        if (r == 0)
        {
            sapp._started = true;
            sapp._address.addr = Nativecalls.dsn_primary_address2();
            sapp._name = argv[0];
        }
        return r;
    }

    private static void AppDestroy(int index, boolean cleanup)
    {
        ServiceApp sapp = (ServiceApp) GlobalInterOpLookupTable.Get(index);
        GlobalInterOpLookupTable.GetRelease(index);
        sapp.Stop(cleanup);
        sapp._started = false;
    }

    public static void RegisterApp(String type_name)
    {
        Nativecalls.dsn_register_app_role_managed(type_name);
    }
}
