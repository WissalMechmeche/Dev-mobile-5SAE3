package com.esprit.eventmanagement.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.esprit.eventmanagement.entities.User;

import java.util.List;


@Dao
public interface UserDAO {
    @Insert
    void addUSer(User u);
    @Update
    void updateUSer(User u);
    @Delete
    void deleteUSer(User u);
    @Query("SELECT * FROM user")
    List<User> getAll();
    @Query("SELECT * FROM user where id = :id")
    User getOne(int id);

    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    User getUserByEmailAndPassword(String email, String password);

    @Query("SELECT email FROM User")
    List<String> getAllEmails();

    @Query("SELECT * FROM user WHERE phoneNumber = :phoneNumber")
    User getUserByPhoneNumber(String phoneNumber);

    @Query("UPDATE user SET password = :newPassword WHERE phoneNumber = :phoneNumber")
    int updatePasswordByPhoneNumber(String phoneNumber, String newPassword);








}
