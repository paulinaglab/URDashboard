package com.gogreenyellow.pglab.urdashboard.api

import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONArray
import javax.xml.transform.ErrorListener

/**
 * Created by Paulina on 2018-02-13.
 */
class AuthorizedJsonArrayRequest(val authToken: String,
                                 url: String,
                                 listener: Response.Listener<JSONArray>,
                                 errorListener: Response.ErrorListener) : JsonArrayRequest(url, listener, errorListener) {

    override fun getHeaders(): MutableMap<String, String> {
        val headers = HashMap<String, String>();
        headers.put("Authorization", "Bearer " + authToken)
        return headers
    }
}