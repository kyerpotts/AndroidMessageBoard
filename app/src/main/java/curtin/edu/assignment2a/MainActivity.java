package curtin.edu.assignment2a;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import curtin.edu.assignment2a.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding;
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}