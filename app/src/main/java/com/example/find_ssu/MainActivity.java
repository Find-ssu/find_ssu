package com.example.find_ssu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.find_ssu.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

   private static final String TAG_LOOK_FOR = "LookForFragment";
    private static final String TAG_HOME = "HomeFragment";
    private static final String TAG_MY_PAGE = "MyPageFragment";
    static final String TAG_FIND = "FindFragment";

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setFragment(TAG_HOME, new HomeFragment());
        binding.navigationView.setSelectedItemId(R.id.home_navi);

        binding.navigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.tab_home) {
                setFragment(TAG_HOME, new HomeFragment());
            } else if (item.getItemId() == R.id.tab_look_for) {
                setFragment(TAG_LOOK_FOR, new LookForFragment());
            } else if (item.getItemId() == R.id.tab_find) {
                setFragment(TAG_FIND, new FindFragment());
            } else if (item.getItemId() == R.id.tab_my_page) {
                setFragment(TAG_MY_PAGE, new MyPageFragment());
            }
            return true;
        });
    }

    protected void setFragment(String tag, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (fragmentManager.findFragmentByTag(tag) == null) {
            fragmentTransaction.add(R.id.home_navi, fragment, tag);
        }

        Fragment look_for = fragmentManager.findFragmentByTag(TAG_LOOK_FOR);
        Fragment home = fragmentManager.findFragmentByTag(TAG_HOME);
        Fragment my_page = fragmentManager.findFragmentByTag(TAG_MY_PAGE);
        Fragment find = fragmentManager.findFragmentByTag(TAG_FIND);

        if (look_for != null){
            fragmentTransaction.hide(look_for);
        }
        if (home != null){
            fragmentTransaction.hide(home);
        }
        if (my_page != null){
            fragmentTransaction.hide(my_page);
        }
        if (find != null){
            fragmentTransaction.hide(find);
        }

        if (!tag.equals(TAG_LOOK_FOR)) {
            if (tag.equals(TAG_HOME)) {
                if (home != null){
                    fragmentTransaction.show(home);
                }
            } else if (tag.equals(TAG_FIND)) {
                if (find != null){
                    fragmentTransaction.show(find);
                }
            } else {
                if (my_page != null){
                    fragmentTransaction.show(my_page);
                }
            }
        } else {
            if (look_for != null){
                fragmentTransaction.show(look_for);
            }
        }
        fragmentTransaction.commitAllowingStateLoss();
    }
}