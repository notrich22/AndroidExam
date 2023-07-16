package com.example.androidbankingexam.settings

import android.content.Context

class SharedPreferencesAppSettings (
    appContext: Context
): AppSettings {

    private val sharedPreferences = appContext.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)

    override fun saveText(text: String) {
        sharedPreferences.edit().putString(TEXT_KEY, text).apply()
    }

    override fun getText(): String {
        return sharedPreferences.getString(TEXT_KEY, "Нет текста") ?: ""
    }

    companion object{
        const val TEXT_KEY = "TEXT_KEY"
    }
}