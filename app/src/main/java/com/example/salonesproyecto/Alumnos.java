package com.example.salonesproyecto;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Alumnos extends AppCompatActivity {

    TextView txtGrupo;
    ListView lvAlumnos;
    //Firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private DatabaseReference mDatabase;
    FirebaseListAdapter Adapter;
    //Variables
    String Grupo;
    String Carrera;
    //Lista-Array
    List<String> Alumnos;
    ArrayAdapter<String> Adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumnos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Grupo = getIntent().getExtras().getString("grupo");
        Carrera = getIntent().getExtras().getString("carrera");

        txtGrupo = (TextView)findViewById(R.id.txtGrupo);
        lvAlumnos = (ListView)findViewById(R.id.lvAlumnos);
        txtGrupo.setText(Grupo);
        listaAlumnos();
    }

    public void llenadoLista(){
        Adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Alumnos);
        lvAlumnos.setAdapter(Adaptador);
    }

    public void listaAlumnos(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Salones/"+Carrera+"/"+Grupo);
        Adapter = new FirebaseListAdapter(this,String.class, android.R.layout.two_line_list_item,myRef) {
            @Override
            protected void populateView(View v, Object model, int position) {
                ((TextView) v.findViewById(android.R.id.text1)).setText(model.toString());
            }
        };
        lvAlumnos.setAdapter(Adapter);
    }
}

