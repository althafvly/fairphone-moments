package com.fairphone.spring.launcher.util

fun String.sanitizeToId(): String {
    return this.trim() // Remove leading/trailing whitespace
        .lowercase() // Convert to lowercase
        .replace(Regex("[^a-z0-9]"), "-") // Replace non-alphanumeric characters with "-"
        .replace(Regex("-+"), "-") // Replace multiple dashes with a single dash
        .trim('-') // Remove leading/trailing dashes
}