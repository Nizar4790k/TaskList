package com.example.tasklist;

import android.content.DialogInterface;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, TextToSpeech.OnInitListener {
    private boolean toolbar=true;
    private boolean active=false;
    private TextToSpeech tts;


    private DatabaseReference mDataBase;



    private String Titulo[]={"a"};
    private String Fecha[]={"b"};
    private String Descripcion[]={"c"};
    private String Hora[]={"d"} ;
    private int position1;
    private ListView listView;
    private CustomAdapter customAdapter;

    private ArrayList<Tarea> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tts = new TextToSpeech(this, this);
        ToolBar();
        InitMetods();
    }

    private void ToolBar(){
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
    }

    private void InitMetods(){
        customAdapter = new CustomAdapter(this, Titulo, Fecha, Hora, Descripcion);
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(active) {
                    position1 = position;
                    Toast.makeText(MainActivity.this, "Press on " + Titulo[position].toUpperCase(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {//Esta parte esta Terminada
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                toolbar=false;
                active=true;
                position1 = position;
                ToolBar();
                Toast.makeText(MainActivity.this, "Selected " + Titulo[position].toUpperCase(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        getAndSetAdd();
    }

    private void getAndSetAdd(){//agrega los datos que creas aquí
        /*////////////////////////////////////////////////////////////////////////////////////////////////////////////////////*/
        taskList = new ArrayList<Tarea>();

        mDataBase = FirebaseDatabase.getInstance().getReference(getString(R.string.Tareas)); // Referencia a la base de datos

        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dt : dataSnapshot.getChildren()){



                    Tarea tarea = dt.getValue(Tarea.class);
                    taskList.add( tarea);




                }

                Titulo = new String[taskList.size()];
                Fecha = new String[taskList.size()];
                Hora = new String[taskList.size()];
                Descripcion = new String[taskList.size()];

                for(int i=0;i<taskList.size();i++){
                    Titulo[i]= taskList.get(i).getTitulo();

                    Log.d(null,taskList.get(i).getTitulo());

                    Fecha[i]= taskList.get(i).getFecha();
                    Descripcion[i]= taskList.get(i).getDescripcion();
                    Hora[i]= taskList.get(i).getHora();
                }

                CustomAdapter customAdapter = new CustomAdapter(MainActivity.this,Titulo,Fecha,Hora,Descripcion);
                listView.setAdapter(customAdapter);

                // Evento de cambio en la base de datos
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*////////////////////////////////////////////////////////////////////////////////////////////////////////////////////*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if(toolbar) {
            getMenuInflater().inflate(R.menu.menu, menu);
            MenuItem menuItem = menu.findItem(R.id.buscar);
            SearchView searchView = (SearchView) menuItem.getActionView();
            searchView.setQueryHint("Buscar...");

            searchView.setOnQueryTextListener(this);
            View searchPlate = (View) searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
            searchPlate.setBackgroundResource(R.color.ColorButton);
        }else{
            toolbar = true;
            getMenuInflater().inflate(R.menu.menu_press, menu);
        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.agregar:
                Intent intent = new Intent(MainActivity.this, Agregar.class);
                startActivity(intent);
                tts.shutdown();
                finish();
                break;
            case R.id.borrar:
                ToolBar();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(Titulo[position1].toUpperCase());
                builder.setMessage("Realmente Desea Borrar Este Apunte?")
                        .setCancelable(false)
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                /* Cuerpo Restante Para Eliminarlo de la Base de Datos*/

                                /* Fin del Cuerpo */
                                deletElementList();//Eliminando elemento seleccionado
                                /*///////////////////////////////////////////////////////////////////////*/
                                Toast.makeText(MainActivity.this, "Eliminado con Exito!", Toast.LENGTH_SHORT).show();
                                active=false;
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
            case R.id.audio:
                String Audio="Apunte número " + (position1+1) + ", tiene como título" + Titulo[position1] + ", La fecha establecida es, día ";
                String fecha[] = Fecha[position1].split("\\/");
                if(fecha[0].charAt(0) == '0'){
                    Audio += fecha[0].charAt(1) + " del mes ";
                }else{
                    Audio += fecha[0] + " del mes ";
                }

                if(fecha[1].charAt(0) == '0'){
                    Audio += fecha[1].charAt(1) + " del año " + fecha[2] + ", ";
                }else{
                    Audio +=  fecha[1] + " del año " + fecha[2] + ", ";
                }

                try {
                    Audio += "y la descripción establecida, " + Descripcion[position1];
                }catch (ArrayIndexOutOfBoundsException ex){
                    Audio += "Actualmente no cuenta con una Descripción.";
                }
                tts.setLanguage(new Locale("ESP"));
                if(tts.isSpeaking()) {
                    tts.stop();
                }
                tts.speak(Audio, TextToSpeech.QUEUE_ADD, null);
                break;
        }

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        String acum="";
        if(s.length() > 0) {
            for (int i = 0; i < Titulo.length; i++) {
                for (int j = 0; j < Titulo[i].length(); j++) {
                    acum += Titulo[i].charAt(j);
                    if (acum.toUpperCase().equals(s.toUpperCase())) {
                        listView.setSelection(i);
                    }
                    if (Titulo[i].charAt(j) == ' ') {
                        acum = "";
                    }
                }
                acum = "";
            }
        }else if(listView.getCount() > 0) {
            listView.setSelection(0);
        }

        return false;
    }

    public void deletElementList(){
        listView.setAdapter(null);
        //Eliminando el Apunte del ListView
        String titulo="", fecha="", hora="", descripcion="";
        for (int i=0; i<Titulo.length; i++){
            if(!Titulo[i].equals(Titulo[position1])){
                titulo += Titulo[i] + "|";
                fecha += Fecha[i] + "|";
                hora += Hora[i] + "|";
                try {
                    descripcion += Descripcion[i] + "|";
                }catch (ArrayIndexOutOfBoundsException ex){
                    descripcion += "\"SIN DESCRIPCION\"" + "|";
                }
            }
        }
        //Asignando nuevos valores a los arreglos
        Titulo = titulo.split("\\|");
        Fecha = fecha.split("\\|");
        Hora = hora.split("\\|");
        Descripcion = descripcion.split("\\|");
        //Asignando Nuevos Datos
        customAdapter = new CustomAdapter(MainActivity.this, Titulo, Fecha, Hora, Descripcion);
        listView.setAdapter(customAdapter);
    }

    @Override
    public void onInit(int status) {
    }

    @Override
    public void onBackPressed(){
        if(active){
            active=false;
            tts.stop();
            ToolBar();
        }else {
            tts.shutdown();
            finish();
        }
    }
}