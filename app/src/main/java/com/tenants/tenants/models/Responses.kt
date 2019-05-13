package com.tenants.tenants.models

data class LoginResponse(val user: User)

data class GroupsResponse(var groups: Array<Group>)
