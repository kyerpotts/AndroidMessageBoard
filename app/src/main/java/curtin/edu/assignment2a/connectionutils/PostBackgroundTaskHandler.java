package curtin.edu.assignment2a.connectionutils;

import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import curtin.edu.assignment2a.model.Post;
import curtin.edu.assignment2a.postsrecyclerview.PostsAdapter;

public class PostBackgroundTaskHandler implements Runnable {
    String url;
    Activity uiActivity;
    ProgressBar progressBar;
    PostsAdapter adapter;

    public PostBackgroundTaskHandler(String url, Activity uiActivity, ProgressBar progressBar, PostsAdapter adapter) {
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

        if (dataResult != null) {
            List<Post> postList = convertResultDataToPostList(dataResult);
            if (postList != null) {
                uiActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // If everything has worked correctly, the RecyclerView data is set
                        adapter.setData(postList);
                    }
                });
            }
        }
    }

    private List<Post> convertResultDataToPostList(String dataResult) {
        Gson gson = new Gson();
        List<Post> postList = gson.fromJson(dataResult, new TypeToken<List<Post>>() {
        }.getType());

        return postList;
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
