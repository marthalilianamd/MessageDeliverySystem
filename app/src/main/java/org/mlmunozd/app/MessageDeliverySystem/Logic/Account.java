package org.mlmunozd.app.MessageDeliverySystem.Logic;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;

import org.mlmunozd.app.MessageDeliverySystem.Fragments.AboutFragment;
import org.mlmunozd.app.MessageDeliverySystem.Fragments.AccountFragment;
import org.mlmunozd.app.MessageDeliverySystem.Fragments.MensajesFragment;
import org.mlmunozd.app.MessageDeliverySystem.Fragments.MensajesPresenter;
import org.mlmunozd.app.MessageDeliverySystem.MainActivity;
import org.mlmunozd.app.MessageDeliverySystem.Persistence.SessionManager;
import org.mlmunozd.app.MessageDeliverySystem.R;
import org.mlmunozd.app.MessageDeliverySystem.Services.MensajeService;

import static org.mlmunozd.app.MessageDeliverySystem.Logic.Intro.SESSION_DATA;

public class Account extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public String email;
    public MensajesPresenter mMensajePresenter;
    public MensajesFragment mMensajesFragment;
    static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        View emailview = navigationView.getHeaderView(0);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("INFO PERMISSION SMS: ", "Permiso no otorgado");
            if(!(ActivityCompat.shouldShowRequestPermissionRationale(Account.this , Manifest.permission.SEND_SMS))) {
                Log.d("INFO PERMISSION SMS: ", "Autorizando ...");
                ActivityCompat.requestPermissions(Account.this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }

        Intent intent = getIntent();
        TextView textEmail = emailview.findViewById(R.id.textEmail);

        if ((intent.getStringExtra(MainActivity.SESSION_MESSAGE)) != null) {
            setDataSessionUser(intent, textEmail, MainActivity.SESSION_MESSAGE);
        }
        if ((intent.getStringExtra(Login.EXTRA_MESSAGE) != null)) {
            setDataSessionUser(intent, textEmail, Login.EXTRA_MESSAGE);
        } else {
            setDataSessionUser(intent, textEmail, SESSION_DATA);
        }
        navigationView.setNavigationItemSelectedListener(this);

        mMensajesFragment = new MensajesFragment();
        mMensajesFragment = (MensajesFragment) getFragmentManager().findFragmentById(R.id.mensajes_container);
        if (mMensajesFragment == null) {
            mMensajesFragment = MensajesFragment.newInstance();
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.mensajes_container, mMensajesFragment, "fragmento_mensajes")
                    .commit();
            mMensajePresenter = new MensajesPresenter(mMensajesFragment,FirebaseMessaging.getInstance());
        }


    }

    public void setDataSessionUser(Intent intent,TextView textEmail, String message){
        email = intent.getStringExtra(message);
        textEmail.setText(email);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        int id = item.getItemId();

        if (id == R.id.stop_service_mensajeria) {
            stopService(new Intent(getApplicationContext(), MensajeService.class));
            return true;
        }
        if (id == R.id.start_service_mensajeria) {
            startService(new Intent(getApplicationContext(), MensajeService.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.mensajes_container)).commit();

        if (id == R.id.nav_openMensajes) {
            mMensajesFragment = MensajesFragment.newInstance();
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.mensajes_container, mMensajesFragment, "fragmento_mensajes")
                    .commit();
            mMensajePresenter = new MensajesPresenter(mMensajesFragment,FirebaseMessaging.getInstance());
        }
        else if (id == R.id.nav_settings) {
            AccountFragment accountFragment = new AccountFragment();
            Bundle args = new Bundle();
            args.putString("email", email);
            accountFragment.setArguments(args);
            loadFragment(accountFragment,"fragmento_account");

        } else if (id == R.id.nav_about) {
            AboutFragment aboutFragment = new AboutFragment();
            loadFragment(aboutFragment, "fragmento_about");
        }
        else if (id == R.id.nav_logout) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setIcon(R.mipmap.applogo);
            dialog.setMessage("Realmente quiere cerrar sesión?");
            dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                SessionManager.getInstance(getApplicationContext()).logoutUser();
                Intent intent = new Intent(getApplicationContext(),Intro.class);
                startActivity(intent);
                finish();
                }
            });
            dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                MensajesFragment mensajesFragment = new MensajesFragment();
                loadFragment(mensajesFragment, "fragmento_mensajes");

                }
            }).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadFragment(final Fragment fragment, String tag){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.mensajes_container, fragment, tag)
                .setTransition(transaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    public void removeFragment(final Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}