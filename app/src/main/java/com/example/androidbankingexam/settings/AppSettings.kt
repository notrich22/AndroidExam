package com.example.androidbankingexam.settings

interface AppSettings {
    fun saveText(text: String)
    fun getText(): String
}