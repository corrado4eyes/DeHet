package com.corrado4eyes.dehet.delegates

import android.util.Log
import com.corrado4eyes.dehet.util.Constants
import com.corrado4eyes.dehet.util.doInBackground
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.IOException

class ViewModelDelegate {
    companion object {
        private const val TAG = "ViewModelDelegate"
    }

    private suspend fun createDocument(word: String): Document = doInBackground {
        val url = "${Constants.url}/$word"
        Log.d(TAG, url)
        return@doInBackground Jsoup.connect(url).get()
    }

    suspend fun getArticle(word: String): String = doInBackground {
        var fullResult: String? = null
        try {
            val document = createDocument(word)
            val articleElement: Element = document.getElementById("content")
            val wordElement = articleElement.getElementsByTag("h1").first()
            fullResult = wordElement.text()
        }catch (e: IOException) {
            Log.d(TAG, "There is an error ${e.message}")
            throw(e)
        }
        return@doInBackground fullResult ?: "No word found."
    }
}