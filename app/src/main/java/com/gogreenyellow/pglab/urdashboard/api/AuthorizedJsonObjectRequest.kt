package com.gogreenyellow.pglab.urdashboard.api

import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class AuthorizedJsonObjectRequest(val authToken: String,
                                  method: Int,
                                  url: String,
                                  body: JSONObject,
                                  responseListener: Response.Listener<JSONObject>,
                                  errorListener: Response.ErrorListener) : JsonObjectRequest(method, url, body, responseListener, errorListener) {

    val authorizer = Authorizer()

    override fun getHeaders(): MutableMap<String, String> {
        return authorizer.generateAuthHeader(authToken)
    }
}