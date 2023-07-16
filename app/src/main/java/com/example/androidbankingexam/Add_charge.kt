package com.example.androidbankingexam

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import com.example.androidbankingexam.HistoryItem
import com.example.androidbankingexam.R
import com.google.gson.Gson
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date
import java.util.UUID

class Add_charge : Fragment() {
    private lateinit var radioGroup: RadioGroup
    private lateinit var editTextAmount: EditText
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var selectedCard: Card
    private lateinit var cardList: MutableList<Card>
    private lateinit var historyItems: MutableList<HistoryItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_charge, container, false)
        sharedPreferences = requireContext().getSharedPreferences("History", Context.MODE_PRIVATE)
        cardList = getAllCards()
        historyItems = getAllHistoryItems()

        val cardSpinner: Spinner = view.findViewById(R.id.spinner_card)
        val cardAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cardList.map { it.getName() })
        cardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cardSpinner.adapter = cardAdapter

        radioGroup = view.findViewById(R.id.radioGroup)

        editTextAmount = view.findViewById(R.id.editTextAmount)

        cardSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedCard = cardList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        val addButton: Button = view.findViewById(R.id.buttonAddOperation)
        addButton.setOnClickListener {
            addOperation()
        }

        return view
    }





    private fun getAllCards(): MutableList<Card> {
        val sharedPreferences = context?.getSharedPreferences("Cards", Context.MODE_PRIVATE)
        val cards: MutableList<Card> = mutableListOf()

        val allEntries = sharedPreferences?.all ?: return cards

        for (entry in allEntries.entries) {
            val cardJson = entry.value as? String ?: continue

            val card = Gson().fromJson(cardJson, Card::class.java)
            cards.add(card)
        }

        return cards
    }

    private fun addOperation() {
        val selectedRadioButtonId = radioGroup.checkedRadioButtonId
        val radioButton: RadioButton? = view?.findViewById(selectedRadioButtonId)
        val isExpense = radioButton?.id == R.id.radioButtonExpense

        val amountText = editTextAmount.text.toString()
        if (amountText.isNotEmpty()) {
            val amount = amountText.toDouble()
            val newHI = HistoryItem(generateUniqueId(), amount, isExpense, selectedCard)
            historyItems.add(newHI)
            editTextAmount.text.clear()
            Toast.makeText(requireContext(), "Operation added successfully", Toast.LENGTH_SHORT).show()
            Log.d("DEBUG HI", newHI.toString())
            Log.d("DEBUG", sharedPreferences.all.toString())
        } else {
            Toast.makeText(requireContext(), "Please enter the amount", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generateUniqueId(): String {
        return UUID.randomUUID().toString()
    }

    private fun saveHistoryItem(historyItem: HistoryItem) {

        historyItems
    }

    override fun onDestroyView() {
        super.onDestroyView()

        saveAllOperations()
    }
    fun saveAllOperations() {
        val editor = sharedPreferences.edit()

        for (hi in historyItems) {
            val id = hi.id
            val cardJson = Gson().toJson(hi)
            editor?.putString(id, cardJson)
        }

        editor?.apply()
    }


    private fun getAllHistoryItems(): MutableList<HistoryItem> {
        val historyItems = mutableListOf<HistoryItem>()

        val allEntries = sharedPreferences.all

        for ((_, value) in allEntries) {
            if (value is String) {
                val historyItem = HistoryItem.fromString(value)
                if (historyItem != null) {
                    historyItems.add(historyItem)
                }
            }
        }

        return historyItems
    }



    companion object {
        @JvmStatic
        fun newInstance() = Add_charge()
    }
}
