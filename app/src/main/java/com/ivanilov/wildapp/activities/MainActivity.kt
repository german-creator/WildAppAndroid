package com.ivanilov.wildapp.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ivanilov.wildapp.R
import com.ivanilov.wildapp.models.Product
import com.ivanilov.wildapp.models.ProductInBasket
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var sPref: SharedPreferences
    lateinit var basketList: ArrayList<ProductInBasket>
    lateinit var navView: BottomNavigationView
    private lateinit var groupList: ArrayList<String>
    private lateinit var productList: ArrayList<Product>

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navView = findViewById(R.id.nav_view)

        sPref = getPreferences(MODE_PRIVATE);

        val firstStart = sPref.getBoolean("firstStart", true)

        if (firstStart) {
            sPref.edit().putBoolean("firstStart", false).commit()
            val checkInIntent = Intent(this, CheckInActivity::class.java)
            startActivity(checkInIntent)
        }

        basketList = ArrayList()
        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_menu,
                R.id.navigation_order,
                R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        supportActionBar?.hide()

    }

    fun addProductToBasket(product: ProductInBasket) {
        basketList.add(product)
        updateBadge()
    }

    fun updateProductInBasket(product: ProductInBasket, position: Int) {
        basketList.set(position, product)
        updateBadge()
    }
    fun clearBasket() {
        basketList = ArrayList()
        updateBadge()
    }

    fun getProductsInBasket(): ArrayList<ProductInBasket> {
        return basketList
    }

    fun getGroupList(): ArrayList<String> {

        if (this::groupList.isInitialized){
            return groupList
        } else
            return ArrayList()
    }

    fun getProductList(): ArrayList<Product> {
        if (this::productList.isInitialized){
            return productList
        } else
            return ArrayList()
    }

    fun setGroupList(group: ArrayList<String>) {
        groupList = group
    }

    fun setProductList(product: ArrayList<Product>) {
        productList = product
    }


    fun updateBadge() {

        if (basketList.size != 0) {
            var basketSize = 0
            basketList.forEach {
                basketSize += it.count
            }

            navView.getOrCreateBadge(R.id.navigation_order).apply {
                backgroundColor = ContextCompat.getColor(this@MainActivity, R.color.colorAzure)
                badgeTextColor = ContextCompat.getColor(this@MainActivity, R.color.colorWhite)
                maxCharacterCount = 200
                number = basketSize
                isVisible = true
            }
        } else {
            navView.getOrCreateBadge(R.id.navigation_order).apply {
                backgroundColor = ContextCompat.getColor(this@MainActivity, R.color.colorAzure)
                badgeTextColor = ContextCompat.getColor(this@MainActivity, R.color.colorWhite)
                maxCharacterCount = 200
                number = 0
                isVisible = false
            }
        }

    }

    override fun onBackPressed() {

        val fragmentManager: FragmentManager = this@MainActivity.supportFragmentManager

        val cardFragment = fragmentManager.findFragmentByTag("cardBottomDialog")
        val aboutUsFragment = fragmentManager.findFragmentByTag("aboutUsFragment")
        val userAgreementFragment = fragmentManager.findFragmentByTag("userAgreementFragment")

        if (cardFragment != null && cardFragment.isVisible) {
            (cardFragment as CardFragment).dismiss()
            nav_view.visibility = View.VISIBLE
            return
        }

        if (aboutUsFragment != null && aboutUsFragment.isVisible) {
            (aboutUsFragment as AboutUsFragment).dismiss()
            nav_view.visibility = View.VISIBLE
            return
        }

        if (userAgreementFragment != null && userAgreementFragment.isVisible) {
            (userAgreementFragment as UserAgreementFragment).dismiss()
            nav_view.visibility = View.VISIBLE
            return
        }

        super.onBackPressed()


    }


}
