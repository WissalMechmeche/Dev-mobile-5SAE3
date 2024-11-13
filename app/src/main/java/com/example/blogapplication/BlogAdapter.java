package com.example.blogapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {

    private List<Blog> fullBlogList;  // Original list of all blogs
    private List<Blog> filteredBlogList;  // List of blogs after applying the search filter
    private Context context;

    public BlogAdapter(List<Blog> blogList, Context context) {
        this.fullBlogList = blogList;
        this.filteredBlogList = new ArrayList<>(blogList); // Initialize filtered list with all blogs
        this.context = context;
    }
    public BlogAdapter(List<Blog> blogList) {
        this.fullBlogList = blogList;
        this.filteredBlogList = new ArrayList<>(blogList); // Initialize filtered list with all blogs
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_item, parent, false);
        context = parent.getContext(); // Initialize context if not set
        return new BlogViewHolder(itemView);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
        Blog blog = filteredBlogList.get(position);
        holder.titleTextView.setText(blog.getTitle());
        holder.contentTextView.setText(blog.getContent());

        // Handle edit button click
        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditBlogActivity.class);
            intent.putExtra("blogId", blog.getId());  // Pass blog ID
            intent.putExtra("title", blog.getTitle());
            intent.putExtra("content", blog.getContent());
            intent.putExtra("author", blog.getAuthor());
            intent.putExtra("date", blog.getDate());
            ((Activity) context).startActivityForResult(intent, 2);

        });

    }

    @Override
    public int getItemCount() {
        return filteredBlogList.size();
    }

    // Method to filter the blog list based on a search query
    public void filter(String query) {
        if (query == null || query.isEmpty()) {
            filteredBlogList = new ArrayList<>(fullBlogList); // Show all items if query is empty
        } else {
            List<Blog> filtered = new ArrayList<>();
            for (Blog blog : fullBlogList) {
                if (blog.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        blog.getContent().toLowerCase().contains(query.toLowerCase())) {
                    filtered.add(blog);
                }
            }
            filteredBlogList = filtered;  // Update the filtered list
        }
        notifyDataSetChanged();  // Notify RecyclerView to refresh displayed data
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView contentTextView;
        Button editButton;

        public BlogViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            editButton = itemView.findViewById(R.id.editButton); // Reference to the editButton in the item layout
        }
    }
}
