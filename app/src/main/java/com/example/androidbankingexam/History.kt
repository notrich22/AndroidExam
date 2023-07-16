package com.example.androidbankingexam

import android.content.Context
import android.content.SharedPreferences
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
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuView.ItemView
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.text.SimpleDateFormat
import java.util.Locale

class History : Fragment() {

    private lateinit var listView: ListView
    private lateinit var adapter: BaseAdapter
    private lateinit var historyItems: MutableList<HistoryItem>
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        sharedPreferences = requireContext().getSharedPreferences("History", Context.MODE_PRIVATE)
        listView = view.findViewById(R.id.listViewHistory)
        historyItems = getAllHistoryItems()
        adapter = object : BaseAdapter() {
            override fun getCount(): Int {
                return historyItems.size
            }

            override fun getItem(position: Int): Any {
                return historyItems[position]
            }

            override fun getItemId(position: Int): Long {
                return position.toLong()
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val itemView: View = convertView ?: LayoutInflater.from(parent?.context).inflate(R.layout.list_item_history, parent, false)

                val historyItem = historyItems[position]

                val textViewAmount: TextView = itemView.findViewById(R.id.textViewAmount)
                val textViewType: TextView = itemView.findViewById(R.id.textViewType)
                val textViewDateTime: TextView = itemView.findViewById(R.id.textViewDateTime)
                val textViewLastFourDigits: TextView = itemView.findViewById(R.id.textViewLastFourDigits)
                val buttonEdit: ImageButton = itemView.findViewById(R.id.buttonEdit)
                val buttonDelete: ImageButton = itemView.findViewById(R.id.buttonDelete)

                textViewAmount.text = historyItem.amount.toString()
                textViewType.text = if (historyItem.isExpense) "Expense" else "Income"
                Log.d("DEBUG", historyItem.dateTime.toString())
                val dateTimeString = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    .format(historyItem.dateTime)
                textViewDateTime.text = dateTimeString

                textViewLastFourDigits.text = historyItem.card.getLast4Numbers()

                buttonEdit.setOnClickListener {
                    context?.let { it -> showEditDialog(position, it, historyItem) }
                }

                buttonDelete.setOnClickListener {
                    removeHistoryItem(historyItem.id)
                    updateHistoryItems()
                    Toast.makeText(itemView.context, "Delete button clicked", Toast.LENGTH_SHORT).show()
                }

                return itemView
            }

        }

        listView.adapter = adapter

        return view
    }
    private fun removeHistoryItem(key: String){
        val sharedPreferences = context?.getSharedPreferences("History", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.remove(key)
        editor?.apply()
    }
    override fun onResume() {
        super.onResume()
        updateHistoryItems()
    }
    private fun showEditDialog(position: Int, context: Context, historyItem: HistoryItem) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_item, null)
        val editTextAmount: EditText = dialogView.findViewById(R.id.editTextAmount)
        val radioGroup: RadioGroup = dialogView.findViewById(R.id.radioGroup)
        val radioButtonIncome: RadioButton = dialogView.findViewById(R.id.radioButtonIncome)
        val radioButtonExpense: RadioButton = dialogView.findViewById(R.id.radioButtonExpense)

        editTextAmount.setText(historyItem.amount.toString())
        radioButtonIncome.isChecked = !historyItem.isExpense
        radioButtonExpense.isChecked = historyItem.isExpense

        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("Edit History Item")
            .setPositiveButton("Save") { dialog, _ ->
                val amount = editTextAmount.text.toString().toDoubleOrNull()
                val isExpense = radioGroup.checkedRadioButtonId == R.id.radioButtonExpense

                if (amount != null) {
                    val updatedItem = historyItems[position].copy(amount = amount, isExpense = isExpense)
                    historyItems[position] = updatedItem
                    adapter.notifyDataSetChanged()
                }

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        saveHistoryItems()
    }

    private fun saveHistoryItems() {
        val sharedPreferences = context?.getSharedPreferences("History", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()

        editor?.clear()

        for (hi in historyItems) {
            val id = hi.id
            val hiJson = Gson().toJson(hi)
            editor?.putString(id, hiJson)
        }

        editor?.apply()
    }

    private fun updateHistoryItems() {
        historyItems.clear()
        historyItems.addAll(getAllHistoryItems())
        historyItems.sortBy { it.dateTime }
        Log.d("DEBUG HI", "History Items: $historyItems")
        adapter.notifyDataSetChanged()
    }


    private fun getAllHistoryItems(): MutableList<HistoryItem> {
        val sharedPreferences = context?.getSharedPreferences("History", Context.MODE_PRIVATE)
        val historyItems: MutableList<HistoryItem> = mutableListOf()

        val allEntries = sharedPreferences?.all ?: return historyItems

        for (entry in allEntries.entries) {
            val hiJSON = entry.value as? String ?: continue

            try {
                Log.d("DEBUG HI", hiJSON)
                val hi = Gson().fromJson(hiJSON, HistoryItem::class.java)
                historyItems.add(hi)
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
            }
        }

        return historyItems
    }
}
