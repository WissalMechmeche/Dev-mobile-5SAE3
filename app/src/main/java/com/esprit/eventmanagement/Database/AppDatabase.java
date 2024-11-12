package com.esprit.eventmanagement.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.esprit.eventmanagement.Dao.TicketDao;
import com.esprit.eventmanagement.Entities.Ticket;


@Database(entities = {Ticket.class}, version = 1, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {
    public abstract TicketDao ticketDao();
}
