package com.example.tasklist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Agregar extends AppCompatActivity {
    private Spinner spinner;
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    private DatabaseReference mDataBase; // Aqui declarando la base de datos.

    @Override
    protected void onCreate(Bundle save){
        super.onCreate(save);
        setContentView(R.layout.agregar);
        add_elements();

        FirebaseApp.initializeApp(this);   // El error esta aqui
        mDataBase = FirebaseDatabase.getInstance().getReference();

    }

    private void add_elements(){

        final TextView textView3 = (TextView) findViewById(R.id.textView3);
        final TextView textView5 =  (TextView) findViewById(R.id.textView5);
        final EditText editText1  = (EditText) findViewById(R.id.editText1);
        final EditText editText2 = (EditText)findViewById(R.id.txtDescripcion);

        final Button button = (Button) findViewById(R.id.botonAceptar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String titulo = editText1.getText().toString();
                String fecha = textView3.getText().toString();
                String hora = textView5.getText().toString();
                String descripcion = editText2.getText().toString();

                String id = mDataBase.push().getKey();   // generando un id para la tarea



                Tarea tarea = new Tarea(id,titulo,descripcion,fecha,hora);

                mDataBase.child("Tareas").child(id).setValue(tarea); // Agregando la tarea a la base de datos

              Toast toast=  Toast.makeText(getApplicationContext(),"Tarea guardada correctamente",Toast.LENGTH_LONG);

                toast.setMargin(400,300);
                toast.show();

                editText1.setText("");
                editText2.setText("");
                editText2.setHint(R.string.descripcion);
                textView5.setText("Presione aqui para agregar la fecha ");
                textView3.setText("Presione aqui para agregar la hora");



                // Aqui es el evento de aceptar
                Intent intent = new Intent(Agregar.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });





        textView5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

              Calendar  calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
               int currentMinute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(Agregar.this, new TimePickerDialog.OnTimeSetListener() {
                    String amPm;
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        textView5.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

            }
        });

        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Agregar.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d("Agregar", "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                String date = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
                textView3.setText(date);
                //textView3.setText("sjgdfgsdgfshdkhk");
            }
        };

        /*
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                Agregar.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

            }
        };*/







    }





}
