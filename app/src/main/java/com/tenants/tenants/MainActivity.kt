package com.tenants.tenants

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.tenants.tenants.fragments.*
import com.tenants.tenants.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    HomeFragment.OnFragmentInteractionListener,
    BillsFragment.OnFragmentInteractionListener,
    DebtsFragment.OnFragmentInteractionListener,
    ShoppingListFragment.OnFragmentInteractionListener,
    CleaningFragment.OnFragmentInteractionListener,
    ShoppingListDetailsFragment.OnFragmentInteractionListener,
    ShoppingListAddFragment.OnFragmentInteractionListener,
    UsersFragment.OnFragmentInteractionListener


{
    lateinit var billsFragment: BillsFragment
    lateinit var cleaningFragment: CleaningFragment
    lateinit var usersFragment: UsersFragment
    lateinit var debtsFragment: DebtsFragment
    lateinit var homeFragment: HomeFragment
    lateinit var shoppingListFragment: ShoppingListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val headerView = nav_view.getHeaderView(0)
        val navGroupName = headerView.findViewById<TextView>(R.id.sidebar_group_name)
        navGroupName.setText(SharedPrefManager.getInstance(applicationContext).groupName)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        homeFragment = HomeFragment.newInstance()

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
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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
        when (item.itemId) {
            R.id.sidebar_home -> {
                homeFragment = HomeFragment.newInstance()
                switchFragment(homeFragment)
            }
            R.id.sidebar_apartment -> {
                usersFragment = UsersFragment.newInstance()
                switchFragment(usersFragment)
            }
            R.id.sidebar_debts -> {
                debtsFragment = DebtsFragment.newInstance()
                switchFragment(debtsFragment)
            }
            R.id.sidebar_shopping_list -> {
                shoppingListFragment = ShoppingListFragment.newInstance()
                switchFragment(shoppingListFragment)
            }
            R.id.sidebar_bills -> {
                billsFragment = BillsFragment.newInstance()
                switchFragment(billsFragment)
            }
            R.id.sidebar_cleaning -> {
                cleaningFragment = CleaningFragment.newInstance()
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
