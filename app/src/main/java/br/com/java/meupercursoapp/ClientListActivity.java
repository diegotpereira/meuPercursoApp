package br.com.java.meupercursoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ClientListActivity extends AppCompatActivity {

    public static final String NEW_LIST_ID = "br.com.java.meupercursoapp.NEW_CLIENTS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);
    }
}