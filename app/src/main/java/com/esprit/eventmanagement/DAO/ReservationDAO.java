package com.esprit.eventmanagement.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.esprit.eventmanagement.entities.Reservation;

import java.util.List;

@Dao
public interface ReservationDAO {

    // Insérer une réservation
    @Insert
    void insertReservation(Reservation reservation);

    // Récupérer toutes les réservations
    @Query("SELECT * FROM reservation")
    List<Reservation> getAllReservations();

    // Récupérer une réservation par ID
    @Query("SELECT * FROM reservation WHERE id = :reservationId")
    Reservation getReservationById(int reservationId);

    // Supprimer une réservation par ID
    @Query("DELETE FROM reservation WHERE id = :reservationId")
    int deleteReservationById(int reservationId);
}
