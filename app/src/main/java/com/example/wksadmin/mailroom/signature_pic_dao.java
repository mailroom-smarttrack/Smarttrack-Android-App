package com.example.wksadmin.mailroom;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

/**
 * Created by wksadmin on 2/15/2018.
 */
@Dao
public interface signature_pic_dao {


    @Query("SELECT * from signature_pic ")
    List<signature_pic> get_all_signature_pic();

    @Query("SELECT * from signature_pic where pic_id = :pic_id_ ")
    List<signature_pic> get_signature_pic_by_pic_id(int pic_id_);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(signature_pic signature_pic_);

    @Query("DELETE FROM signature_pic where pic_db_id = :pic_db_id_  ")
    void deleteBypic_db_id_(String pic_db_id_);


    @Query("DELETE FROM signature_pic")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM  signature_pic")
    int count();

    @Update
    void update(signature_pic signature_pic_);

}
