package wieniacy.w.kaloszach.poznaj2.models;

import android.os.AsyncTask;

/**
 * Created by Robin on 20.04.2016.
 */
public class AsyncTaskConnectBase extends AsyncTask{

    public ConnectionClass ConnClass = null;


    @Override
    protected Object doInBackground(Object[] params) {
        ConnClass = new ConnectionClass();
        return null;
    }
}
