package com.example.wksadmin.mailroom;
import android.annotation.SuppressLint;
import android.content.Context;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.hardware.SensorListener;
import android.os.AsyncTask;

import android.os.Vibrator;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.hardware.Camera;
import android.widget.Toast;
import android.util.Log;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.INTERNET;
import static android.app.PendingIntent.getActivity;

import android.content.DialogInterface;
import android.content.Intent;
import java.io.ByteArrayOutputStream;
import java.sql.CallableStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import android.widget.AutoCompleteTextView;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.ProgressDialog;

import android.os.AsyncTask;


public class Mailroom extends AppCompatActivity implements SensorEventListener {


    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;
    private ArrayAdapter<String> empAdapter ;
    private ArrayList<String> barcodeList;
    private ArrayList<String> empList;


    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_INTERNET = 2;
    private static final int REQUEST_ACCESS_NETWORK_STATE = 3;
    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;

    private  SensorManager m_sensor_manager;
    private  Sensor m_accelerometer;
    private long lastUpdate = 0;
    private long shack_count = 0;
    private Vibrator vib;
    private static final String url = "jdbc:mysql://10.17.204.134:3306/smarttrack";
    private static final String user = "mailroom";
    private static final String pass = "mailroom";

    public package_info_dao _package_info_dao;
    public signature_info_dao _signature_info_dao;
    public signature_pic_dao _signature_pic_dao;
    public roomdb db ;

    private ArrayList<String> current_barcode_list;




    private AutoCompleteTextView actv ;



    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {




        m_sensor_manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        m_accelerometer = m_sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        m_sensor_manager.registerListener(this, m_accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);





        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mailroom);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        UiChangeListener();
        empList  = new ArrayList<String>();
        current_barcode_list = new ArrayList<String>();
        new Async_Mysql().execute();

        mainListView = findViewById( R.id.mainListView );

        barcodeList = new ArrayList<String>();
        //String[] barcode = new String[] { ""};


        //barcodeList.addAll( Arrays.asList(barcode) );

        empAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, empList);

        //AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        actv = findViewById(R.id.autoCompleteTextView);
        actv.setThreshold(1);
        actv.clearListSelection();
        actv.setAdapter(empAdapter);
        actv.setTextColor(Color.RED);
        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, barcodeList);
        View header = getLayoutInflater().inflate(R.layout.list_header, mainListView, false);
        mainListView.addHeaderView(header, null, false);
        mainListView.setAdapter( listAdapter );

        //mScannerView = new ZXingScannerView(this);
        qrScan = new IntentIntegrator(this);

        FloatingActionButton _fab_scan = findViewById(R.id.fab_scan);
        _fab_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                //setContentView(mScannerView);
                qrScan.setPrompt("Scan a Package");
                qrScan.setCameraId(camId);  // Use a specific camera of the device
                qrScan.setBeepEnabled(true);
                qrScan.setOrientationLocked(false);
                qrScan.setBarcodeImageEnabled(true);
                qrScan.setTimeout(3000);
                qrScan.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                qrScan.initiateScan();
            }
        });
        //Getting the scan results

        FloatingActionButton _fab_sign = findViewById(R.id.fab_sign);
        _fab_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((barcodeList.size() ==0)|| (actv.getText().length() == 0)){
                    Toast.makeText( Mailroom.this, "Please input Receiver name and Scan someitem first.", Toast.LENGTH_SHORT).show();
                }else {
                    new AlertDialog.Builder(Mailroom.this)
                            .setMessage("Is the signature ok?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @SuppressLint("StaticFieldLeak")
                                public void onClick(DialogInterface dialog, int id) {
                                    Toast.makeText(Mailroom.this, "Signature Accepted", Toast.LENGTH_SHORT).show();
                                    GestureOverlayView gestureView = findViewById(R.id.signaturePad);
                                    gestureView.setDrawingCacheEnabled(true);
                                    //gestureView.setBackgroundColor(Color.WHITE);
                                    Bitmap bm = Bitmap.createScaledBitmap(gestureView.getDrawingCache(), 540 , 450,true );
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bm.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                                    ListView _ListView  = findViewById( R.id.mainListView );
                                    //update Gestures
                                    String[] _barcode = barcodeList.toArray(new String[barcodeList.size()]);

                                    Object arr1[] = new Object[] {baos.toByteArray(),_barcode , actv.getText().toString()};
                                    new AsyncTask<Object , Void, Void>() {
                                        @Override
                                        protected Void doInBackground(Object... _object) {
                                            try {
                                                byte[] _byte = (byte[])_object[0];
                                                String[] _barcodeList = (String[])_object[1];
                                                String _receiver = (String)_object[2];

                                                Log.v("Gestures", _byte.toString() + "  " + _byte.length);

                                                long pic_id = _signature_pic_dao.insert(new signature_pic( _byte ));
                                                Log.v("_signature_pic_dao", pic_id + "  " );

                                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                                Log.v("_barcodeList.size()", _barcodeList.length+"");


                                                Date date = Calendar.getInstance().getTime();
                                                for (int i = 0; i < _barcodeList.length; i++) {
                                                    _signature_info_dao.insert(new signature_info(_barcodeList[i],_receiver,dateFormat.format(date),(int)pic_id));
                                                    Log.v("_signature_info_dao", _signature_info_dao.get_all_signature_info().toString());
                                                }

                                            } catch (Exception e) {
                                                Log.v("Gestures", e.getMessage());
                                                e.printStackTrace();
                                            }
                                            return null;
                                        }
                                    }.execute(arr1);




                                    gestureView.cancelClearAnimation();
                                    gestureView.clear(true);

                                    gestureView.invalidate();
                                    actv.setText("");
                                    barcodeList.clear();

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Toast.makeText(Mailroom.this, "Signature Clear", Toast.LENGTH_SHORT).show();
                                    GestureOverlayView gestureView = findViewById(R.id.signaturePad);
                                    gestureView.cancelClearAnimation();
                                    gestureView.clear(true);
                                    //gestureView.setBackgroundColor(0x01060013);
                                    gestureView.invalidate();
                                }
                            })
                            .show();

                }



            }
        });

        FloatingActionButton _fab_sync = findViewById(R.id.fab_sync);
        _fab_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Async_Mysql().execute();
                empAdapter.notifyDataSetChanged();
                Toast.makeText( Mailroom.this, "Sync", Toast.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if ((result.getContents() == null)  ){
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data

                if (current_barcode_list.contains(result.getContents()))
                {
                    int index = -1;
                    index = listAdapter.getPosition(result.getContents());

                    Log.d("debug index", index+"");
                    if (index == -1){
                        listAdapter.add(result.getContents());
                        Toast.makeText(this, "Add Item: "+result.getContents(), Toast.LENGTH_LONG).show();
                    }else
                    {
                        listAdapter.remove(result.getContents());
                        Toast.makeText(this, "Delete Item: "+result.getContents(), Toast.LENGTH_LONG).show();
                    }
                    listAdapter.notifyDataSetChanged();
                }else
                {
                    Toast.makeText(this, "Please Scan the Qrcode again", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mailroom, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = sensorEvent.timestamp;

            if ((curTime - lastUpdate) > 500) {
                float[] values = sensorEvent.values;
                float x = values[0];
                float y = values[1];
                float z = values[2];
                float accelationSquareRoot = (x * x + y * y + z * z)
                        / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

                if (accelationSquareRoot >= 7){
                    Log.d("Debug shack", accelationSquareRoot +"");
                    shack_count ++;
                }

                if ( shack_count >= 4) //
                {
                    Log.d("Debug shack", "Clear screen");
                    shack_count = 0;
                    Toast.makeText(this, "Clear Signature", Toast.LENGTH_SHORT).show();
                    lastUpdate = curTime;
                    GestureOverlayView gestureView = findViewById(R.id.signaturePad);
                    gestureView.cancelClearAnimation();
                    gestureView.clear(true);
                    try {
                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                        r.play();
                        vib.vibrate(400);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Mailroom.this.finishAndRemoveTask();
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void UiChangeListener()
    {
        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener (new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }
            }
        });
    }



    private class Async_Mysql extends AsyncTask <String , String , String >{
        ProgressDialog pdLoading = new ProgressDialog(Mailroom.this);
        String err = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage("\tSync With Database...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }


        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);
            /* System.out.println("Database connection success"); */
                String result = "Database connection success\n";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT IloPadLocation FROM smarttrack.itemlog where IloStatusID ='DE' group by IloPadLocation ORDER BY IloAutoID DESC LIMIT 50 ;");
                //ResultSetMetaData rsmd = rs.getMetaData();


                //empList.removeAll(true);
                empList.clear();
                while(rs.next()) {
                    empList.add(rs.getString(1));
                    Log.d("debug sql", rs.getString(1));
                }
                rs = st.executeQuery("SELECT IeSID , IeSTrackingNo , IeSSender FROM smarttrack.itemstatus ORDER BY IeSAutoID DESC LIMIT 100;");
                //android room db init
                db = roomdb.getDatabase(Mailroom.this);
                _package_info_dao = db.packageinfo_dao();
                _signature_info_dao = db.signature_info_dao();
                _signature_pic_dao = db.signature_pic_dao();

                //android room db -- get data
                _package_info_dao.deleteAll();
                current_barcode_list.clear();
                while(rs.next()) {
                    _package_info_dao.insert(new package_info(rs.getString(1) ,rs.getString(2) , rs.getString(3)));
                    current_barcode_list.add(rs.getString(1));

//                    Log.d("debug insert sql", rs.getString(1)+" "+rs.getString(2) + " "+ rs.getString(3));
                }
                //android room db -- upload signature_pic
                PreparedStatement sql_insert_pic , sql_insert_log , sql_delete_status , sql_call;

                sql_insert_pic = con.prepareStatement("insert into smarttrack.signatures (SigSignature) values (?)");

                List<signature_pic> signature_pics_list = _signature_pic_dao.get_all_signature_pic();
                for (int i=0; i<signature_pics_list.size(); i++) {
                    signature_pic tmp = signature_pics_list.get(i);
                    sql_insert_pic.setBytes(1,tmp._pic_jpg);
                    sql_insert_pic.execute();
                    int sign_dbid = -1;
                    ResultSet generatedKeys = sql_insert_pic.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        sign_dbid = generatedKeys.getInt(1);
                    }
                    Log.d("debug - sign_dbid" , sign_dbid+"" );
                    tmp.set_pic_db_id(sign_dbid);
                    _signature_pic_dao.update(tmp);
                    Log.d("debug - sign_dbid" , _signature_pic_dao.get_all_signature_pic().toString() );
                }

                //android room db -- upload signature_info
                sql_insert_log = con.prepareStatement("INSERT INTO smarttrack.itemlog (itemlog.IloStaCode, itemlog.IloStaGroup, itemlog.IloID, itemlog.IloTrackingNo, itemlog.IloCarrier, itemlog.IloSender, itemlog.IloEmployeeFirstName, itemlog.IloEmployeeLasttName, itemlog.IloLocName, itemlog.IloLocID, itemlog.IloService, itemlog.IloServiceDescripton, itemlog.IloDateTime, itemlog.IloUseName, itemlog.IloStatusID , itemlog.IloPadLocation , itemlog.IloSigID) Select itemlog.IloStaCode, itemlog.IloStaGroup, itemlog.IloID, itemlog.IloTrackingNo, itemlog.IloCarrier, itemlog.IloSender, itemlog.IloEmployeeFirstName, itemlog.IloEmployeeLasttName, itemlog.IloLocName, itemlog.IloLocID, itemlog.IloService, itemlog.IloServiceDescripton, ?, itemlog.IloUseName, 'DE', ? , ? from smarttrack.itemlog where IloID = ? Order by IloAutoID desc limit 1");
                sql_delete_status = con.prepareStatement("DELETE FROM smarttrack.itemstatus WHERE IeSAutoID = (select temp.IeSAutoID from(select IeSAutoID FROM smarttrack.itemstatus where IeSID = ?) as temp)");


                List<signature_info> signatureInfo_list = _signature_info_dao.get_all_signature_info();
                Log.d("debug - signatureInfo_list" , signatureInfo_list.toString() );

                for (int i=0 ; i<signatureInfo_list.size(); i++){
                    signature_info tmp = signatureInfo_list.get(i);
                    List<signature_pic> pic_tmp = _signature_pic_dao.get_signature_pic_by_pic_id(tmp._signature_pic_id);
                    Log.d("debug - signature_info" , tmp.toString());
                    Log.d("debug - signature_pic" , pic_tmp.toString());

                    sql_insert_log.setString(1,tmp._receiver_date);
                    sql_insert_log.setString(2,tmp._receiver);
                    sql_insert_log.setString(3,pic_tmp.get(0)._pic_db_id+"");
                    sql_insert_log.setString(4,tmp._label_id);
                    sql_insert_log.execute();
                    sql_delete_status.setString(1,tmp._label_id);
                    sql_delete_status.execute();
                }

                con.close();


//                List<package_info> _package_info = dao.get_all_package_info();
                //Log.d("debug - _package_info_dao" , _package_info_dao.count()+"" );
                //Log.d("debug - _signature_info_dao" , _signature_info_dao.count()+"" );
                //Log.d("debug - _signature_info_dao" , _signature_info_dao.get_all_signature_info()+"" );
            }
            catch(Exception e) {
                e.printStackTrace();
                err = e.toString();
                //Toast.makeText( Mailroom.this, e.toString(), Toast.LENGTH_SHORT).show();
                Log.d("debug sql", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pdLoading.dismiss();
            if(err == null) {
                _signature_info_dao.deleteAll();
                _signature_pic_dao.deleteAll();
                Toast.makeText(Mailroom.this, "Sync Success", Toast.LENGTH_LONG).show();
            }else{
                // you to understand error returned from doInBackground method
                Toast.makeText(Mailroom.this, err, Toast.LENGTH_LONG).show();
            }
        }
    }
}



