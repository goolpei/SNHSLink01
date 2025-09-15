package com.example.snhslink.StudentInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.snhslink.BaseActivity;
import com.example.snhslink.R;

public class IntroPage extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.page_intro);

        // Reference to the TextView
        TextView appearingTextView = findViewById(R.id.appearing_textview);

        // Initially set the TextView to invisible
        appearingTextView.setAlpha(0f);

        // Gradually fade in the text
        appearingTextView.animate().alpha(1f).setDuration(2500);

        // Set a click listener for the root layout to transition to the next activity
        View rootLayout = findViewById(R.id.main);
        rootLayout.setOnClickListener(v -> {
            // Navigate to ChooseStudentOrAdminActivity
            Intent intent = new Intent(IntroPage.this, ChooseStudentOrAdminActivity.class);
            startActivity(intent);
        });
    }
}
