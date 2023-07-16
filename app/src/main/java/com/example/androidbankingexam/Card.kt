package com.example.androidbankingexam

class Card(
    private var id: String,
    private var name: String,
    private var last4numbers: String,
    private var balance: Double
    ) {

    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }
    fun getId() : String{
        return id;
    }
    fun getLast4Numbers(): String {
        return last4numbers
    }

    fun setLast4Numbers(last4numbers: String) {
        this.last4numbers = last4numbers
    }
    fun getBalance() : Double{
        return balance
    }
    fun setBalance(newBalance : Double){
        this.balance = newBalance
    }
}
