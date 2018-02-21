package com.example.wksadmin.mailroom;



/**
 * Created by wksadmin on 2/15/2018.
 */


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;



@Entity(tableName = "signature_info")
public class signature_info {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "signature_id")
    private   int _signature_id;

    @NonNull
    @ColumnInfo(name = "label_id")
    public  String _label_id;


    @NonNull
    @ColumnInfo(name = "receiver")
    public  String _receiver;

    @NonNull
    @ColumnInfo(name = "receive_date")
    public  String _receiver_date;

    @NonNull
    @ColumnInfo(name = "signature_pic_id")
    public  int _signature_pic_id;


    public String toString()
    {
        return _signature_id+ " "+  _label_id + " " +this._receiver + " " +_receiver_date + " " + _signature_pic_id;
    }

    public signature_info(@NonNull String label_id ,@NonNull String receiver , @NonNull String receiver_date , @NonNull  int signature_pic_id  ) {
        this._label_id = label_id;
        this._receiver =receiver;
        this._receiver_date =receiver_date ;
        this._signature_pic_id =signature_pic_id;
    }


    @NonNull
    public int get_signature_id() {
        return _signature_id;
    }

    public void set_signature_id(@NonNull int _signature_id) {
        this._signature_id = _signature_id;
    }



}
