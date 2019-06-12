package com.tenants.tenants.models

data class LoginResponse(val user: User)

data class RegisterResponse(val user: User)

data class GroupsResponse(var groups: Array<Group>)

data class DebtsResponse(var debts: Array<Debt>)

data class BillsResponse(var bills: Array<Bill>)

data class ShoppingListsResponse(var shoppingLists: Array<ShoppingList>)

data class DutiesResponse(var duties: Array<Duty>)
