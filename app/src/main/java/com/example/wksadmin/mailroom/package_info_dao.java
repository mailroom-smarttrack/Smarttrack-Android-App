package com.example.wksadmin.mailroom;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by wksadmin on 2/14/2018.
 */
@Dao
public interface  package_info_dao {

    @Query("SELECT * from package_info ")
    List<package_info> get_all_package_info();

    @Query("SELECT * from package_info where label_id = :label_id_ ")
    List<package_info> get_package_info_by_label_id(String label_id_);

    @Insert
    void insert(package_info word);


    @Query("DELETE FROM package_info")
    void deleteAll();


    @Query("SELECT COUNT(*) FROM  package_info")
    int count();


}
