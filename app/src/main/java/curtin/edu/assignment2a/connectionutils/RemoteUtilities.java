package curtin.edu.assignment2a.connectionutils;

import android.app.Activity;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RemoteUtilities {

    public static RemoteUtilities remoteUtilities = null;
    private Activity uiActivity;

    public RemoteUtilities(Activity uiActivity) {
        this.uiActivity = uiActivity;
    }

    public void setUiActivity(Activity uiActivity) {
        this.uiActivity = uiActivity;
    }

    public static RemoteUtilities getInstance(Activity uiActivity) {
        if (remoteUtilities == null) {
            remoteUtilities = new RemoteUtilities(uiActivity);
        }
        remoteUtilities.setUiActivity(uiActivity);
        return remoteUtilities;
    }

    public HttpURLConnection openConnection(String urlString) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (connection == null) {
            uiActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(uiActivity, "Check Internet", Toast.LENGTH_LONG).show();
                }
            });
        }
        return connection;
    }

    public boolean isConnectionOkay(HttpURLConnection connection) {
        try {
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            uiActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(uiActivity, "Problem with API Endpoint", Toast.LENGTH_LONG).show();
                }
            });
        }
        return false;
    }

    public String getResponseString(HttpURLConnection connection) {
        String data = null;
        try {
            InputStream inputStream = connection.getInputStream();
            byte[] bytes = IOUtils.toByteArray(inputStream);
            data = new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
