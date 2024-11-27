package com.esprit.eventmanagement.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.esprit.eventmanagement.DAO.EventDAO;
import com.esprit.eventmanagement.DAO.ReservationDAO;
import com.esprit.eventmanagement.DAO.TicketDao;
import com.esprit.eventmanagement.DAO.UserDAO;
import com.esprit.eventmanagement.entities.Event;
import com.esprit.eventmanagement.entities.Reservation;
import com.esprit.eventmanagement.entities.Ticket;
import com.esprit.eventmanagement.entities.User;


@Database(entities = {User.class , Event.class , Ticket.class , Reservation.class},version = 1 , exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase instance;

    public abstract UserDAO UserDAO();
    public abstract EventDAO EventDAO();
    public abstract TicketDao TicketDao();
    public abstract ReservationDAO ReservationDAO();

    public static AppDataBase getAppDataBase(Context context){

        if (instance==null){
            Log.d("AppDataBase", "Creating new database instance.");
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                            AppDataBase.class,
                            "event_management_db")
                    .allowMainThreadQueries()
                    .build();

            } else {
        Log.d("AppDataBase", "Using existing database instance.");
    }

        return instance;
    }


}
