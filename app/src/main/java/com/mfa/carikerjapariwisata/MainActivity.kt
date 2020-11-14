package com.mfa.carikerjapariwisata

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mfa.carikerjapariwisata.utils.GlobalFunction
import com.mfa.carikerjapariwisata.views.ui.home.HomeFragment
import com.mfa.carikerjapariwisata.views.ui.job.JobFragment
import com.mfa.carikerjapariwisata.views.ui.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val homeFragment: Fragment = HomeFragment()
    private val jobFragment: Fragment = JobFragment()
    private val profileFragment: Fragment = ProfileFragment()
    private val fm: FragmentManager = supportFragmentManager
    private var active: Fragment = homeFragment
    private var globalFunction:GlobalFunction? = null

    companion object{
        val REQUEST_CODE = "1"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        globalFunction = GlobalFunction(this)

        if(intent.getStringExtra("request_code") == REQUEST_CODE) {
            globalFunction?.createSnackBar(layout, "Selamat datang di aplikasi Cari Kerja Pariwisata", R.color.colorPrimary)
        }

        val navigation =
            findViewById<View>(R.id.nav_view) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        fm.beginTransaction().add(R.id.nav_host_fragment, profileFragment, "3").hide(profileFragment).commit()
        fm.beginTransaction().add(R.id.nav_host_fragment, jobFragment, "2").hide(jobFragment).commit()
        fm.beginTransaction().add(R.id.nav_host_fragment, homeFragment, "1").commit()
//        val navView: BottomNavigationView = findViewById(R.id.nav_view)
//
//        val navController = findNavController(R.id.nav_host_fragment)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        navView.setupWithNavController(navController)
    }

    private val mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
        object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.getItemId()) {
                    R.id.navigation_home -> {
                        fm.beginTransaction().hide(active).show(homeFragment).commit()
                        active = homeFragment
                        return true
                    }
                    R.id.navigation_job -> {
                        fm.beginTransaction().hide(active).show(jobFragment).commit()
                        active = jobFragment
                        return true
                    }
                    R.id.navigation_profile -> {
                        fm.beginTransaction().hide(active).show(profileFragment).commit()
                        active = profileFragment
                        return true
                    }
                }
                return false
            }
        }
}