package com.tenants.tenants.models

data class LoginResponse(val user: User)

data class RegisterResponse(val user: User)

data class GroupsResponse(var groups: Array<Group>)
