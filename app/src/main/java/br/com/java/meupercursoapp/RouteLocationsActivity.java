package br.com.java.meupercursoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class RouteLocationsActivity extends AppCompatActivity {

    public static final String LIST_ID = "br.com.java.meupercursoapp.CLIENTS_TO_VISIT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_locations);
    }
}