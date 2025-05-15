package com.fairphone.spring.launcher.util

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

import android.annotation.SuppressLint
import android.net.Uri
import android.net.http.SslError
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.PermissionRequest
import android.webkit.SslErrorHandler
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.isVisible
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.ui.theme.Color_FP_Brand_Forest
import com.fairphone.spring.launcher.ui.theme.Color_FP_Brand_Lime
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme

const val LOG_TAG = "FairphoneWebView"

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun FairphoneWebViewScreen(
    url: String,
    showCloseButton: Boolean = false,
    hideHeaderAndFooter: Boolean = true,
    disableOverflow: Boolean = true,
    disableUrlLoading: Boolean = false,
    vararg cssRules: String,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    var screenState: FairphoneWebViewScreenState by remember {
        mutableStateOf(FairphoneWebViewScreenState.Loading)
    }
    val webView: WebView by remember { mutableStateOf(WebView(context)) }

    val allowedFilePickerTypes by remember { mutableStateOf(arrayOf("image/*", "application/pdf")) }
    var filePickerCallback: ValueCallback<Array<Uri>>? by remember { mutableStateOf(null) }
    val filePicker =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { result ->
            if (result != null) {
                filePickerCallback?.onReceiveValue(arrayOf(result))
            }
        }

    BackHandler {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            onBackPressed()
        }
    }

    LaunchedEffect(Unit) {
        Log.d(LOG_TAG, "Opening url in WebView: $url")
        webView.apply {
            isVisible = false
            settings.apply {
                domStorageEnabled = true
                loadsImagesAutomatically = true
                allowFileAccess = true
                allowContentAccess = true
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                javaScriptEnabled = true
                useWideViewPort = true
                loadWithOverviewMode = true
                mediaPlaybackRequiresUserGesture = true

            }

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    Log.d(LOG_TAG, "Loading url: ${request?.url?.toString()}")
                    return when {
                        url == request?.url?.toString() -> false
                        else -> disableUrlLoading
                    }
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    view?.apply {
                        if (hideHeaderAndFooter) {
                            hideHeaderAndFooter()
                        }
                        if (disableOverflow) {
                            disableOverflow()
                        }
                        cssRules.forEach { rule ->
                            injectCss(rule)
                        }
                        if (url?.contains("keep-club-fairphone") == true) {
                            overrideYotpoCssRules()
                        }
                    }
                    super.onPageFinished(view, url)
                    isVisible = true
                    screenState = FairphoneWebViewScreenState.Success
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)
                    Log.d(LOG_TAG, "Error: ${error?.errorCode}, description: ${error?.description}")
                }

                override fun onReceivedHttpError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    errorResponse: WebResourceResponse?
                ) {
                    super.onReceivedHttpError(view, request, errorResponse)
                    Log.d(LOG_TAG, "HTTP Error: ${errorResponse?.reasonPhrase}")
                }

                override fun onReceivedSslError(
                    view: WebView?,
                    handler: SslErrorHandler?,
                    error: SslError?
                ) {
                    Log.d(LOG_TAG, "SSL error: ${error?.primaryError}, url: ${error?.url}")
                    handler?.cancel()
                }
            }
            webChromeClient = object : WebChromeClient() {
                override fun onConsoleMessage(message: ConsoleMessage): Boolean {
                    Log.d(LOG_TAG, "${message.message()} -- From line ${message.lineNumber()} of ${message.sourceId()}")
                    return true
                }

                override fun onPermissionRequest(request: PermissionRequest?) {
                    request?.grant(arrayOf(PermissionRequest.RESOURCE_PROTECTED_MEDIA_ID))
                }

                override fun onShowFileChooser(
                    webView: WebView?,
                    filePathCallback: ValueCallback<Array<Uri>>?,
                    fileChooserParams: FileChooserParams?
                ): Boolean {
                    filePickerCallback = filePathCallback
                    filePicker.launch(allowedFilePickerTypes)
                    return true
                }
            }
            loadUrl(url)
        }
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr)
                )
        ) {
            when (screenState) {
                is FairphoneWebViewScreenState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {

                    }
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        trackColor = Color_FP_Brand_Forest,
                        color = Color_FP_Brand_Lime
                    )
                }

                is FairphoneWebViewScreenState.Success -> {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { webView }
                    )
                    if (showCloseButton) {
                        val colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White,
                        )
                        Button(
                            colors = colors,
                            onClick = onBackPressed,
                            enabled = true,
                            shape = RectangleShape,
                            elevation = ButtonDefaults.buttonElevation(),
                            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 14.dp),
                            interactionSource = remember { MutableInteractionSource() },
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 24.dp, start = 32.dp, end = 32.dp)
                                .fillMaxWidth()
                                .height(56.dp),
                        ) {
                            Text(
                                text = stringResource(R.string.close),
                                lineHeight = 20.8.sp,
                                color = colors.contentColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}

sealed class FairphoneWebViewScreenState {
    data object Loading : FairphoneWebViewScreenState()
    data object Success : FairphoneWebViewScreenState()
}

@Composable
@Preview
fun WebViewScreen_Preview() {
    SpringLauncherTheme {
        FairphoneWebViewScreen(
            url = "",
            onBackPressed = {}
        )
    }
}