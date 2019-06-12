package com.tenants.tenants.models

data class Debt(var _id: String, var name: String, var value: Int, var paid: Boolean, var holder: Holder, var debtor: Debtor, var createdAt: String)