package br.com.java.meupercursoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.util.Pair;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.InvalidRequestException;
import com.google.maps.model.TravelMode;

import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.Duration;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.TravelMode;

import java.util.ArrayList;
import java.util.List;

import br.com.java.meupercursoapp.database.ClientesDbHelper;
import br.com.java.meupercursoapp.databinding.ActivityMapsBinding;
import br.com.java.meupercursoapp.model.Cliente;
import br.com.java.meupercursoapp.model.MeiosDeTransporte;
import br.com.java.meupercursoapp.model.Opcoes;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    // https://github.com/MichalSzafinski/MyWay

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationManager locationManager;
    private DrawerLayout mDrawerLayout;
    private LatLng ClienteLocalizacao = new LatLng(40, 40);
    private ArrayList<Cliente> clientesParaVisitar;
    private Opcoes opcoes = new Opcoes(MeiosDeTransporte.Carro, true, true);

    public static final int LOCATIONS_REQUEST = 2;
    public static final int OPTIONS_REQUEST = 3;
    private String[] RotaEnderecos = new String[] {"Aleje Jerozolimskie", "Obozowa Warszawa", "Koszykowa Warszawa", "Stadion Narodowy"};
    public String IniciarEndereco = null;
    public String PararEndereco = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtenha o SupportMapFragment e seja notificado quando o mapa estiver pronto para ser usado.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        definirNavBar();

        ClientesDbHelper db = ClientesDbHelper.initDbHelper(getApplicationContext());
        clientesParaVisitar = new ArrayList<>();
    }

    private void AlternarAtividadeLocaisRota() {
        Intent intent = new Intent(this, RouteLocationsActivity.class);
        intent.putExtra(RouteLocationsActivity.LIST_ID, clientesParaVisitar);

        startActivityForResult(intent, LOCATIONS_REQUEST);
    }

    private void MudarParaOpcoes() {
        Intent intent = new Intent(this, OptionsActivity.class);
        intent.putExtra(OptionsActivity.OPTIONS_ID, opcoes);

        startActivityForResult(intent, OPTIONS_REQUEST);
    }

    private void a(String aa) {

        Toast.makeText(this, aa, Toast.LENGTH_LONG).show();
    }

    private void definirNavBar() {
        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // definir item como selecionado para persistir destaque
                        item.setChecked(true);

                        // fechar o drawer quando o item for tocado
                        mDrawerLayout.closeDrawers();

                        // Adicione o código aqui para atualizar a interface do usuário com base no item selecionado
                        // Por exemplo, troque os fragmentos da interface do usuário aqui
                        a(item.getTitle().toString());

                        // CLIQUES DOS ITENS DO MENU
                        if (item.getTitle().toString().contains("Client list")) {
                            AlternarAtividadeLocaisRota();
                        }
                        if (item.getTitle().toString().contains("Opcoes")) {
                            MudarParaOpcoes();
                        }
                        return true;
                    }
                }
        );
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        setTitle("Meu Caminho");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

        }

        Intent intent = new Intent(this, RouteLocationsActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        while (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            ClienteLocalizacao = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10));
                        }
                    }
                });
        mMap.setMyLocationEnabled(true);
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Nullable
            @Override
            public View getInfoWindow(@NonNull Marker marker) {
                return null;
            }

            @Nullable
            @Override
            public View getInfoContents(@NonNull Marker marker) {
                Context mContext = getApplicationContext();

                LinearLayout info = new LinearLayout(mContext);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(mContext);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(mContext);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
    }

    public void onClicaBtn(View view) {

        try {
            ExibirRotaMaisCurta();
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        }

    public void ExibirRotaMaisCurta() {
        //Verificando se as opções estão corretas
        int listaPontosPassagem = 0;

        if (!opcoes.isInciarAtual())
            listaPontosPassagem++;

        if (!opcoes.isPararAtual())
            listaPontosPassagem++;

        if (clientesParaVisitar.size() < listaPontosPassagem || clientesParaVisitar.size() <= 1) {

            Toast.makeText(this, "Poucos endereços fornecidos!", Toast.LENGTH_LONG).show();

            return;
        }
        mMap.clear();

        String geoApiKey = getString(R.string.google_maps_key);

        GeoApiContext context = new GeoApiContext.Builder().apiKey(geoApiKey).build();

        String iniciar = "", parar = "";

        ArrayList<String> pontosDePassagem = new ArrayList<String>();
        ArrayList<Cliente> ClientePercurso = new ArrayList<Cliente>();

        for (int index = 0; index < clientesParaVisitar.size(); index++) {
            if (index == 0 && !opcoes.isInciarAtual())
                iniciar = clientesParaVisitar.get(index).getEndereco();

            else if (index == clientesParaVisitar.size() - 1 && !opcoes.isPararAtual())
                parar = clientesParaVisitar.get(index).getEndereco();

            else {
                pontosDePassagem.add(clientesParaVisitar.get(index).getEndereco());
                clientesParaVisitar.add(clientesParaVisitar.get(index));
            }
        }
        if (!opcoes.isInciarAtual() && iniciar.length() < 2 || !opcoes.isPararAtual() && parar.length() < 2) {

            Toast.makeText(this, "O endereço fornecido não é suficientemente preciso!", Toast.LENGTH_LONG).show();

            return;
        }

        RotaEnderecos = new String[pontosDePassagem.size()];
        RotaEnderecos = pontosDePassagem.toArray(RotaEnderecos);

        List<LatLng> path = new ArrayList();
        List<Pair<LatLng, String>> marcas = new ArrayList<>();

        long HoraJornada = 0;
        int DuracaoJornada = 0;
        int[] ordemPontoPassagem = new int[0];

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location != null) {
                                ClienteLocalizacao = new LatLng(location.getLatitude(), location.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                            }

                        }
                    });
            if (opcoes.isInciarAtual())
                iniciar = Double.toString(ClienteLocalizacao.latitude) + ',' + Double.toString(ClienteLocalizacao.longitude);
            if (opcoes.isPararAtual())
                parar = Double.toString(ClienteLocalizacao.latitude) + ',' + Double.toString(ClienteLocalizacao.longitude);

            DirectionsApiRequest req = DirectionsApi.getDirections(context, iniciar, parar);
            req.optimizeWaypoints(true);
            req.waypoints(RotaEnderecos);

            if (opcoes.getMeiosDeTransporte() == MeiosDeTransporte.Bicicleta)
                req.mode(TravelMode.BICYCLING);
            if (opcoes.getMeiosDeTransporte() == MeiosDeTransporte.Caminhar)
                req.mode(TravelMode.WALKING);

            DirectionsResult res = req.await();
            ordemPontoPassagem = new int[pontosDePassagem.size()];

            if (res.routes != null  && res.routes.length > 0){
                DirectionsRoute route = res.routes[0];
                ordemPontoPassagem = route.waypointOrder;

                if (route.legs != null) {
                    marcas.add(new Pair<>(new LatLng(route.legs[0].startLocation.lat, route.legs[0].startLocation.lng), route.legs[0].startAddress));

                    for (int index = 0; index < route.legs.length; index++) {
                        DirectionsLeg leg = route.legs[index];
                        marcas.add(new Pair<>(new LatLng(route.legs[index].endLocation.lat, route.legs[index].endLocation.lng), route.legs[index].endAddress));

                        HoraJornada += route.legs[index].duration.inSeconds;
                        DuracaoJornada += route.legs[index].distance.inMeters;

                        if (leg.steps != null) {
                            for(int j = 0; j < leg.steps.length; j++) {
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length > 0) {
                                    for(int k = 0; k < step.steps.length; k++) {
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;

                                        if (points1 != null) {

                                            // Decodifique a polilinha e adicione pontos à lista de coordenadas da rota
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();

                                            for(com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        // Decodifique a polilinha e adicione pontos à lista de coordenadas da rota
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (com.google.maps.errors.InvalidRequestException ex) {

            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Exception ex) {

            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        if (path.size() > 0) {
            if (opcoes.isInciarAtual() && opcoes.isPararAtual()) marcas.remove(marcas.size() - 1);
            for(int i = 0; i < marcas.size(); i++) {
                LatLng l = marcas.get(i).first;
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(l);
                markerOptions.title("Posição na estrada: " + Integer.toString(i));
                String snippet = marcas.get(i).second;

                try {
                    if (i == 0 && !opcoes.isInciarAtual() && clientesParaVisitar.get(0).getTelefone().length() > 2)
                        snippet += String .format("\n" + "Tel : %s", clientesParaVisitar.get(0).getTelefone());
                    else if(opcoes.isInciarAtual() && i > 0 && i - 1 < ordemPontoPassagem.length && ClientePercurso.get(ordemPontoPassagem[i - 1]).getTelefone().length() > 2)
                        snippet += String.format("\n" + "TEL : %s", clientesParaVisitar.get(ordemPontoPassagem[i - 1]).getTelefone());
                    else if (!opcoes.isInciarAtual() && i > 0 && i - 1 < ordemPontoPassagem.length && clientesParaVisitar.get(ordemPontoPassagem[i - 1]).getTelefone().length() > 2)
                        snippet += String.format("\n" + "TEL : %s", clientesParaVisitar.get(ordemPontoPassagem[i - 1]).getTelefone());
                    else if (i == marcas.size()-1 && !opcoes.isPararAtual() && clientesParaVisitar.get(clientesParaVisitar.size()-1).getTelefone().length()>2)
                        snippet += String.format("\n" + "Tel : %s", clientesParaVisitar.get(clientesParaVisitar.size()-1).getTelefone());
                } catch (Exception ex) {
                    Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
                }
                markerOptions.snippet(snippet);
                mMap.addMarker(markerOptions);
            }
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
            mMap.addPolyline(opts);

            Toast.makeText(this,"Estimated time : " + FormatTimeFromSeconds(HoraJornada) + "\nDistance : " + FormatDistanceFromMeters(DuracaoJornada) , Toast.LENGTH_LONG).show();
        }
    }

    public void onClientListClick(View view) {
        Intent intent = new Intent(this, ClientListActivity.class);
        startActivity(intent);
    }
    public static String FormatTimeFromSeconds(long longVal) {
        int horas = (int) longVal / 3600;
        int restante = (int) longVal - horas * 3600;
        int mins = restante / 60;
        restante = restante - mins * 60;
        int secs = restante;

        return String.format("%02d:%02d:%02d", horas, mins, secs);
    }

    public static String FormatDistanceFromMeters(int metros) {
        int km = metros/1000;
        metros = metros - km * 1000;

        return km + ", " + metros + " km";
    }
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            ClienteLocalizacao = new LatLng(location.getLatitude(), location.getLongitude());
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {}

    };

    @Override
    protected void onDestroy() {
        ClientesDbHelper.getDbHelper().close();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATIONS_REQUEST ) {
            if (resultCode == RESULT_OK) {
                clientesParaVisitar = (ArrayList<Cliente>) data.getSerializableExtra(RouteLocationsActivity.LIST_ID);
            }
        } else if (requestCode == OPTIONS_REQUEST) {
            if (resultCode == RESULT_OK) {
                opcoes = (Opcoes) data.getSerializableExtra(OptionsActivity.OPTIONS_ID);
            }
        }
    }
}