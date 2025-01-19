package com.example.benchmarkingscs.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.benchmarkingscs.MainActivity;
import com.example.benchmarkingscs.R;
import com.example.benchmarkingscs.model.ViewResultsModel;

import java.util.ArrayList;
import java.util.HashMap;

public class                                                                                                                                                                ViewResultsActivity extends AppCompatActivity {

    private ListView resultsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_results);

        // Initialize the ListView
        resultsListView = findViewById(R.id.resultsListView);

        // Load and display total benchmark scores
        loadTotalScores();

        // Set up the Back button
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ViewResultsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void loadTotalScores() {
        ArrayList<HashMap<String, String>> resultsList = ViewResultsModel.loadTotalScores(this);

        if (resultsList.isEmpty()) {
            Toast.makeText(this, "No total benchmark results available.", Toast.LENGTH_SHORT).show();
            return;
        }


        SimpleAdapter adapter = new SimpleAdapter(this, resultsList,
                android.R.layout.simple_list_item_2,
                new String[]{"timestamp", "totalScore"},
                new int[]{android.R.id.text1, android.R.id.text2});

        resultsListView.setAdapter(adapter);
    }
}
