package com.example.salonesproyecto;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static final String Direcciones [][] = {{"C9:65:D3:9D:1B:D4","F8:CE:D3:43:A8:3F","C1:BA:D9:B3:93:75","C2:BB:DA:B4:94:76"},{"Amarillo","Magenta","Rosa","Rosa"}};

    //Bluetooth
    BluetoothAdapter BAdapter;
    //BLE
    boolean prendido = false;
    private boolean mScanning;
   private android.os.Handler mHandler;
   private BluetoothAdapter.LeScanCallback mLeScanCallback =
           new BluetoothAdapter.LeScanCallback() {
               @Override
               public void onLeScan(final BluetoothDevice device, int rssi,
                                    byte[] scanRecord) {
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           if(BtDevices.toArray().length>=1){
                               //BAdapter.stopLeScan(mLeScanCallback);
                           }
                           else {
                               BtDevices.add(device.getAddress());
                               Object[] disp = BtDevices.toArray();
                               for(int i = 1; i <= BtDevices.size(); i++) {
                                   llenarFilas(disp[i-1].toString(),i);
                               }
                           }
                       }
                   });
               }
           };
    private static final long SCAN_PERIOD = 10000;
    public ArrayList<String> BtDevices = new ArrayList<>();
    public ArrayAdapter<String> deviceAdapter;

    //Firabase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private DatabaseReference mDatabase;
    int contador = 0;
    String beacon;

    //Variables
    Button btnBuscar;
    TextView txtR1;
    TextView txtR2;
    TextView txtR3;
    TextView txtR4;
    TextView txtR5;
    TextView txtR6;
    TextView txtR7;
    TextView txtR8;
    TextView txtR9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new android.os.Handler();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        BAdapter = BluetoothAdapter.getDefaultAdapter();
        setContentView(R.layout.activity_main);

        btnBuscar = (Button) findViewById(R.id.btnBuscar);
        txtR1 = (TextView) findViewById(R.id.txtR1);
        txtR2 = (TextView) findViewById(R.id.txtR2);
        txtR3 = (TextView) findViewById(R.id.txtR3);
        txtR4 = (TextView) findViewById(R.id.txtR4);
        txtR5 = (TextView) findViewById(R.id.txtR5);
        txtR6 = (TextView) findViewById(R.id.txtR6);
        txtR7 = (TextView) findViewById(R.id.txtR7);
        txtR8 = (TextView) findViewById(R.id.txtR8);
        txtR9 = (TextView) findViewById(R.id.txtR9);

        txtR1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtR1.getText().equals("")){
                    InfoSalon(txtR1.getText().toString());
                }
            }
        });
        txtR2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtR2.getText().equals("")){
                    InfoSalon(txtR2.getText().toString());
                }
            }
        });
        txtR3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtR3.getText().equals("")){
                    InfoSalon(txtR3.getText().toString());
                }
            }
        });
        txtR4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtR4.getText().equals("")){
                    InfoSalon(txtR4.getText().toString());
                }
            }
        });
        txtR5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtR5.getText().equals("")){
                    InfoSalon(txtR5.getText().toString());
                }
            }
        });
        txtR6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtR6.getText().equals("")){
                    InfoSalon(txtR6.getText().toString());
                }
            }
        });
        txtR7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtR7.getText().equals("")){
                    InfoSalon(txtR7.getText().toString());
                }
            }
        });
        txtR8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtR8.getText().equals("")){
                    InfoSalon(txtR8.getText().toString());
                }
            }
        });
        txtR9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtR9.getText().equals("")){
                    InfoSalon(txtR9.getText().toString());
                }
            }
        });
    }
    public boolean isInArray(String texto){
        return Arrays.asList(Direcciones[0]).contains(texto);
    }

    private BroadcastReceiver mBroadcastReciver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BtDevices.add(device.getAddress());
                Object[] disp = BtDevices.toArray();
                 for(int i = 1; i <= BtDevices.size(); i++) {
                    llenarFilas(disp[i-1].toString(),i);
                }
            }
        }
    };
    public void llenarFilas(String address, final int fila){
        beacon = "Desconocido";
        if(isInArray(address)){
            for(int i = 0; i<Direcciones[0].length; i++){
                if(Direcciones[0][i].equals(address)){
                    beacon = Direcciones[1][i];
                }
            }
            String direc = "Beacons/"+beacon;
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(direc).child("Salones");
            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    contador ++;
                    switch (contador){
                        case 1:
                            txtR1.setText(dataSnapshot.getValue().toString());
                            break;
                        case 2:
                            txtR2.setText(dataSnapshot.getValue().toString());
                            break;
                        case 3:
                            txtR3.setText(dataSnapshot.getValue().toString());
                            break;
                        case 4:
                            txtR4.setText(dataSnapshot.getValue().toString());
                            break;
                        case 5:
                            txtR5.setText(dataSnapshot.getValue().toString());
                            break;
                        case 6:
                            txtR6.setText(dataSnapshot.getValue().toString());
                            break;
                        case 7:
                            txtR7.setText(dataSnapshot.getValue().toString());
                            break;
                        case 8:
                            txtR8.setText(dataSnapshot.getValue().toString());
                            break;
                        case 9:
                            txtR9.setText(dataSnapshot.getValue().toString());
                            break;
                    }

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            myRef.addChildEventListener(childEventListener);
        }
    }
    public void btnDiscover(View view){
        BtDevices = new ArrayList<>();
        txtR1.setText("");
        txtR2.setText("");
        txtR3.setText("");
        txtR4.setText("");
        txtR5.setText("");
        txtR6.setText("");
        txtR7.setText("");
        txtR8.setText("");
        txtR9.setText("");
        contador = 0;
        //CheckP();
        scanLeDevice(true);
    }
    private void InfoSalon(String Aula){

        Intent Horario = new Intent(MainActivity.this,InfoSalon.class);
        Horario.putExtra("titulo",Aula);
        startActivity(Horario);
    }
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    BAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);
            mScanning = true;
            BAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            BAdapter.stopLeScan(mLeScanCallback);
        }
    }
    private void CheckP(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText(this, "The permission to get BLE location data is required", Toast.LENGTH_SHORT).show();
            }else{
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }else{
            Toast.makeText(this, "Location permissions already granted", Toast.LENGTH_SHORT).show();
        }
    }

}
