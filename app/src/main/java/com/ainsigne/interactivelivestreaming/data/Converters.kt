package com.ainsigne.interactivelivestreaming.data

import androidx.room.TypeConverter
import com.ainsigne.interactivelivestreaming.model.StreamUser
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.util.*


/**
 * Type converters to allow Room to reference complex data types.
 */
class Converters {
    @TypeConverter fun calendarToDatestamp(calendar: Calendar): Long = calendar.timeInMillis

    @TypeConverter fun datestampToCalendar(value: Long): Calendar =
            Calendar.getInstance().apply { timeInMillis = value }




    @TypeConverter fun arrayListHashmapToString(value: ArrayList<HashMap<String, Any>>) = Gson().toJson(value)

    @TypeConverter fun stringToArrayListHashmap(value: String) : ArrayList<HashMap<String, Any>>{
        val venueType = object : TypeToken<ArrayList<HashMap<String, Any>>>() {}.type
        return Gson().fromJson(value, venueType)
    }

    @TypeConverter fun hashmapToString(value: HashMap<String, Any>) = Gson().toJson(value)

    @TypeConverter fun stringTohashmap(value: String) : HashMap<String, Any>{
        val venueType = object : TypeToken<HashMap<String, Any>>() {}.type
        return Gson().fromJson(value, venueType)
    }

//
//    @TypeConverter fun anyToString(stream : Any) =  Gson().toJson(stream)
//
//    @TypeConverter fun stringToAny(value: String) : Any{
//        val userType = object : TypeToken<Any>() {}.type
//        return Gson().fromJson(value, userType)
//    }
//
    @TypeConverter fun userToString(user: StreamUser) =  Gson().toJson(user)

    @TypeConverter fun stringToUser(value: String) : StreamUser{


        val json = Gson().fromJson(value, JsonObject::class.java)

        return StreamUser(json["userName"].toString(), json.get("dateCreated").toString())
//        json.get("dateCreated")
//        val userType = object : TypeToken<Any>() {}.type
//        return Gson().fromJson(value, userType)
    }

//    @TypeConverter fun mapToUser(value: String) : StreamUser {
//        val myHashMap = HashMap<String, String>()
//        try {
//            val jArray = JSONArray(value)
//            var jObject: JSONObject? = null
//            var keyString: String? = null
//            for (i in 0 until jArray.length()) {
//                jObject = jArray.getJSONObject(i)
//                // beacuse you have only one key-value pair in each object so I have used index 0
//                keyString = jObject.names()[0] as String
//                myHashMap[keyString] = jObject.getString(keyString)
//            }
//        } catch (e: JSONException) {
//            // TODO Auto-generated catch block
//            e.printStackTrace()
//        }
//        return StreamUser(myHashMap.get("userName")!!, myHashMap.get("dateCreated")!!)
//    }

//    @TypeConverter fun userToTreeMap(user : StreamUser) =  Gson().toJson(user)
//
//    @TypeConverter fun treeMapToUser(value: TreeMap<String,String>) : StreamUser{
//        val userType = object : TypeToken<Any>() {}.type
//        return Gson().fromJson(value), userType)
//    }
//


}