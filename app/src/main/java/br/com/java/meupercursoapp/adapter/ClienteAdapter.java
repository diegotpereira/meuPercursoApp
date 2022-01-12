package br.com.java.meupercursoapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.com.java.meupercursoapp.ClientListActivity;
import br.com.java.meupercursoapp.R;
import br.com.java.meupercursoapp.database.ClientesDbHelper;
import br.com.java.meupercursoapp.model.Cliente;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ViewHolder> {

    private ArrayList<Cliente> data;
    private ArrayList<Cliente> clientesParaVisitar;
    private ClientListActivity clientListActivity;

    // Classe Banco
    private ClientesDbHelper db;

    public ArrayList<Cliente> getData() {
        return data;
    }

    public void setData(ArrayList<Cliente> data) {
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private Button btnCliente;
        private Button btnRemoveDoDb;

        public Button getBtnCliente() {
            return btnCliente;
        }

        public Button getBtnRemoveDoDb() {
            return btnRemoveDoDb;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            btnCliente = itemView.findViewById(R.id.btnCliente);
            btnRemoveDoDb = itemView.findViewById(R.id.btnRemoveDoDb);
        }
    }

    public ClienteAdapter(ArrayList<Cliente> data, ArrayList<Cliente> clientesParaVisitar, ClientListActivity clientListActivity) {
        this.data = data;
        this.clientesParaVisitar = clientesParaVisitar;
        this.clientListActivity = clientListActivity;
        this.db = ClientesDbHelper.getDbHelper();


    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_cliente, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Button clienteBtn = holder.getBtnCliente();
        clienteBtn.setText(data.get(position).getNome());

        clienteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cliente clienteNaPosicao = data.get(position);
                clientListActivity.retornarNovoCliente(clienteNaPosicao);
            }
        });
        Button btnRemoveDoDb = holder.getBtnRemoveDoDb();
        btnRemoveDoDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cliente clienteNaPosicao = data.get(position);
                db.deletarCliente(clienteNaPosicao);
                data.remove(position);
                notifyDataSetChanged();
                clientListActivity.carregarClientes();
            }
        });
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
}
