package curtin.edu.assignment2a;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import curtin.edu.assignment2a.connectionutils.PostBackgroundTaskHandler;
import curtin.edu.assignment2a.connectionutils.UserBackgroundTaskHandler;
import curtin.edu.assignment2a.model.Post;
import curtin.edu.assignment2a.postsrecyclerview.PostsAdapter;

public class UserPostsFragment extends Fragment {
    ProgressBar progressBar;

    public UserPostsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_posts, container, false);
        // Get id of selected user. This will allow us to search for posts made by the user by querying the api.
        int args = UserPostsFragmentArgs.fromBundle(getArguments()).getUserId();

        // Set up connection info and threading objects
        String url = "https://jsonplaceholder.typicode.com/users/" + args + "/posts";
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // Set up progress bar
        ProgressBar progressBar = view.findViewById(R.id.postListProgressBar);

        // Set up the recycler view
        RecyclerView rv = view.findViewById(R.id.rvPosts);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        PostsAdapter adapter = new PostsAdapter();

        // Collect the list of users from the api and set adapter data
        PostBackgroundTaskHandler postFragBackgroundWorker = new PostBackgroundTaskHandler(url, requireActivity(), progressBar, adapter);
        executorService.execute(postFragBackgroundWorker);

        rv.setAdapter(adapter);
        return view;
    }
}