package com.example.tasklist;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private TextView textView;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private String Titulo[];
    private String Fecha[];
    private String Hora[];
    private String Descripcion[];
    private LayoutInflater inflater;

    public CustomAdapter(Context context, String Titulo[], String Fecha[], String Hora[], String Descripcion[]){
        this.context = context;
        this.Titulo = Titulo;
        this.Fecha = Fecha;
        this.Hora = Hora;
        this.Descripcion = Descripcion;
    }

    @Override
    public int getCount() {
        return Titulo.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.listview_customadapter_activity, parent, false);

        textView = convertView.findViewById(R.id.textview1);
        textView1 = convertView.findViewById(R.id.textview2);
        textView2 = convertView.findViewById(R.id.textview3);
        textView3 = convertView.findViewById(R.id.textview4);
        ImageView imageView = convertView.findViewById(R.id.imageview1);

        textView.setText(Html.fromHtml("<b>" + Titulo[position].toUpperCase() + "</b>\n"));
        textView1.setText(Fecha[position]);
        try {
            textView2.setText(Descripcion[position].toLowerCase());
        }catch (ArrayIndexOutOfBoundsException ex){
            textView2.setText("\"SIN DESCRIPCION\"");
        }
        textView3.setText(Hora[position]);
        imageView.setImageResource(R.drawable.calendario);

        return convertView;
    }
}
