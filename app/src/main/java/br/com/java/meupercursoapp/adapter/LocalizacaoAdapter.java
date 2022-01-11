package br.com.java.meupercursoapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.com.java.meupercursoapp.R;
import br.com.java.meupercursoapp.model.Cliente;

public class LocalizacaoAdapter extends RecyclerView.Adapter<LocalizacaoAdapter.ViewHolder> {

    private ArrayList<Cliente> data;

    public ArrayList<Cliente> getData() {
        return data;
    }
    public void setData(ArrayList<Cliente> data) {
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtLocalizacao;
        private Button btnDeletar;

        public TextView getTxtLocalizacao() {
            return txtLocalizacao;
        }

        public Button getBtnDeletar() {
            return btnDeletar;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtLocalizacao = itemView.findViewById(R.id.txtLocalizacao);
            btnDeletar = itemView.findViewById(R.id.btnDeletar);
        }
    }

    public LocalizacaoAdapter(ArrayList<Cliente> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public LocalizacaoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_localizacao, parent, false);

        return new LocalizacaoAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalizacaoAdapter.ViewHolder holder, final int position) {
        holder.getTxtLocalizacao().setText(data.get(position).getNome());
        holder.getBtnDeletar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.remove(position);
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
