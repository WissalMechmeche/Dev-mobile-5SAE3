package com.example.blogapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.blogapplication.Blog;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "blogDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Name
    private static final String TABLE_BLOGS = "blogs";

    // Column Names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_DATE = "date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BLOGS_TABLE = "CREATE TABLE " + TABLE_BLOGS + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_TITLE + " TEXT, "
                + KEY_CONTENT + " TEXT, "
                + KEY_AUTHOR + " TEXT, "
                + KEY_DATE + " TEXT)";
        db.execSQL(CREATE_BLOGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLOGS);
        onCreate(db);
    }

    // Method to add a blog
    public void addBlog(Blog blog) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, blog.getTitle());
        values.put(KEY_CONTENT, blog.getContent());
        values.put(KEY_AUTHOR, blog.getAuthor());
        values.put(KEY_DATE, blog.getDate());
        // Inserting the new row into the table
        db.insert(TABLE_BLOGS, null, values);
        db.close();
    }

    // Method to get a single blog based on its ID
    public Blog getBlog(String blogId) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to select the blog by its ID
        Cursor cursor = db.query(TABLE_BLOGS, null, KEY_ID + " = ?", new String[]{blogId},
                null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                // Safely retrieve column indexes
                int idIndex = cursor.getColumnIndex(KEY_ID);
                int titleIndex = cursor.getColumnIndex(KEY_TITLE);
                int contentIndex = cursor.getColumnIndex(KEY_CONTENT);
                int authorIndex = cursor.getColumnIndex(KEY_AUTHOR);
                int dateIndex = cursor.getColumnIndex(KEY_DATE);

                // Check if any of the indexes is -1 (invalid) and handle it
                if (idIndex != -1 && titleIndex != -1 && contentIndex != -1 && authorIndex != -1 && dateIndex != -1) {
                    // Use the indexes to retrieve the values
                    Blog blog = new Blog(
                            cursor.getInt(idIndex),
                            cursor.getString(titleIndex),
                            cursor.getString(contentIndex),
                            cursor.getString(authorIndex),
                            cursor.getString(dateIndex)
                    );
                    cursor.close();
                    return blog;
                }
            }
            cursor.close();
        }

        return null;  // Return null if no blog is found or if there was an issue with the column index
    }
    // Method to update the blog
    public void updateBlog(String blogId, Blog blog) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, blog.getTitle());
        values.put(KEY_CONTENT, blog.getContent());
        values.put(KEY_AUTHOR, blog.getAuthor());
        values.put(KEY_DATE, blog.getDate());

        // Updating row
        db.update(TABLE_BLOGS, values, KEY_ID + " = ?", new String[]{blogId});
    }

    // Method to get all blogs
    public List<Blog> getAllBlogs() {
        List<Blog> blogList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to fetch all blogs
        Cursor cursor = db.query(TABLE_BLOGS, null, null, null, null, null, null);

        if (cursor != null) {
            // Check if the cursor has at least one row
            if (cursor.moveToFirst()) {
                // Safely retrieve column indexes
                int idIndex = cursor.getColumnIndex(KEY_ID);
                int titleIndex = cursor.getColumnIndex(KEY_TITLE);
                int contentIndex = cursor.getColumnIndex(KEY_CONTENT);
                int authorIndex = cursor.getColumnIndex(KEY_AUTHOR);
                int dateIndex = cursor.getColumnIndex(KEY_DATE);

                // Check if any of the indexes are -1 (invalid) and handle it
                if (idIndex != -1 && titleIndex != -1 && contentIndex != -1 && authorIndex != -1 && dateIndex != -1) {
                    // Iterate over all rows in the cursor
                    do {
                        Blog blog = new Blog(
                                cursor.getInt(idIndex),
                                cursor.getString(titleIndex),
                                cursor.getString(contentIndex),
                                cursor.getString(authorIndex),
                                cursor.getString(dateIndex)
                        );
                        blogList.add(blog);
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        }

        return blogList;  // Return the list of blogs (empty if there were issues or no blogs)
    }


    public List<Blog> searchBlogs(String query) {
        List<Blog> blogList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQuery = "SELECT * FROM blogs WHERE title LIKE ? OR content LIKE ?";
        String[] searchArgs = new String[]{"%" + query + "%", "%" + query + "%"};

        Cursor cursor = db.rawQuery(searchQuery, searchArgs);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Blog blog = new Blog(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("title")),
                        cursor.getString(cursor.getColumnIndex("content")),
                        cursor.getString(cursor.getColumnIndex("author")),
                        cursor.getString(cursor.getColumnIndex("date"))
                );
                blogList.add(blog);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return blogList;
    }


}

