package br.com.java.meupercursoapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.com.java.meupercursoapp.R;
import br.com.java.meupercursoapp.model.Cliente;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ViewHolder> {

    private ArrayList<Cliente> data;
    private ArrayList<Cliente> clientesParaVisitar;
    // Classe Banco

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

    public ClienteAdapter(ArrayList<Cliente> data, ArrayList<Cliente> clientesParaVisitar) {
        this.data = data;
        this.clientesParaVisitar = clientesParaVisitar;

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
                // Cliente Activity
            }
        });
        Button btnRemoveDoDb = holder.getBtnRemoveDoDb();
        btnRemoveDoDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cliente clienteNaPosicao = data.get(position);
                notifyDataSetChanged();

                // Activity
            }
        });
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
}
