/*
 * Copyright 2020, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.dessertclicker

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import com.example.android.dessertclicker.databinding.ActivityMainBinding

const val TAG = "MainActivity"
const val KEY_REVENUE = "revenue_key"
const val KEY_DESERT_SOLD = "dessert_sold_key"

class MainActivity : AppCompatActivity() {

    private var revenue = 0
    private var dessertsSold = 0

    // Contains all the views
    private lateinit var binding: ActivityMainBinding

    /** Dessert Data **/

    /**
     * Simple data class that represents a dessert. Includes the resource id integer associated with
     * the image, the price it's sold for, and the startProductionAmount, which determines when
     * the dessert starts to be produced.
     */
    data class Dessert(val imageId: Int, val price: Int, val startProductionAmount: Int)

    // Create a list of all desserts, in order of when they start being produced
    private val allDesserts = listOf(
            Dessert(R.drawable.cupcake, 5, 0),
            Dessert(R.drawable.donut, 10, 2),
            Dessert(R.drawable.eclair, 15, 4),
            Dessert(R.drawable.froyo, 30, 6),
            Dessert(R.drawable.gingerbread, 50, 8),
            Dessert(R.drawable.honeycomb, 100, 10),
            Dessert(R.drawable.icecreamsandwich, 500, 11),
            Dessert(R.drawable.jellybean, 1000, 12),
            Dessert(R.drawable.kitkat, 2000, 13),
            Dessert(R.drawable.lollipop, 3000, 14),
            Dessert(R.drawable.marshmallow, 4000, 15),
            Dessert(R.drawable.nougat, 5000, 16),
            Dessert(R.drawable.oreo, 6000, 17)
    )
    private var currentDessert = allDesserts[0]

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        logDebug("onSaveInstance Called")
        outState.putInt(KEY_REVENUE, revenue)
        outState.putInt(KEY_DESERT_SOLD, dessertsSold)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logDebug("onCreate called")

        // Use Data Binding to get reference to the views
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.dessertButton.setOnClickListener {
            onDessertClicked()
        }

        // Set the TextViews to the right values
        binding.revenue = revenue
        binding.amountSold = dessertsSold

        // Make sure the correct dessert is showing
        binding.dessertButton.setImageResource(currentDessert.imageId)

        if (savedInstanceState != null) {
            revenue = savedInstanceState.getInt(KEY_REVENUE, 0)
            dessertsSold = savedInstanceState.getInt(KEY_DESERT_SOLD, 0)
            showCurrentDessert()
        }
    }

    override fun onStart() {
        super.onStart()
        logDebug("onStart called")
    }

    override fun onRestart() {
        super.onRestart()
        logDebug("onRestart called")
    }

    override fun onResume() {
        super.onResume()
        logDebug("onResume called")
    }

    override fun onPause() {
        super.onPause()
        logDebug("onPause called")
    }

    override fun onStop() {
        super.onStop()
        logDebug("onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        logDebug("onDestroy called")
    }
    private fun logDebug(log: String) {
        Log.d(TAG, log)
    }

    /**
     * Updates the score when the dessert is clicked. Possibly shows a new dessert.
     */
    private fun onDessertClicked() {

        // Update the score
        revenue += currentDessert.price
        dessertsSold++

        binding.revenue = revenue
        binding.amountSold = dessertsSold

        // Show the next dessert
        showCurrentDessert()
    }

    /**
     * Determine which dessert to show.
     */
    private fun showCurrentDessert() {
        var newDessert = allDesserts[0]
        for (dessert in allDesserts) {
            if (dessertsSold >= dessert.startProductionAmount) {
                newDessert = dessert
            }
            // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
            // you'll start producing more expensive desserts as determined by startProductionAmount
            // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
            // than the amount sold.
            else break
        }

        // If the new dessert is actually different than the current dessert, update the image
        if (newDessert != currentDessert) {
            currentDessert = newDessert
            binding.dessertButton.setImageResource(newDessert.imageId)
        }
    }

    /**
     * Menu methods
     */
    private fun onShare() {
        val shareIntent = ShareCompat.IntentBuilder.from(this)
                .setText(getString(R.string.share_text, dessertsSold, revenue))
                .setType("text/plain")
                .intent
        try {
            startActivity(shareIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(this, getString(R.string.sharing_not_available),
                    Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.shareMenuButton -> onShare()
        }
        return super.onOptionsItemSelected(item)
    }
}
