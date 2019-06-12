package com.tenants.tenants.api

import com.tenants.tenants.models.*

data class LoginResponse(val user: User)

data class RegisterResponse(val user: User)

data class ActivationLinkResponse(val link: String)

data class GroupsResponse(var groups: Array<Group>)

data class MembersResponse(var members: Array<Member>)

data class DebtsResponse(var debts: Array<Debt>)

data class BillsResponse(var bills: Array<Bill>)

data class ShoppingListsResponse(var shoppingLists: Array<ShoppingList>)

data class DutiesResponse(var duties: Array<Duty>)
