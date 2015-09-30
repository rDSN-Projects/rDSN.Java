package dsn.app.echo;
import dsn.dev.java.*;
import java.util.ArrayList;
import java.util.Arrays;


public class echo {
    
    public static void main(String[] args) throws ClassNotFoundException
    {
    	System.loadLibrary("dsn.dev.java_helper");
        EchoHelper.InitCodes();

        ServiceApp.RegisterApp("dsn.app.echo.EchoClientApp");
        ServiceApp.RegisterApp("dsn.app.echo.EchoServerApp");

        ArrayList<String> argsList = new ArrayList<String>(Arrays.asList(args));
        argsList.add(0, (new String("echo.exe")));
        String[] args2 = (String[])argsList.toArray(new String[0]);
        Nativecalls.dsn_run(args2.length, args2, true);
    }
}
