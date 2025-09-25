/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.util

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.drawToBitmap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

// Helper classes for providing necessary owners to ComposeView when off-screen

private class OffScreenLifecycleOwner : LifecycleOwner {
    private val lifecycleRegistry = LifecycleRegistry(this)
    init {
        // Initialize in a state that allows composition and is active
        lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
        lifecycleRegistry.currentState = Lifecycle.State.RESUMED
    }
    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    fun destroy() {
        // Transition to destroyed to clean up resources
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }
}

private class OffScreenSavedStateRegistryOwner(lifecycleOwner: LifecycleOwner) : SavedStateRegistryOwner {
    private val _lifecycle: Lifecycle = lifecycleOwner.lifecycle // Bind to the provided lifecycle
    private val controller = SavedStateRegistryController.create(this)

    init {
        // Restore state (even if empty) to complete initialization
        controller.performRestore(null) // Pass null for no previously saved state
    }

    override val lifecycle: Lifecycle
        get() = _lifecycle

    override val savedStateRegistry: SavedStateRegistry
        get() = controller.savedStateRegistry

    /**
     * Call this if you need to save the state, though for simple off-screen capture
     * this might not be necessary unless composables use saveable state.
     */
    fun performSave(outBundle: Bundle) {
        controller.performSave(outBundle)
    }
}

private class OffScreenViewModelStoreOwner : ViewModelStoreOwner {
    private val _viewModelStore = ViewModelStore()
    override val viewModelStore: ViewModelStore
        get() = _viewModelStore

    fun clear() {
        _viewModelStore.clear() // Clear any ViewModels that might have been created
    }
}


/**
 * Utility object for capturing Jetpack Compose composables as Bitmaps
 * and saving Bitmaps as PNG files.
 */
object WallpaperUtils {

    const val TAG = "WallpaperUtils"

    /**
     * Captures a Jetpack Compose composable content as a Bitmap.
     *
     * The composable is rendered off-screen. You can either let the composable
     * determine its own size (if it has intrinsic content size like Text or a Modifier.size())
     * or provide specific target dimensions in pixels.
     *
     * If target dimensions are provided, the composable will be constrained to that size.
     * If not, `UNSPECIFIED` measure specs are used, and the composable should define its own size.
     *
     * Important Considerations:
     * - If the composable contains asynchronous content (e.g., images loaded from network via AsyncImage),
     * it might be captured before the content is fully loaded. You might need to implement
     * custom logic to await content readiness for such cases.
     * - This function should ideally be called from the main thread if it interacts with Views,
     * though `ComposeView` creation and `setContent` are generally main-thread safe.
     * The measurement, layout, and drawing are View operations.
     *
     * @param context The Android Context.
     * @param targetWidthPx Optional target width for the bitmap in pixels. If null, content's intrinsic width is used.
     * @param targetHeightPx Optional target height for the bitmap in pixels. If null, content's intrinsic height is used.
     * @param composableContent The composable function to capture.
     * @return A Bitmap representation of the composable, or null if capture fails (e.g., due to zero size).
     */
    suspend fun captureComposableAsBitmap(
        context: Context,
        targetWidthPx: Int? = null,
        targetHeightPx: Int? = null,
        composableContent: @Composable () -> Unit
    ): Bitmap? = withContext(Dispatchers.Main) {
        val composeView = ComposeView(context)

        // Create and set the necessary owners for off-screen composition
        val lifecycleOwner = OffScreenLifecycleOwner()
        val savedStateRegistryOwner = OffScreenSavedStateRegistryOwner(lifecycleOwner)
        val viewModelStoreOwner = OffScreenViewModelStoreOwner()

        // These are crucial for ComposeView to initialize its composition context correctly
        // when not attached to a window.
        composeView.setViewTreeLifecycleOwner(lifecycleOwner)
        composeView.setViewTreeSavedStateRegistryOwner(savedStateRegistryOwner)
        composeView.setViewTreeViewModelStoreOwner(viewModelStoreOwner)

        // Set a composition strategy that disposes the composition when the lifecycle is destroyed.
        composeView.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(lifecycleOwner))

        var bitmap: Bitmap? = null
        try {
            // Set the composable content. This will trigger composition.
            composeView.setContent(composableContent)

            // Determine measure specs
            val widthMeasureSpec = if (targetWidthPx != null && targetWidthPx > 0) {
                View.MeasureSpec.makeMeasureSpec(targetWidthPx, View.MeasureSpec.EXACTLY)
            } else {
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            }

            val heightMeasureSpec = if (targetHeightPx != null && targetHeightPx > 0) {
                View.MeasureSpec.makeMeasureSpec(targetHeightPx, View.MeasureSpec.EXACTLY)
            } else {
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            }

            // Measure the ComposeView
            composeView.measure(widthMeasureSpec, heightMeasureSpec)

            val measuredWidth = composeView.measuredWidth
            val measuredHeight = composeView.measuredHeight

            if (measuredWidth <= 0 || measuredHeight <= 0) {
                Log.e(
                    TAG,
                    "Composable measured to non-positive dimensions ($measuredWidth x $measuredHeight). " +
                            "Ensure it has defined dimensions (e.g., Modifier.size()), " +
                            "or valid target dimensions are provided, " +
                            "or its content has an intrinsic size."
                )
                null
            }

            // Layout the ComposeView
            composeView.layout(0, 0, measuredWidth, measuredHeight)

            // Capture the view as a bitmap
            // Ensure the view is laid out and drawn before capturing.
            // For complex UIs, a short delay or waiting for idle might be needed,
            // but usually measure/layout is sufficient.
            bitmap = composeView.drawToBitmap(Bitmap.Config.ARGB_8888)

        } catch (e: Exception) {
            Log.e(TAG, "Error capturing composable to bitmap: ${e.message}", e)
            null // Ensure null is returned on error
        } finally {
            // Clean up: Destroy the lifecycle owner to dispose of the composition and free resources.
            // This is important to avoid leaks (Recomposer, etc.).
            lifecycleOwner.destroy()
            viewModelStoreOwner.clear() // Clear any ViewModels
            // SavedStateRegistryOwner's resources are managed by its controller and lifecycle.
        }
        bitmap
    }

    /**
     * Saves a Bitmap as a PNG file.
     *
     * By default, it saves to the app's internal cache directory, which requires no special permissions.
     * You can modify the path to save elsewhere (e.g., app-specific external storage) if needed.
     *
     * @param context The Android Context.
     * @param bitmap The Bitmap to save.
     * @param directory The directory where the file should be saved. Defaults to app's cache directory.
     * @param fileName The desired name for the PNG file (e.g., "my_image.png").
     * A timestamp is appended by default to ensure uniqueness if not specified.
     * @return The File object representing the saved PNG, or null if saving fails.
     */
    fun saveBitmapAsPng(
        context: Context,
        bitmap: Bitmap,
        directory: File = context.cacheDir, // Or context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        fileName: String = "composable_capture_${System.currentTimeMillis()}.png"
    ): File? {
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val imageFile = File(directory, fileName)

        return try {
            FileOutputStream(imageFile).use { fos ->
                // Compress bitmap to PNG format, 100 quality means lossless for PNG.
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.flush()
            }
            imageFile // Return the file object on success
        } catch (e: IOException) {
            System.err.println("Error saving bitmap as PNG: ${e.message}")
            e.printStackTrace()
            null
        }
    }
}

// Example Usage (within a Composable or Activity/Fragment):
/*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

// In your Composable function where you want to trigger the capture:
@Composable
fun MyScreen() {
    val context = LocalContext.current

    // Example composable to capture
    @Composable
    fun MyComposableToCapture(modifier: Modifier = Modifier) {
        Box(
            modifier = modifier
                .background(Color.Yellow)
                .padding(16.dp)
        ) {
            Text("Hello, Capture!", color = Color.Black, fontSize = 20.sp)
        }
    }

    // Button or some trigger to perform capture
    // Button(onClick = {
    //     // Option 1: Capture with intrinsic size (if MyComposableToCapture defines its own size well)
    //     // For this to work well, MyComposableToCapture might need a Modifier.size() or wrap content.
    //     val bitmapIntrinsic = ComposableCaptureUtils.captureComposableAsBitmap(context) {
    //         MyComposableToCapture(Modifier.wrapContentSize()) // or Modifier.size(width, height)
    //     }
    //     bitmapIntrinsic?.let { bmp ->
    //         Log.d("CaptureTest", "Intrinsic Bitmap captured: ${bmp.width}x${bmp.height}")
    //         val file = ComposableCaptureUtils.saveBitmapAsPng(context, bmp, fileName = "intrinsic_capture.png")
    //         file?.let { Log.d("CaptureTest", "Saved to: ${it.absolutePath}") }
    //             ?: Log.e("CaptureTest", "Failed to save intrinsic capture.")
    //     } ?: Log.e("CaptureTest", "Failed to capture intrinsic bitmap.")


    //     // Option 2: Capture with a specific size in pixels
    //     // Convert dp to pixels for target dimensions
    //     val density = LocalDensity.current.density
    //     val targetWidthPx = (250 * density).roundToInt()
    //     val targetHeightPx = (100 * density).roundToInt()

    //     val bitmapSpecificSize = ComposableCaptureUtils.captureComposableAsBitmap(
    //         context = context,
    //         targetWidthPx = targetWidthPx,
    //         targetHeightPx = targetHeightPx
    //     ) {
    //         // This composable will be forced into the targetWidthPx x targetHeightPx bounds.
    //         // If it uses Modifier.fillMaxSize(), it will fill these bounds.
    //         MyComposableToCapture(Modifier.fillMaxSize())
    //     }

    //     if (bitmapSpecificSize != null) {
    //         Log.d("CaptureTest", "Specific Size Bitmap captured: ${bitmapSpecificSize.width}x${bitmapSpecificSize.height}")
    //         // Save it as a PNG
    //         val imageFile = ComposableCaptureUtils.saveBitmapAsPng(context, bitmapSpecificSize, fileName = "specific_size_capture.png")
    //         if (imageFile != null) {
    //             Log.d("CaptureTest", "PNG saved to: ${imageFile.absolutePath}")
    //             // You can now use this file (e.g., share it, upload it)
    //         } else {
    //             Log.e("CaptureTest", "Failed to save specific size PNG.")
    //         }
    //     } else {
    //         Log.e("CaptureTest", "Failed to capture specific size composable as Bitmap.")
    //     }
    // }) {
    //     Text("Capture Composable")
    // }
}
*/
