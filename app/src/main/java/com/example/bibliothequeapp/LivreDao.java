package com.example.bibliothequeapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LivreDao {

    // Insère un nouveau livre dans la base
    @Insert
    void insert(Livre livre);

    // Modifie un livre existant
    @Update
    void update(Livre livre);

    // Supprime un livre existant
    @Delete
    void delete(Livre livre);

    // Récupère tous les livres, du plus récent au plus ancien
    @Query("SELECT * FROM livres ORDER BY id DESC")
    List<Livre> getAllLivres();

    // Supprime tous les livres
    @Query("DELETE FROM livres")
    void deleteAll();
}