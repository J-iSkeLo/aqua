package l.chernenkiy.aqua.Helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {

    private final Context context;

    public ConnectionDetector(Context context){
        this.context = context;
    }

    public boolean ConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            for (NetworkInfo networkInfo : info)
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
        }
        return false;
    }
}