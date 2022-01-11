package br.com.java.meupercursoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import br.com.java.meupercursoapp.model.Opcoes;

public class OptionsActivity extends AppCompatActivity {

    private Opcoes opcoes;

    public static final String OPTIONS_ID = "br.com.java.meupercursoapp.model.OPCOES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
    }
}