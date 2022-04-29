package com.mariomunozmyaplication.gestionatunegocio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.mariomunozmyaplication.gestionatunegocio.home.HomeFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    public FloatingActionButton fab;
    private Intent intent;


    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    //variable del fragment detalle
   // MyApplication myApplication= (MyApplication) this.getApplication();

    //AÑADIR DATOS
    private ActivityResultLauncher<Intent> miActivityResultLauncher;
    //private DatosRegistro persona;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fab = findViewById(R.id.fab);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        //lo sgt se implementa luego de haber implementado NavigationView.OnNavigationItemSelectedListener
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        //cargar fragment principal en la actividad
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();


        //CAMBIAR POR PRODUCTOS
        fragmentTransaction.add(R.id.container_fragment, new HomeFragment());
        fragmentTransaction.commit();


        //MUESTRA EN EL APP BAR EN QUE FRAGMENT ESTAMOS
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_pedidos, R.id.nav_empleados,R.id.nav_datos,R.id.nav_contacto , R.id.nav_logout)
                .setDrawerLayout(drawerLayout)
                .build();


    }

    //MODIFICA EL navHEADER
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        tvEmailUsuario = findViewById(R.id.tvEmailUsuario);
        tvNombreEmpresa = findViewById(R.id.tvNombreEmpresa);
        //Mostramos el nombre de la empresa y el correo en el menu draw
        tvEmailUsuario.setText(LoginActivity.user.getEmail());
        String nombreEmpresa = LoginActivity.user.getDisplayName();
        if (nombreEmpresa != null && !nombreEmpresa.equals("")) {
            tvNombreEmpresa.setText(nombreEmpresa);
        }


        return true;
    }


    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //para cerrar automaticamente el menu
        drawerLayout.closeDrawer(GravityCompat.START);
        int id = menuItem.getItemId();
        menuItem.setChecked(true);
        switch (id) {
            case R.id.nav_home:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, new HomeFragment());
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                fragmentTransaction.commit();
                break;
            case R.id.nav_pedidos:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, new HomeFragment());
                Toast.makeText(this, "Productos", Toast.LENGTH_SHORT).show();
                fragmentTransaction.commit();
                menuItem.setChecked(true);
                break;
            case R.id.nav_empleados:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, new HomeFragment());
                Toast.makeText(this, "Pedidos", Toast.LENGTH_SHORT).show();
                fragmentTransaction.commit();
                menuItem.setChecked(true);
                break;
            case R.id.nav_datos:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, new HomeFragment());
                Toast.makeText(this, "Empleados", Toast.LENGTH_SHORT).show();
                fragmentTransaction.commit();
                break;
            case R.id.nav_contacto:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, new HomeFragment());
                Toast.makeText(this, "Datos de Usuario", Toast.LENGTH_SHORT).show();
                fragmentTransaction.commit();
                break;
            case R.id.nav_logout:
                intent = new Intent(this, LoginActivity.class);
                Toast.makeText(this, "Saliendo", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
                break;
            default:
                return true;
        }
        return true;
    }
}
