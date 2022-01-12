package br.com.java.meupercursoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import br.com.java.meupercursoapp.adapter.ClienteAdapter;
import br.com.java.meupercursoapp.database.ClientesDbHelper;
import br.com.java.meupercursoapp.model.Cliente;

public class ClientListActivity extends AppCompatActivity {

    public static final String NEW_LIST_ID = "br.com.java.NEW_CLIENTS";

    private ClientesDbHelper db;
    private ClienteAdapter clienteAdapter;
    private ArrayList<Cliente> clientesParaVisita;
    private ArrayList<Cliente> todosClientes;
    private EditText txtBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);

        RecyclerView clienteExibir = (RecyclerView) findViewById(R.id.clientView);
        db = ClientesDbHelper.getDbHelper();
        clienteExibir.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        clientesParaVisita = (ArrayList<Cliente>) intent.getSerializableExtra(RouteLocationsActivity.LIST_ID);
        clienteExibir.setAdapter(clienteAdapter);
        txtBuscar = findViewById(R.id.textFind);

        txtBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filtrarClientes();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarClientes();
        filtrarClientes();
    }

    public void onBtnAddClienteClicar(View view) {
        Intent intent = new Intent(this, AddClientActivity.class);
        startActivity(intent);
    }

    public void retornarNovoCliente(Cliente cliente) {
        clientesParaVisita.add(cliente);

        Intent intent = new Intent();
        intent.putExtra(NEW_LIST_ID, clientesParaVisita);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void filtrarClientes() {
        ArrayList<Cliente> filtrado = new ArrayList<>();
        String chave = txtBuscar.getText().toString();

        for(Cliente c : todosClientes) {
            if (c.getNome().toLowerCase().contains(chave.toLowerCase())) {
                filtrado.add(c);
            }
        }
        clienteAdapter.setData(filtrado);
        clienteAdapter.notifyDataSetChanged();
    }

    public void carregarClientes() {
        todosClientes = db.getClientes();
    }
}