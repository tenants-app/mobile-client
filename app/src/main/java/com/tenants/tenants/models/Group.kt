package com.tenants.tenants.models

data class Group(var _id: String, var name: String, var members: Array<Member>)

data class Member(var _id: String, var username: String, var email: String)