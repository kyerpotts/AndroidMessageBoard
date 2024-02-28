package curtin.edu.assignment2a.connectionutils;

import android.app.Activity;
import android.net.Uri;

import java.net.HttpURLConnection;
import java.util.concurrent.Callable;

public class DataRetrievalTask implements Callable<String> {

    private final String baseURL;
    private final RemoteUtilities remoteUtilities;

    public DataRetrievalTask(String baseURL, Activity uiActivity) {
        this.baseURL = baseURL;
        remoteUtilities = RemoteUtilities.getInstance(uiActivity);
    }

    @Override
    public String call() {
        String response = null;
        String endpoint = getDataRetrievalEndpoint();
        HttpURLConnection connection = remoteUtilities.openConnection(endpoint);
        if (connection != null) {
            if (remoteUtilities.isConnectionOkay(connection)) {
                response = remoteUtilities.getResponseString(connection);
                connection.disconnect();
            }
        }
        return response;
    }

    private String getDataRetrievalEndpoint() {
        String data = null;
        Uri.Builder url = Uri.parse(this.baseURL).buildUpon();
        return url.build().toString();
    }
}
