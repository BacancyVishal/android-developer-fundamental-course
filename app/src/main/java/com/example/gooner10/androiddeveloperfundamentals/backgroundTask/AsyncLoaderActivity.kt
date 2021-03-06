package com.example.gooner10.androiddeveloperfundamentals.backgroundTask

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.gooner10.androiddeveloperfundamentals.R
import com.example.gooner10.androiddeveloperfundamentals.backgroundTask.model.Books
import kotlinx.android.synthetic.main.activity_async_loader.*

class AsyncLoaderActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Books> {
    // Class name for Log tag.
    private val LOG_TAG = AsyncLoaderActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_async_loader)

        //Check if a Loader is running, if it is, reconnect to it
        if (supportLoaderManager.getLoader<Any>(0) != null) {
            supportLoaderManager.initLoader(0, null, this)
        }
    }

    /**
     * Gets called when the user pushes the "Search Books" button
     *
     * @param view The view (Button) that was clicked.
     */
    fun searchBooks(view: View) {
        // Get the search string from the input field.
        val queryString = bookInput!!.text.toString()

        // Hide the keyboard when the button is pushed.
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)

        // Check the status of the network connection.
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo

        // If the network is active and the search field is not empty,
        // add the search term to the arguments Bundle and start the loader.
        if (networkInfo != null && networkInfo.isConnected && queryString.isNotEmpty()) {
            authorText!!.text = ""
            titleText!!.setText(R.string.loading)
            val queryBundle = Bundle()
            queryBundle.putString("queryString", queryString)
            supportLoaderManager.restartLoader(0, queryBundle, this)
        } else {
            if (queryString.isEmpty()) {
                authorText!!.text = ""
                titleText!!.setText(R.string.no_search_term)
            } else {
                authorText!!.text = ""
                titleText!!.setText(R.string.no_network)
            }
        }// Otherwise update the TextView to tell the user there is no connection or no search term.
    }

    /**
     * Loader Callbacks
     */

    /**
     * The LoaderManager calls this method when the loader is created.
     *
     * @param id ID integer to id   entify the instance of the loader.
     * @param args The bundle that contains the search parameter.
     * @return Returns a new BookLoader containing the search term.
     */
    override fun onCreateLoader(id: Int, args: Bundle): Loader<Books> {
        return BookLoader(this, args.getString("queryString")!!)
    }

    /**
     * Called when the data has been loaded. Gets the desired information from
     * the JSON and updates the Views.
     *
     * @param loader The loader that has finished.
     * @param data The JSON response from the Books API.
     */
    override fun onLoadFinished(loader: Loader<Books>?, books: Books?) {
        try {
            Log.d(LOG_TAG, "books: " + books)

            // If both are found, display the result.
            if (books != null) {
                Log.d(LOG_TAG, "books.items: " + books.items)
                if (books.items?.get(0)?.volumeInfo!!.title != null && books.items?.get(0)?.volumeInfo!!.authors != null) {
                    titleText.text = books.items?.get(0)?.volumeInfo!!.title
                    authorText.text = books.items?.get(0)?.volumeInfo!!.authors?.get(0) ?: "No author"
                    bookInput.setText("")
                } else {
                    // If none are found, update the UI to show failed results.
                    titleText!!.setText(R.string.no_results)
                    authorText!!.text = ""
                }
            }

        } catch (e: Exception) {
            // If onPostExecute does not receive a proper JSON string, update the UI to show failed results.
            titleText.setText(R.string.no_results)
            authorText.text = ""
            Log.e(LOG_TAG, e.toString())
        }


    }

    /**
     * In this case there are no variables to clean up when the loader is reset.
     *
     * @param loader The loader that was reset.
     */
    override fun onLoaderReset(loader: Loader<Books>) {}
}
