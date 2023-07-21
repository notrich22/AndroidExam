package com.example.androidbankingexam

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import android.widget.Spinner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import java.util.logging.Logger

class Settings : Fragment() {

    private lateinit var spinner : Spinner
    private lateinit var adapter : SimpleAdapter
    private var savedValue : Variants? = null
    enum class Variants(val variant:String){
        _7days("Last 7 days"),
        _1month("Last month"),
        _6month("Last 6 month"),
        _1year("Last year")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadSelectedValueFromSharedPreferences();
        super.onViewCreated(view, savedInstanceState)
        spinner = view.findViewById(R.id.historyPeriodSpinner)
        val values = Variants.values().toMutableList().map { mapOf("value" to it.variant) }
        adapter = SimpleAdapter(requireContext(), values, android.R.layout.simple_spinner_item, arrayOf("value"), intArrayOf(android.R.id.text1))
        spinner.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        saveSelectedValueToSharedPreferences(savedValue.toString());
    }
    private fun saveSelectedValueToSharedPreferences(value: String) {
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("historyRange", value).apply();
    }

    private fun loadSelectedValueFromSharedPreferences(): String? {
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("historyRange", null)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            Settings().apply {
                arguments = Bundle().apply {

                }
            }
    }
}