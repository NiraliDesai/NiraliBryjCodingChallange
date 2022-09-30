package com.nirali.niralibryjcodingchallange.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nirali.niralibryjcodingchallange.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import android.webkit.WebView
import android.widget.Toast
import com.nirali.niralibryjcodingchallange.R
import com.nirali.niralibryjcodingchallange.url.URLList


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // JSInterface
    object AndroidJSInterface {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load Home page on open application
        loadUrl(URLList.homeURL)

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.itemHome -> loadUrl(URLList.homeURL)
                R.id.itemShop -> loadUrl(URLList.shopURL)
                R.id.itemAccount -> loadUrl(URLList.accountURL)
                R.id.itemBag -> loadUrl(URLList.bagURL)
                else -> {
                }
            }
            true
        }

        // Burger menu will open Navigation from Webview's Menu
        binding.burgerMenu.setOnClickListener {
            loadJs(binding.webView)
        }
    }

    private fun loadUrl(strUrl: String) {
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.addJavascriptInterface(AndroidJSInterface, "Android")
        binding.webView.webViewClient = WebViewClient()
        binding.webView.loadUrl(strUrl)

    }

    private fun loadJs(webView: WebView) {
        // Get Burger Menu from webview and it's click
        webView.loadUrl(
            """javascript:(function f() {
        var btns = document.getElementsByTagName('button');
        for (var i = 0, n = btns.length; i < n; i++) {
          if (btns[i].getAttribute('aria-label') === 'Toggle navigation') {
            btns[i].click();
          }
        }
      })()"""
        )
    }

    inner class WebViewClient : android.webkit.WebViewClient() {
        override fun onPageStarted(view: WebView, url: String, favicon: android.graphics.Bitmap?) {
            super.onPageStarted(view, url, favicon)
            binding.progressBar.visibility = View.VISIBLE
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return false
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            binding.webView.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }
}