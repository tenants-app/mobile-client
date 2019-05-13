package com.tenants.tenants.models

data class Group(var _id: String, var name: String): Model {
    override fun getDisplayName(): String {
        return name
    }

    override fun getId(): String {
        return _id
    }
}