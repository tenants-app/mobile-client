package com.tenants.tenants.models

import java.io.Serializable

data class ShoppingList(var _id: String?,
                        var name: String,
                        var value: Int?,
                        var user: User?,
                        var products: Array<Product>,
                        var debtors: Array<BillDebtor>?,
                        var createdAt: String?): Serializable
