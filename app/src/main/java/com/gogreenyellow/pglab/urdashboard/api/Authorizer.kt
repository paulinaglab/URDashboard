package com.gogreenyellow.pglab.urdashboard.api

class Authorizer {
    fun generateAuthHeader(authToken: String): MutableMap<String, String> {
        val headers = HashMap<String, String>();
        headers.put("Authorization", "Bearer " + authToken)
        return headers
    }
}