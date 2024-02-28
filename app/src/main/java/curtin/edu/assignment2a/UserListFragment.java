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

import curtin.edu.assignment2a.connectionutils.UserBackgroundTaskHandler;
import curtin.edu.assignment2a.model.User;
import curtin.edu.assignment2a.userrecyclerview.UserAdapter;

public class UserListFragment extends Fragment {

    public UserListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        // Set up connection info and threading objects
        String url = "https://jsonplaceholder.typicode.com/users";
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // Set up progress bar
        ProgressBar progressBar = view.findViewById(R.id.userListProgressBar);

        // Set up the recycler view
        RecyclerView rv = view.findViewById(R.id.rvUsers);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        UserAdapter adapter = new UserAdapter();

        // Collect the list of users from the api and set adapter data
        UserBackgroundTaskHandler userFragBackgroundWorker = new UserBackgroundTaskHandler(url, requireActivity(), progressBar, adapter);
        executorService.execute(userFragBackgroundWorker);

        rv.setAdapter(adapter);
        return view;
    }
}