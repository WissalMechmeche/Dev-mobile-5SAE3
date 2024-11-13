package com.example.blogapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddBlogActivity extends AppCompatActivity {

    private EditText titleEditText, contentEditText, authorEditText, dateEditText;
    private Button saveButton;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blog); // Set the layout for AddBlogActivity

        db = new DatabaseHelper(this);

        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);
        authorEditText = findViewById(R.id.authorEditText);
        dateEditText = findViewById(R.id.dateEditText);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String content = contentEditText.getText().toString();
            String author = authorEditText.getText().toString();
            String date = dateEditText.getText().toString();

            if (!title.isEmpty() && !content.isEmpty() && !author.isEmpty() && !date.isEmpty()) {
                // Save the blog
                Blog newBlog = new Blog(title, content, author, date);
                db.addBlog(newBlog);

                // Notify the user
                Toast.makeText(AddBlogActivity.this, "Blog Added Successfully", Toast.LENGTH_SHORT).show();

                // Return the result to BlogFragment
                setResult(RESULT_OK);
                finish(); // Close AddBlogActivity
            } else {
                Toast.makeText(AddBlogActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
