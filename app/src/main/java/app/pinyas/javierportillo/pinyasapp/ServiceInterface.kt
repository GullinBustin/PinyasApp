package app.pinyas.javierportillo.pinyasapp

import org.json.JSONArray
import org.json.JSONObject

interface ServiceInterface {
    fun post(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit)
    fun get(path: String, completionHandler: (response: JSONArray?) -> Unit)
}