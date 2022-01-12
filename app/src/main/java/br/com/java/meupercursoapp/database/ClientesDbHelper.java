package br.com.java.meupercursoapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import br.com.java.meupercursoapp.model.Cliente;

public class ClientesDbHelper extends SQLiteOpenHelper {

    public static class ClienteEntrada implements BaseColumns {
        public static final String TABELA_NOME = "clientes";
        public static final String COLUNA_NOME_NOME = "Nome";
        public static final String COLUNA_NOME_ENDERECO = "Endereco";
        public static final String COLUNA_NOME_TELEFONE = "Telefone";
    }

    public static final int DATABASE_VERSION = 1;
    public static final  String DATABASE_NOME = "clientes.db";

    private static final String SQL_CRIAR_ENTRADAS =
            "CREATE TABLE " + ClienteEntrada.TABELA_NOME + " (" +
                    ClienteEntrada._ID + " INTEGER PRIMARY KEY, " +
                    ClienteEntrada.COLUNA_NOME_ENDERECO + " TEXT," +
                    ClienteEntrada.COLUNA_NOME_TELEFONE + " TEXT," +
                    ClienteEntrada.COLUNA_NOME_NOME + " TEXT)";

    private static final String SQL_DELETE_ENTRADAS =
            "DROP TABLE IF EXISTS " + ClienteEntrada.TABELA_NOME;

    private static ClientesDbHelper dbHelper = null;

    public static ClientesDbHelper initDbHelper(Context context) {
        dbHelper = new ClientesDbHelper(context);

        return dbHelper;
    }

    public ClientesDbHelper(Context context) {
        super(context, DATABASE_NOME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CRIAR_ENTRADAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRADAS);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion
        );
    }

    public static ClientesDbHelper getDbHelper() {

        return dbHelper;
    }

    // Escrevendo dado
    public void addCliente(Cliente cliente) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ClienteEntrada.COLUNA_NOME_ENDERECO, cliente.getEndereco());
        values.put(ClienteEntrada.COLUNA_NOME_NOME, cliente.getNome());
        values.put(ClienteEntrada.COLUNA_NOME_TELEFONE, cliente.getTelefone());

        long id = db.insert(ClienteEntrada.TABELA_NOME, null, values);
        cliente.setId(id);
    }

    // Lendo dado
    public ArrayList<Cliente> getClientes() {
        SQLiteDatabase db = this.getReadableDatabase();

        String [] projecao = {
                ClienteEntrada._ID,
                ClienteEntrada.COLUNA_NOME_NOME,
                ClienteEntrada.COLUNA_NOME_ENDERECO,
                ClienteEntrada.COLUNA_NOME_TELEFONE
        };

        String ordemClassificacao = ClienteEntrada._ID + " ASC";

        Cursor cursor = db.query(ClienteEntrada.TABELA_NOME, projecao, null, null, null, null, ordemClassificacao);


        ArrayList<Cliente> clientes = new ArrayList<>();

        while(cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(ClienteEntrada._ID));
            String nome  = cursor.getString(cursor.getColumnIndexOrThrow(ClienteEntrada.COLUNA_NOME_NOME));
            String endereco = cursor.getString(cursor.getColumnIndexOrThrow(ClienteEntrada.COLUNA_NOME_ENDERECO));
            String telefone = cursor.getString(cursor.getColumnIndexOrThrow(ClienteEntrada.COLUNA_NOME_TELEFONE));

            clientes.add(new Cliente(id, nome, endereco, telefone));
        }
        return clientes;
    }

    public void deletarCliente(Cliente cliente) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selecao = ClienteEntrada._ID + " = ?";
        String [] args = { String.valueOf(cliente.getId())};

        db.delete(ClienteEntrada.TABELA_NOME, selecao, args);
    }
}
