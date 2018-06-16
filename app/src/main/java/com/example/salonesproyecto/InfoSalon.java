package com.example.salonesproyecto;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.felipecsl.gifimageview.library.GifImageView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class InfoSalon extends AppCompatActivity {
    //Storage
    private StorageReference mStorageRef;
    private FirebaseStorage storage;
    //Firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private DatabaseReference mDatabase;
    //Hora
    Calendar calendario = Calendar.getInstance();
    //Componentes
    RadioButton rdbActivo;
    TextView txtAula;
    TextView txtMaestro;
    TextView txtCarrera;
    TextView txtMateria;
    GifImageView ImgHorario;
    ImageView ImgMaestro;
    Button btnVerAumnos;
    //Variables
    String Aula;
    int hora;
    String grupo;
    //Ble
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            }
        }
    };
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(device.getAddress().toString().equals("E2:02:00:25:36:40")) {
                                rdbActivo.setChecked(true);
                                scanLeDevice(false);
                            }
                        }
                    });
                }
            };
    private static final long SCAN_PERIOD = 10000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new android.os.Handler();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        setContentView(R.layout.activity_info_salon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Calendar calendario = new GregorianCalendar();
        hora = calendario.get(Calendar.HOUR_OF_DAY);

        storage = FirebaseStorage.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final String Titulo = getIntent().getExtras().getString("titulo");

        ImgHorario = (GifImageView) findViewById(R.id.imgHorario);
        rdbActivo = (RadioButton) findViewById(R.id.rdbActivo);
        ImgMaestro = (ImageView)findViewById(R.id.imageView3);
        txtMaestro = (TextView)findViewById(R.id.txtNombreMaestro);
        txtCarrera = (TextView)findViewById(R.id.txtCarrera4);
        txtMateria = (TextView)findViewById(R.id.txtMateria);
        txtAula = (TextView)findViewById(R.id.txtAula);
        btnVerAumnos = (Button)findViewById(R.id.btnAlumnos);
        txtAula.setText(Titulo);
        Aula = Titulo.substring(5,8);
        Aula = Aula.replace(".","");
        nombreMaestro(Aula, (hora + ":00"));
        nombreCarrera(Aula,(hora + ":00"));
        nombreMateria(Aula,(hora + ":00"));
        nombreSalon(Aula,(hora + ":00"));
        imageDownloadHorario(Aula);
        ImgHorario.setOnTouchListener(new OnSwipeTouchListener(InfoSalon.this){
            public void onSwipeTop() {

            }
            public void onSwipeRight() {
                imageDownloadHorario(Aula);
                ImgHorario.stopAnimation();
            }
            public void onSwipeLeft() {
                imageDownloadGif(Aula);
            }
            public void onSwipeBottom() {

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        scanLeDevice(true);
    }

    public void imageDownloadHorario(String Salon){
        try {
            String imageAula = "Salon" + Salon + ".JPG";
            final File localFile = File.createTempFile("images", "jpeg");
            StorageReference storageRef = storage.getReferenceFromUrl("gs://proyectounesalones.appspot.com").child(imageAula);
            storageRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap image = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            ImgHorario.setImageBitmap(image);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });
        }catch (IOException e){

        }
    }
    public void imageDownloadGif(String Salon){
        try {
            String imageAula = "Gif" + Salon + ".gif";
            final File localFile = File.createTempFile("images", "gif");
            StorageReference storageRef = storage.getReferenceFromUrl("gs://proyectounesalones.appspot.com").child(imageAula);
            storageRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            ImgHorario.setBytes(convertFileToByteArray(localFile));
                            ImgHorario.startAnimation();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });
        }catch (IOException e){

        }
    }
    public void nombreMaestro(String Salon, String Clase){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Aulas/"+Salon+"/Lunes/"+Clase+"/Maestro");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txtMaestro.setText(dataSnapshot.getValue(String.class));
                imageDownloadProfsr(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void nombreCarrera(String Salon,  String Clase){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Aulas/"+Salon+"/Lunes/"+Clase+"/Carrera");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txtCarrera.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void nombreMateria(String Salon,  String Clase){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Aulas/"+Salon+"/Lunes/"+Clase+"/Materia");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txtMateria.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void nombreSalon(final String Salon, String Clase){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Aulas/"+Salon+"/Lunes/"+Clase+"/Grupo");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                grupo = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void mostrarAlumnos(View view){
        Intent Alumnos = new Intent(InfoSalon.this,Alumnos.class);
        Alumnos.putExtra("carrera",txtCarrera.getText());
        Alumnos.putExtra("grupo",grupo);
        startActivity(Alumnos);
    }
    public void imageDownloadProfsr(String Maestro){
        try {
            String imageAula =  Maestro + ".png";
            final File localFile = File.createTempFile("images", "png");
            StorageReference storageRef = storage.getReferenceFromUrl("gs://proyectounesalones.appspot.com").child(imageAula);
            storageRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap image = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            ImgMaestro.setImageBitmap(image);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });
        }catch (IOException e){

        }
    }
    public static byte[] convertFileToByteArray(File f) {
        byte[] byteArray = null;
        try
        {
            InputStream inputStream = new FileInputStream(f);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024*8];
            int bytesRead =0;

            while ((bytesRead = inputStream.read(b)) != -1)
            {
                bos.write(b, 0, bytesRead);
            }

            byteArray = bos.toByteArray();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return byteArray;
    }
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }
}
