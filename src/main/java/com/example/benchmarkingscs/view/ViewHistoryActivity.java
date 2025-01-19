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
import com.example.benchmarkingscs.model.ViewHistoryModel;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewHistoryActivity extends AppCompatActivity {

    private ListView historyListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);

        historyListView = findViewById(R.id.historyListView);

        loadBenchmarkHistory();
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {

            finish();
        });
    }

    private void loadBenchmarkHistory() {
        ArrayList<HashMap<String, String>> historyList = ViewHistoryModel.loadBenchmarkHistory(this);

        if (historyList.isEmpty()) {
            Toast.makeText(this, "No benchmark history available.", Toast.LENGTH_SHORT).show();
            return;
        }


        SimpleAdapter adapter = new SimpleAdapter(this, historyList,
                android.R.layout.simple_list_item_2,
                new String[]{"timestamp", "score"},
                new int[]{android.R.id.text1, android.R.id.text2});

        historyListView.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ViewHistoryActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);


        super.onBackPressed();
        finish();
    }

}
