package com.tenants.tenants.models

data class Bill(var _id: String, var name: String, var value: Int, var user: User, var debtors: Array<BillDebtor>, var createdAt: String)

data class BillDebtor(var _id: String, var value: Float, var user: String, var paid: Boolean)
