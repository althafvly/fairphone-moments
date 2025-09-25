/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.util

import android.util.Base64
import android.webkit.WebView

/**
 * Hide the Header and the Footer of the page displayed in the WebView.
 */
//todo check behavior when final link is ready
fun WebView.hideHeaderAndFooter() {
    val css = """
        header,
        footer,
        .c-inline-section--newsletter,
        .fp_head_container,
        .fp_footer_container,
        .fp_main_navigation,
        .s_fp_sticky_navigation,
        .fp_sticky_navigation_container {
            display: none;
        }  
    """
    injectCss(css)
}

fun WebView.overrideYotpoCssRules() {
    val css = """
        .yotpo-widget-my-rewards-widget,
        .yotpo-is-mobile,
        .yotpo-grid,
        .fp_more_information_wrapper,
        .fp_modal_wrapper,
        .fp_backdrop {
            height: 100% !important;
        }
    """
    injectCss(css)
}

/**
 * Disable overflow to fix Phoenix web pages not opening using WebView.
 */
fun WebView.disableOverflow() {
    val css = """
        html,body {
            overflow: unset;
        }  
    """
    injectCss(css)
}

/**
 * Inject the given css code string into the receiving WebView.
 */
fun WebView.injectCss(css: String) {
    val encoded = Base64.encodeToString(css.toByteArray(), Base64.NO_WRAP)
    val script = "javascript:(function() {" +
            "var parent = document.getElementsByTagName('head').item(0);" +
            "var style = document.createElement('style');" +
            "style.type = 'text/css';" +
            "style.innerHTML = window.atob('$encoded');" +
            "parent.appendChild(style)" +
            "})()"
    loadUrl(script)
}

