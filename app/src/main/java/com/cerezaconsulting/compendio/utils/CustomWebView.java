package com.cerezaconsulting.compendio.utils;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by junior on 05/04/17.
 */

public class CustomWebView extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
