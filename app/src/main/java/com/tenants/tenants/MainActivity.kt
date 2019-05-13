package com.tenants.tenants

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.tenants.tenants.Fragments.*
import com.tenants.tenants.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    HomeFragment.OnFragmentInteractionListener,
    BillsFragment.OnFragmentInteractionListener,
    DebtsFragment.OnFragmentInteractionListener,
    ShoppingListFragment.OnFragmentInteractionListener,
    CleaningFragment.OnFragmentInteractionListener

{
    lateinit var billsFragment: BillsFragment
    lateinit var cleaningFragment: CleaningFragment
    lateinit var debtsFragment: DebtsFragment
    lateinit var homeFragment: HomeFragment
    lateinit var shoppingListFragment: ShoppingListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        homeFragment = HomeFragment.newInstance("", "")
        debtsFragment = DebtsFragment.newInstance("", "")
        shoppingListFragment = ShoppingListFragment.newInstance("", "")
        billsFragment = BillsFragment.newInstance("", "")
        cleaningFragment = CleaningFragment.newInstance("", "")

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, homeFragment)
            .commit()

        nav_view.setCheckedItem(R.id.sidebar_home)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_logout -> {
                SharedPrefManager.getInstance(applicationContext).clear()

                val intent = Intent(applicationContext, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.sidebar_home -> {
                switchFragment(homeFragment)
            }
            R.id.sidebar_debts -> {
                switchFragment(debtsFragment)
            }
            R.id.sidebar_shopping_list -> {
                switchFragment(shoppingListFragment)
            }
            R.id.sidebar_bills -> {
                switchFragment(billsFragment)
            }
            R.id.sidebar_cleaning -> {
                switchFragment(cleaningFragment)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onFragmentInteraction(uri: Uri) {}

    fun switchFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(fragment.toString())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }
}
