package com.usedocetangerinaestoque.util

import java.util.Locale

fun String.title(): String =
    this.lowercase().split(" ").joinToString(" ") { word ->
        word.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    }

fun String.capitalizeLocale(locale: Locale = Locale.getDefault()): String =
    this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }