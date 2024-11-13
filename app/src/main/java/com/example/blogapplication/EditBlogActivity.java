package com.example.blogapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class EditBlogActivity extends AppCompatActivity {

    private EditText titleEditText, contentEditText, authorEditText, dateEditText;
    private Button saveButton;
    private DatabaseHelper databaseHelper;
    private String blogId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_blog);

        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);
        authorEditText = findViewById(R.id.authorEditText);
        dateEditText = findViewById(R.id.dateEditText);
        saveButton = findViewById(R.id.saveButton);

        databaseHelper = new DatabaseHelper(this);

        // Get the blog ID passed from the intent
        blogId = getIntent().getStringExtra("blogId");

        // Load the blog data into the fields
        loadBlogDetails(blogId);

        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String content = contentEditText.getText().toString();
            String author = authorEditText.getText().toString();
            String date = dateEditText.getText().toString();

            Blog blog = new Blog(title, content, author, date);
            databaseHelper.updateBlog(blogId, blog);  // Update the blog in the database
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);  // Set the result as OK
            finish(); // Close the activity and return to the blog list

        });
    }

    private void loadBlogDetails(String blogId) {
        // Fetch the blog from the database using the ID
        Blog blog = databaseHelper.getBlog(blogId);
        if (blog != null) {
            titleEditText.setText(blog.getTitle());
            contentEditText.setText(blog.getContent());
            authorEditText.setText(blog.getAuthor());
            dateEditText.setText(blog.getDate());
        }
    }
}
