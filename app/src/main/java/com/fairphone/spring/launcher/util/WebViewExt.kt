/*
 * Copyright (C) 2025 Fairphone B.V.
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

package com.fairphone.spring.launcher.util

import android.util.Base64
import android.webkit.WebView
import kotlin.text.toByteArray

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

