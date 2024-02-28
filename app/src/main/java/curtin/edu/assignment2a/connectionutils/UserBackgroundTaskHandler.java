package curtin.edu.assignment2a.connectionutils;

import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import curtin.edu.assignment2a.model.User;
import curtin.edu.assignment2a.userrecyclerview.UserAdapter;

public class UserBackgroundTaskHandler implements Runnable {
    String url;
    Activity uiActivity;
    ProgressBar progressBar;
    UserAdapter adapter;

    public UserBackgroundTaskHandler(String url, Activity uiActivity, ProgressBar progressBar, UserAdapter adapter) {
        this.url = url;
        this.uiActivity = uiActivity;
        this.progressBar = progressBar;
        this.adapter = adapter;
    }

    @Override
    public void run() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        DataRetrievalTask dataRetrievalTask = new DataRetrievalTask(url, uiActivity);
        Future<String> dataStringPlaceholder = executorService.submit(dataRetrievalTask);
        String dataResult = waitingForData(dataStringPlaceholder);

        if(dataResult != null) {
            List<User> userList = convertResultDataToUserList(dataResult);
            if(userList != null) {
                uiActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // If everything has worked correctly, the RecyclerView data is set
                        adapter.setData(userList);
                    }
                });
            }
        }

    }

    private List<User> convertResultDataToUserList(String dataResult) {
        List<User> userList;
        JsonElement dataElement = new JsonParser().parse(dataResult);
        JsonArray userDataJsonArray = dataElement.getAsJsonArray();

        if(userDataJsonArray == null){
            return null;
        }

        userList = new ArrayList<>();

        // The JsonArray needs to be iterated through to retrieve the data for each user object in order to add it to the userList
        for (int i = 0; i < userDataJsonArray.size(); i++) {
            // Grab a single instance of a User object from the JsonArray
            JsonObject userJsonObject = userDataJsonArray.get(i).getAsJsonObject();

            // Once the User object has been grabbed, individual data can be selected from the object
            int id = userJsonObject.get("id").getAsInt();
            String name = userJsonObject.get("name").getAsString();
            String username = userJsonObject.get("username").getAsString();
            String email = userJsonObject.get("email").getAsString();
            String phone = userJsonObject.get("phone").getAsString();
            String website = userJsonObject.get("website").getAsString();

            // The user can be added to the list now that details have been retrieved.
            userList.add(new User(id, name, username, email, phone, website));
        }
        return userList;
    }

    private String waitingForData(Future<String> dataStringPlaceholder) {
        uiActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        String responseData = null;
        try {
            responseData = dataStringPlaceholder.get(6000, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            e.printStackTrace();
            showError(1, "Data String Retrieval");
        } catch (InterruptedException e) {
            e.printStackTrace();
            showError(2, "Data String Retrieval");
        } catch (TimeoutException e) {
            e.printStackTrace();
            showError(3, "Data String Retrieval");
        }
        uiActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        return responseData;
    }

    private void showError(int code, String errorType) {
        switch (code) {
            case 1:
                showToast(errorType + " Task Execution Error");
                break;
            case 2:
                showToast(errorType + " Task Interruption Error");
                break;
            case 3:
                showToast(errorType + " Task Timeout Error");
                break;
        }
    }

    private void showToast(String text) {
        uiActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(uiActivity, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
