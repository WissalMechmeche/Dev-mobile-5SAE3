package com.example.blogapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class BlogFragment extends Fragment {

    private RecyclerView recyclerView;
    private BlogAdapter blogAdapter;
    private List<Blog> blogList = new ArrayList<>();  // Your list of blogs
    private DatabaseHelper db;
    private Button addBlogButton;

    public BlogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blog, container, false);

        // Initialize DatabaseHelper and RecyclerView
        db = new DatabaseHelper(getContext());
        recyclerView = rootView.findViewById(R.id.blogRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get all blogs and set up the adapter
        blogList = db.getAllBlogs();
        blogAdapter = new BlogAdapter(blogList);
        recyclerView.setAdapter(blogAdapter);

        // Setup SearchView for filtering blogs
        SearchView searchView = rootView.findViewById(R.id.searchBlogView);
        searchView.setIconified(false);  // Expands the SearchView so it is not collapsed
        searchView.requestFocus();       // Ensures the keyboard opens for input
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                blogAdapter.filter(query); // Filter on query submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                blogAdapter.filter(newText); // Filter on text change
                return true;
            }
        });

        // Setup Add Blog button
        addBlogButton = rootView.findViewById(R.id.addBlogButton); // Ensure you have this button in your fragment's layout
        addBlogButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddBlogActivity.class);
            startActivityForResult(intent, 1); // Start AddBlogActivity and expect result
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK) {
            // Refresh the blog list after adding a new blog
            blogList.clear();
            blogList.addAll(db.getAllBlogs()); // Fetch updated list from database
            blogAdapter.notifyDataSetChanged(); // Notify adapter of changes
        }else if (requestCode == 2) { // Editing a blog
            Fragment fragment = getFragmentManager().findFragmentById(R.id.openBlogFragmentButton);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.remove(fragment); // Remove the fragment
            transaction.commit(); // Commit the transaction
            transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.openBlogFragmentButton, new BlogFragment()); // Replace with a new instance of the fragment
            transaction.commit();
            blogList.clear();
            blogList.addAll(db.getAllBlogs());
            blogAdapter.notifyDataSetChanged();
        }
    }
}
