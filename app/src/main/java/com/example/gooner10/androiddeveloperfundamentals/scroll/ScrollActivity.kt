package com.example.gooner10.androiddeveloperfundamentals.scroll

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import com.example.gooner10.androiddeveloperfundamentals.R

class ScrollActivity : AppCompatActivity() {
    companion object {
        const val REPLY = "Reply"
        private val TAG = ScrollActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll)
    }

    override fun onBackPressed() {
        Log.d(TAG, "onBackPressed")
        val intent = Intent()
        intent.putExtra(REPLY, "From onBackPressed")
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item != null) {
            Log.d(TAG, "" + item.itemId)
            Log.d(TAG, "onNavigateUp")
            val intent = Intent().apply {
                putExtra(REPLY, "From onOptionsItemSelected")
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}
