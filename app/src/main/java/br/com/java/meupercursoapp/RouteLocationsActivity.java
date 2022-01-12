package br.com.java.meupercursoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import br.com.java.meupercursoapp.adapter.LocalizacaoAdapter;
import br.com.java.meupercursoapp.model.Cliente;

public class RouteLocationsActivity extends AppCompatActivity {

    public static final String LIST_ID = "br.com.java.CLIENTS_TO_VISIT";
    private static final int ADD_LOCATION_REQUEST = 1;

    private ArrayList<Cliente> clientesParaVisitar;
    private LocalizacaoAdapter localizacaoAdapter;

    public void definirClientesParaVisitar(ArrayList<Cliente> l) {
        clientesParaVisitar = l;
        Intent intent = new Intent();
        intent.putExtra(LIST_ID, clientesParaVisitar);
        setResult(RESULT_OK, intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_locations);

        RecyclerView localizacaoExibir = (RecyclerView) findViewById(R.id.locationsView);
        Intent intent = getIntent();

        clientesParaVisitar = (ArrayList<Cliente>) intent.getSerializableExtra(LIST_ID);
        localizacaoAdapter = new LocalizacaoAdapter(clientesParaVisitar, this);
        localizacaoExibir.setAdapter(localizacaoAdapter);
        localizacaoExibir.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        localizacaoAdapter.notifyDataSetChanged();
    }

    public void onBtnAddLocalizacaoClicar(View view) {
        Intent intent = new Intent(this, ClientListActivity.class);
        intent.putExtra(LIST_ID, clientesParaVisitar);
        startActivityForResult(intent, ADD_LOCATION_REQUEST);
    }

    public void onButtonOkClick(View view) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_LOCATION_REQUEST) {
            if (resultCode == RESULT_OK) {
                clientesParaVisitar = (ArrayList<Cliente>) data.getSerializableExtra(ClientListActivity.NEW_LIST_ID);
                localizacaoAdapter.setData(clientesParaVisitar);
                localizacaoAdapter.notifyDataSetChanged();

                Intent intent = new Intent();
                intent.putExtra(LIST_ID, clientesParaVisitar);
                setResult(RESULT_OK, intent);
            }
        }
    }
}