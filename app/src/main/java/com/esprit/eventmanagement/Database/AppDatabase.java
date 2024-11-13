package com.esprit.eventmanagement.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.esprit.eventmanagement.Dao.TicketDao;
import com.esprit.eventmanagement.Entities.Ticket;


@Database(entities = {Ticket.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TicketDao ticketDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                // Ensures that INSTANCE is only created once and thread-safe
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "ticket-db")
                            .fallbackToDestructiveMigration() // Optional: allows migration if needed
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}