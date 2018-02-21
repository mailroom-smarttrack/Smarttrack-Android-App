package com.example.wksadmin.mailroom;

/**
 * Created by wksadmin on 2/15/2018.
 */


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "signature_pic")
public class signature_pic {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "pic_id")
    private   int _pic_id;

    @NonNull
    @ColumnInfo(name = "pic_jpg" ,typeAffinity = ColumnInfo.BLOB)
    public  byte[] _pic_jpg ;

    @NonNull
    @ColumnInfo(name = "pic_db_id" )
    public  int _pic_db_id =-1;


    public signature_pic(@NonNull  byte[] pic_jpg   ) {
        this._pic_jpg =pic_jpg;

    }




    @NonNull
    public int get_pic_id() {
        return _pic_id;
    }

    public void set_pic_id(@NonNull int pic_id) {
        this._pic_id = pic_id;
    }

    public void set_pic_db_id(@NonNull int pic_db_id) {
        this._pic_db_id = pic_db_id;
    }

    public String toString()
    {
        return _pic_id+ " jpg size: "+  _pic_jpg.length + " pic_db_id: " +  _pic_db_id;
    }
}
