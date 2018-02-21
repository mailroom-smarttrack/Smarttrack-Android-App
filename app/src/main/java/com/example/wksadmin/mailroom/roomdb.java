package com.example.wksadmin.mailroom;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;



/**
 * Created by wksadmin on 2/14/2018.
 */
@Database(entities = {package_info.class , signature_info.class, signature_pic.class}, version = 3)
public abstract  class roomdb extends RoomDatabase {

    public abstract package_info_dao packageinfo_dao();
    public abstract signature_info_dao signature_info_dao();
    public abstract signature_pic_dao signature_pic_dao();

    private static roomdb INSTANCE;

    static roomdb getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (roomdb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            roomdb.class  , "package_info_db")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this codelab.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){

        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            // If you want to keep the data through app restarts,
            // comment out the following line.
             new PopulateDbAsync(INSTANCE).execute();
        }
    };


    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final package_info_dao m_package_info_dao;
        private final signature_info_dao m_signature_info_dao;
        private final signature_pic_dao m_signature_pic_dao;


        PopulateDbAsync(roomdb db) {
            m_package_info_dao = db.packageinfo_dao();
            m_signature_info_dao = db.signature_info_dao();
            m_signature_pic_dao = db.signature_pic_dao();

        }


        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.

            try {




//                Class.forName("com.mysql.jdbc.Driver");
//
//                Connection con = DriverManager.getConnection(Mailroom.url, Mailroom.user, Mailroom.pass);
//            /* System.out.println("Database connection success"); */
//                String result = "Database connection success\n";
//                Statement st = con.createStatement();
//                ResultSet rs = st.executeQuery("SELECT IeSID , IeSTrackingNo , IeSSender FROM smarttrack.itemstatus ORDER BY IeSAutoID DESC LIMIT 100;");
//                ResultSetMetaData rsmd = rs.getMetaData();

                //empList.removeAll(true);
                //empList.clear();
//                m_package_info_dao.deleteAll();
//                while(rs.next()) {
//                    //empList.add(rs.getString(1));
//                    Log.d("debug room sql", rs.getString(1)+" "+rs.getString(2) + " "+ rs.getString(3));
//
//                    m_package_info_dao.insert(new package_info(rs.getString(1) ,rs.getString(2) , rs.getString(3)));
//                }
//                m_package_info_dao.insert(new package_info("","",""));

                //actv.clearListSelection();
                //actv.setAdapter(empAdapter);
                //con.close();

                //roomdb db = roomdb.getDatabase(Mailroom.this);
                //package_info_dao dao = db.packageinfo_dao();
                //List<package_info> _package_info = dao.get_all_package_info();
//                Log.d("debug - room" , m_signature_info_dao.count()+"" );
//                m_signature_info_dao.insert(new signature_info("11111" , "receiver 1 " , "11-22-2018 12:34:56" , 10));
//                m_signature_info_dao.insert(new signature_info("22222" , "receiver 2 " , "11-22-2018 12:34:56" , 20));
//                m_signature_info_dao.insert(new signature_info("33333" , "receiver 3 " , "11-22-2018 12:34:56" , 30));
//                m_signature_info_dao.insert(new signature_info("44444" , "receiver 4 " , "11-22-2018 12:34:56" , 40));
//
//                Log.d("debug - room" , m_signature_info_dao.count()+"" );
//                Log.d("debug - room" , m_signature_info_dao.get_signature_info_by_label_id("22222")+"" );
//                Log.d("debug - room" , m_signature_info_dao.get_all_signature_info()+"" );

            }

            catch(Exception e) {
                e.printStackTrace();
                //err = e.toString();
                //Toast.makeText( Mailroom.this, e.toString(), Toast.LENGTH_SHORT).show();
                Log.d("debug sql", e.toString());

            }


            return null;
        }
    }
}



