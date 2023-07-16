package com.example.androidbankingexam

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.util.UUID

class Add_card : Fragment() {
    private lateinit var listView: ListView
    private lateinit var cardsList: MutableList<Card>
    private lateinit var adapter: BaseAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_card, container, false)

        listView = view.findViewById(R.id.listViewCards)

        cardsList = getAllCards()

        adapter = object : BaseAdapter() {
            override fun getCount(): Int {
                return cardsList.size
            }

            override fun getItem(position: Int): Any {
                return cardsList[position]
            }

            override fun getItemId(position: Int): Long {
                return position.toLong()
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val itemView: View = convertView ?: LayoutInflater.from(parent?.context).inflate(R.layout.list_item_card, parent, false)

                val card = cardsList[position]

                val textViewCardName: TextView = itemView.findViewById(R.id.textViewCardName)
                val buttonDeleteCard: Button = itemView.findViewById(R.id.buttonDeleteCard)

                textViewCardName.text = card.getName()

                buttonDeleteCard.setOnClickListener {
                    val last4Numbers = card.getLast4Numbers()
                    removeCard(last4Numbers)
                }

                return itemView
            }
        }

        listView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonAddCard: Button = view.findViewById(R.id.buttonAddCard)
        val editTextName: EditText = view.findViewById(R.id.editTextName)
        val editTextLast4Numbers: EditText = view.findViewById(R.id.editTextLast4Numbers)

        cardsList = getAllCards()

        val adapter = object : BaseAdapter() {
            override fun getCount(): Int {
                return cardsList.size
            }

            override fun getItem(position: Int): Any {
                return cardsList[position]
            }

            override fun getItemId(position: Int): Long {
                return position.toLong()
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val itemView: View = convertView ?: LayoutInflater.from(parent?.context).inflate(R.layout.list_item_card, parent, false)

                val card = cardsList[position]

                val textViewCardName: TextView = itemView.findViewById(R.id.textViewCardName)
                val buttonDeleteCard: ImageButton = itemView.findViewById(R.id.buttonDeleteCard)

                textViewCardName.text = card.getName()

                buttonDeleteCard.setOnClickListener {
                    val last4Numbers = card.getLast4Numbers()
                    removeCard(last4Numbers)

                    cardsList.remove(card)

                    notifyDataSetChanged()
                }

                return itemView
            }
        }

        listView.adapter = adapter

        buttonAddCard.setOnClickListener {
            val cardName = editTextName.text.toString()
            val last4Numbers = editTextLast4Numbers.text.toString()

            if (cardName.isNotEmpty() && last4Numbers.length == 4 ) {
                val newCard = Card(UUID.randomUUID().toString(), cardName, last4Numbers, 0.0)

                saveCard(newCard)

                cardsList.add(newCard)

                adapter.notifyDataSetChanged()

                editTextName.text.clear()
                editTextLast4Numbers.text.clear()
            } else {
                Toast.makeText(requireContext(), "Please enter valid details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun saveCard(card: Card) {
        val sharedPreferences = context?.getSharedPreferences("Cards", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()

        val id = card.getId()

        val cardJson = Gson().toJson(card)

        editor?.putString(id, cardJson)
        editor?.apply()
    }

    fun getAllCards(): MutableList<Card> {
        val sharedPreferences = context?.getSharedPreferences("Cards", Context.MODE_PRIVATE)
        val cards: MutableList<Card> = mutableListOf()

        val allEntries = sharedPreferences?.all ?: return cards

        for (entry in allEntries.entries) {
            val cardJson = entry.value as? String ?: continue

            try {
                Log.d("DEBUG CARD", cardJson)
                val card = Gson().fromJson(cardJson, Card::class.java)
                cards.add(card)
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
            }
        }

        return cards
    }

    fun removeCard(key: String) {
        val sharedPreferences = context?.getSharedPreferences("Cards", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.remove(key)
        editor?.apply()
    }
    override fun onDestroyView() {
        saveAllCards()
        super.onDestroyView()
    }
    fun saveAllCards() {
        val sharedPreferences = context?.getSharedPreferences("Cards", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()

        editor?.clear()

        for (card in cardsList) {
            val id = card.getId()
            val cardJson = Gson().toJson(card)
            editor?.putString(id, cardJson)
        }

        editor?.apply()
    }
    companion object {
        @JvmStatic
        fun newInstance() =
            Add_card().apply {
                arguments = Bundle().apply {
                }
            }
    }
}