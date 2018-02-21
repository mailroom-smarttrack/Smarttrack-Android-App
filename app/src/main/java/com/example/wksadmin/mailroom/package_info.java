package com.example.wksadmin.mailroom;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by wksadmin on 2/14/2018.
 */

@Entity(tableName = "package_info")
public class package_info {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "label_id")
    private String _label_id;


    @NonNull
    @ColumnInfo(name = "tracking_id")
    private String _tracking_id;

    @NonNull
    @ColumnInfo(name = "sender")
    private String _sender;


    public package_info(@NonNull String label_id ,@NonNull String tracking_id , @NonNull String sender   ) {
        this._label_id = label_id;
        this._sender =sender;
        this._tracking_id =tracking_id ;
    }

    public String get_trackerID_sender()
    {return this._tracking_id + " " +_sender;}

    public String toString()
    {
        return _label_id + " " +this._tracking_id + " " +_sender;
    }

    @NonNull
    public String get_label_id() {
        return _label_id;
    }

    public void set_label_id(@NonNull String _label_id) {
        this._label_id = _label_id;
    }

    @NonNull
    public String get_tracking_id() {
        return _tracking_id;
    }

    public void set_tracking_id(@NonNull String _tracking_id) {
        this._tracking_id = _tracking_id;
    }


    @NonNull
    public String get_sender() {
        return _sender;
    }

    public void set_sender(@NonNull String _sender) {
        this._sender = _sender;
    }


}
