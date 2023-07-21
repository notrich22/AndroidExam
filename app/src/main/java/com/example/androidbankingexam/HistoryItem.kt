package com.example.androidbankingexam

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.time.chrono.ChronoLocalDateTime
import java.util.Calendar
import java.util.Date

data class HistoryItem(
    val id: String,
    var amount: Double,
    var isExpense: Boolean,
    val card: Card,
    var dateTime: Long = System.currentTimeMillis()
) {
    companion object {
        fun fromString(string: String): HistoryItem? {
            val parts = string.split(";")
            if (parts.size == 4) {
                val id = parts[0]
                val amount = parts[1].toDoubleOrNull()
                val isExpense = parts[2].toBooleanStrictOrNull()
                val cardId = parts[3]
                val card = Card("CARD ID", "NAME OF CARD", "3229")
                if (amount != null && isExpense != null && card != null) {
                    return HistoryItem(id, amount, isExpense, card)
                }
            }
            return null
        }


        fun getCardById(context: Context, cardId: String): Card? {
            return null;
        }
    }
}
