package com.mariomunozmyaplication.gestionatunegocio;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Declaramos variables
    private AppBarConfiguration mAppBarConfiguration;
    private TextView tvEmailUsuario, tvNombreEmpresa;
    private Intent intent;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    public FloatingActionButton fab;
    Toolbar toolbar;

    // Metodo onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_pedidos, R.id.nav_empleados, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
        intent = getIntent();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("loge", "atrass");
    }

    // Metodo para modificar el NavHeader
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        tvEmailUsuario = findViewById(R.id.tvEmailUsuario);
        tvNombreEmpresa = findViewById(R.id.tvNombreEmpresa);

        // Mostramos el nombre de la empresa y el correo en el menu draw
        tvEmailUsuario.setText(LoginActivity.user.getEmail());
        String nombreEmpresa = LoginActivity.user.getDisplayName();
        if (nombreEmpresa != null && !nombreEmpresa.equals("")) {
            tvNombreEmpresa.setText(nombreEmpresa);
        }
        return true;
    }


    // Opciones menú 3 puntitos
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                intent = new Intent(this, ConfguracionActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Metodo para el navigation controle los fragment
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // Opciones menú desplegable
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_home);
                drawer.closeDrawers();
                return true;
            case R.id.nav_pedidos:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_pedidos);
                drawer.closeDrawers();
                return true;
            case R.id.nav_empleados:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_empleados);
                drawer.closeDrawers();
                return true;
            case R.id.nav_contacto:
                intent = new Intent(MainActivity.this, ContactosActivity.class);
                startActivity(intent);
                return true;
            case R.id.nav_logout:
                LoginActivity.auth.signOut();
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return false;
    }
}