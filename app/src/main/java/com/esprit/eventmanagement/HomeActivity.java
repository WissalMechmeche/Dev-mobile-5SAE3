package com.esprit.eventmanagement;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;


import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomeActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        sessionManager = new SessionManager(this); // Initialiser SessionManager
        // Configurer le Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Définit le toolbar comme ActionBar

        // Vérifier si l'utilisateur est connecté
        if (!sessionManager.isLoggedIn()) {
            // Si aucun utilisateur n'est connecté, rediriger vers LoginActivity
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            // L'utilisateur est connecté, afficher ses informations ou continuer l'activité
            String email = sessionManager.getUserEmail();
            // Utiliser l'email pour afficher des informations personnalisées
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Configure NavController avec le NavHostFragment
        NavController navController = Navigation.findNavController(this, R.id.fragment_content_main);

        // Associer le BottomNavigationView au NavController
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Configurer l'AppBar avec le NavController
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_events, R.id.nav_blog, R.id.nav_reservations).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);





        // Vérifier si l'utilisateur est connecté
        if (!sessionManager.isLoggedIn()) {
            // Si aucun utilisateur n'est connecté, rediriger vers LoginActivity
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            // L'utilisateur est connecté, afficher ses informations ou continuer l'activité
            String email = sessionManager.getUserEmail();
            // Utiliser l'email pour afficher des informations personnalisées
        }




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.pink)); // Mauve color
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragment_content_main);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate le menu ; ceci ajoute des éléments à l'action bar si elle est présente.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_logout) {
            // Appeler la méthode logout pour déconnecter l'utilisateur
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        sessionManager.logout();
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    }

