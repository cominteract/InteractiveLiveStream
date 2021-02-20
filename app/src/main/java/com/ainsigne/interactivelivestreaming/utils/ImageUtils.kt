package com.ainsigne.interactivelivestreaming.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.view.View
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class ImageUtils {

    companion object{
        fun viewToBitmap(view: View): Bitmap? {
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            return bitmap
        }
        fun postData(imageToSend: Bitmap) {
            try {
                val url = URL("${BASE_API}upload_image")
                val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
                conn.setRequestMethod("POST")
                conn.setDoInput(true)
                conn.setDoOutput(true)
                conn.setRequestProperty("Connection", "Keep-Alive")
                conn.setRequestProperty("Cache-Control", "no-cache")
                conn.setReadTimeout(35000)
                conn.setConnectTimeout(35000)

                // directly let .compress write binary image data
                // to the output-stream
                val os: OutputStream = conn.getOutputStream()
                imageToSend.compress(Bitmap.CompressFormat.JPEG, 100, os)
                os.flush()
                os.close()
                System.out.println("Response Code: " + conn.getResponseCode())
                val `in`: InputStream = BufferedInputStream(conn.getInputStream())

                val responseStreamReader = BufferedReader(InputStreamReader(`in`))
                var line: String? = ""
                val stringBuilder = StringBuilder()
                while (responseStreamReader.readLine()
                        .also { line = it } != null
                ) stringBuilder.append(line).append("\n")
                responseStreamReader.close()
                val response = stringBuilder.toString()
                conn.disconnect()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}