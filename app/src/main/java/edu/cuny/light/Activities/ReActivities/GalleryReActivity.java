package edu.cuny.light.Activities.ReActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.cuny.light.R;

public class GalleryReActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_re);

        BottomNavigationView botNav=findViewById(R.id.bottom_nav_bar);
        botNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction() .replace(R.id.fragment_container,new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            switch(menuItem.getItemId()){
                case R.id.nav_home:
                    selectedFragment=new HomeFragment();
                    break;
                case R.id.nav_create:
                    selectedFragment=new ShareFragment();
                    break;
                case R.id.nav_portal:
                    selectedFragment=new PortalFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction() .replace(R.id.fragment_container,selectedFragment).commit();
            return true;
        }
    };
}
