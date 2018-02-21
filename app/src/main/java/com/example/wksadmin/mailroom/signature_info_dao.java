package com.example.wksadmin.mailroom;

/**
 * Created by wksadmin on 2/15/2018.
 */


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

@Dao
public interface signature_info_dao {


    @Query("SELECT * from signature_info ")
    List<signature_info> get_all_signature_info();

    @Query("SELECT * from signature_info where label_id = :label_id_ ")
    List<signature_info> get_signature_info_by_label_id(String label_id_);

    @Insert
    void insert(signature_info word);

    @Query("DELETE FROM signature_info where label_id = :label_id_  ")
    void deleteBylabel_id_(String label_id_);


    @Query("DELETE FROM signature_info")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM  signature_info")
    int count();



}
