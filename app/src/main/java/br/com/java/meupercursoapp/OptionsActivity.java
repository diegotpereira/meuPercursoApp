package br.com.java.meupercursoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.Hashtable;
import java.util.Map;

import br.com.java.meupercursoapp.model.MeiosDeTransporte;
import br.com.java.meupercursoapp.model.Opcoes;

public class OptionsActivity extends AppCompatActivity {

    private Opcoes opcoes;

    public static final String OPTIONS_ID = "br.com.java.meupercursoapp.model.OPCOES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner spinnerMean = findViewById(R.id.spinnerMean);
        Switch switchStartCurrent = findViewById(R.id.switchStartCurrent);
        Switch switchStopCurrent = findViewById(R.id.switchStopCurrent);
        final ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.meios_transporte, R.layout.support_simple_spinner_dropdown_item);
        spinnerMean.setAdapter(adapter);

        Intent intent = getIntent();
        opcoes = (Opcoes) intent.getSerializableExtra(OPTIONS_ID);

        Hashtable<MeiosDeTransporte, String> meanToStrDict = new Hashtable<>();
        meanToStrDict.put(MeiosDeTransporte.Carro, "Carro");
        meanToStrDict.put(MeiosDeTransporte.Bicicleta, "Bicicleta");
        meanToStrDict.put(MeiosDeTransporte.Caminhar, "Caminhar");

        final Hashtable<String, MeiosDeTransporte> strToMeanDict = new Hashtable<>();
        for(Map.Entry<MeiosDeTransporte, String> entry : meanToStrDict.entrySet()) {
            strToMeanDict.put(entry.getValue(), entry.getKey());
        }

        String mediaStr = meanToStrDict.get(opcoes.getMeiosDeTransporte());
        spinnerMean.setSelection(adapter.getPosition(mediaStr));
        switchStartCurrent.setChecked(opcoes.isInciarAtual());
        switchStopCurrent.setChecked(opcoes.isPararAtual());

        spinnerMean.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String media = (String) adapter.getItem(position);
                opcoes.setMeiosDeTransporte(strToMeanDict.get(media));
                atualizarOpcoes();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        switchStartCurrent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                opcoes.setInciarAtual(isChecked);
                atualizarOpcoes();
            }
        });
        switchStopCurrent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                opcoes.setPararAtual(isChecked);
                atualizarOpcoes();
            }
        });
    }
    private void atualizarOpcoes() {
        Intent intent = new Intent();
        intent.putExtra(OPTIONS_ID, opcoes);
        setResult(RESULT_OK, intent);
    }
}