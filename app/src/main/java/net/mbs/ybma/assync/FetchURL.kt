package net.mbs.ybma.assync

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import net.mbs.ybma.fragment.ui.home.HomeFragment
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class FetchURL(var context: Context?, var categorie: String): AsyncTask<String, Any, String>() {
    var directionMode=""
    override fun doInBackground(vararg params: String?): String {
        // Pour stocker des données à partir d'un service Web
        var data=""
        directionMode= params[1]!!
        try {
            // Récupération des données du service Web
            data = downloadUrl(params[0])
        }catch (e:Exception){
            Log.d("Background Task",e.toString())
        }
        return data
    }

    override fun onPostExecute(s: String?) {
        super.onPostExecute(s)
        var parserTask = PointsParser(context!!, directionMode, categorie)
        // Invokes the thread for parsing the JSON data
        parserTask.execute(s)
        try {
            if (categorie == "home") {
                if (directionMode == "bicycling") {
                   HomeFragment.parseRouteDistanceBicycling(JSONObject(s))
                } else {
                    HomeFragment.parseRouteDistance(JSONObject(s))
                }
            } else if (categorie == "timeout") {
               // MainActivity.parseRouteDistanceTimeOut(JSONObject(s))
            } else {
//                FragmentHome.dismissBottomSheet();
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun downloadUrl(url: String?): String {
        var data =""
        var iStream: InputStream?=null
        var urlConnection:HttpURLConnection?=null

        try {
            var url =URL(url)
            // Création d'une connexion http pour communiquer avec l'url
            urlConnection =url.openConnection() as HttpURLConnection
            //Connect url
            urlConnection.connect()
            //Lecturedes données from url
            iStream = urlConnection.inputStream
            val br = BufferedReader(InputStreamReader(iStream))
            val sb = StringBuffer()
            var line: String? = ""
            while (br.readLine().also { line = it } != null) {
                sb.append(line)
            }
            data = sb.toString()
            Log.d("mylog", "Downloaded URL: $data")
            br.close()
        }catch (e:java.lang.Exception){
            Log.d("mylog", "Exception downloading URL: $e")
        }
        finally {
        iStream!!.close()
        urlConnection!!.disconnect()
        }
        return data
    }

}
