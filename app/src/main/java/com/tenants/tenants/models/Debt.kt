package com.tenants.tenants.models

data class Debt(var _id: String, var name: String, var value: Int, var paid: Boolean, var holder: Holder, var debtor: Debtor, var createdAt: String)

data class Holder(var _id: String, var username: String, var email: String, var bank_account_number: String)

data class Debtor(var _id: String, var username: String, var email: String, var bank_account_number: String)