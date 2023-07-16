package com.example.androidbankingexam

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import android.widget.Spinner
import androidx.lifecycle.findViewTreeViewModelStoreOwner

class Settings : Fragment() {

    private lateinit var spinner : Spinner
    private lateinit var adapter : SimpleAdapter

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
        super.onViewCreated(view, savedInstanceState)
        spinner = view.findViewById(R.id.historyPeriodSpinner)
        val values = Variants.values().toMutableList().map { mapOf("key" to it.toString()) }
        adapter = SimpleAdapter(requireContext(),values, android.R.layout.simple_spinner_item, arrayOf("key"), intArrayOf(android.R.id.text1))
        spinner.adapter = adapter

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