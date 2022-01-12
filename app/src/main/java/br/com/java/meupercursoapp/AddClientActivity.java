package br.com.java.meupercursoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import br.com.java.meupercursoapp.database.ClientesDbHelper;
import br.com.java.meupercursoapp.model.Cliente;

public class AddClientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
    }

    public void onBtnSalvarClicar(View view) {
        ClientesDbHelper db = ClientesDbHelper.getDbHelper();
        String nome = ((EditText) findViewById(R.id.txtName)).getText().toString();
        String endereco = ((EditText)findViewById(R.id.txtAddress)).getText().toString();
        String telefone = ((EditText)findViewById(R.id.txtPhone)).getText().toString();
        db.addCliente(new Cliente(nome, endereco, telefone));
        finish();
    }
}