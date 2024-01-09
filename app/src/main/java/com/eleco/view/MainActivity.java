package com.eleco.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.eleco.R;
import com.eleco.view.dashboard.fragment.HomeFragment;
import com.eleco.view.dashboard.fragment.KomunitasFragment;
import com.eleco.view.dashboard.fragment.PoinFragment;
import com.eleco.view.dashboard.fragment.ProfilFragment;
import com.eleco.view.dashboard.fragment.SetorFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;

    private BottomNavigationView bottomNavigationView;

    private HomeFragment homeFragment;
    private SetorFragment setorFragment;
    private KomunitasFragment komunitasFragment;
    private PoinFragment poinFragment;
    private ProfilFragment profileFragment;

    private Fragment activeFragment;
    private Map<Integer, Fragment> fragmentMap = new HashMap<>();
    private static final int REQUEST_CODE_PICK_LOCATION = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        homeFragment = new HomeFragment();
        setorFragment = new SetorFragment();
        komunitasFragment = new KomunitasFragment();
        poinFragment = new PoinFragment();
        profileFragment = new ProfilFragment();
        fragmentMap.put(R.id.home, homeFragment);
        fragmentMap.put(R.id.setor, setorFragment);
        fragmentMap.put(R.id.komunitas, komunitasFragment);
        fragmentMap.put(R.id.tukarpoin, poinFragment);
        fragmentMap.put(R.id.profile, profileFragment);

        activeFragment = homeFragment;

        setFragment(activeFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = fragmentMap.get(item.getItemId());
            setFragment(selectedFragment);
            return true;
        });
    }

    private void setFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tekan back sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }


}