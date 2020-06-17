package net.mbs.ybma.assync

import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import org.json.JSONObject
import java.util.*

class PointsParser(var context: Context?, private var directionMode: String, private var categorie: String)
    : AsyncTask<String?, Int?, List<List<HashMap<String, String>>>?>(){

    var taskCallback: TaskLoadedCallback? = null

    var mContext: Context? = null

    fun PointsParser(
        mContext: Context?,
        directionMode: String,
        categorie: String
    ) {
        taskCallback = mContext as TaskLoadedCallback?
        this.directionMode = directionMode
        this.mContext = mContext
        this.categorie = categorie
    }

    override fun doInBackground(vararg params: String?): List<List<HashMap<String, String>>>? {
        var jsonObject:JSONObject
        var routes: List<List<HashMap<String, String>>>? = null

        try{
            jsonObject = JSONObject(params[0])
            Log.d("mylog", params.get(0).toString())
            val parser = DataParser(context!!)
            Log.d("mylog", parser.toString())

            // Starts parsing data
            routes = parser.parse(jsonObject)
            Log.d("mylog", "Executing routes")
            Log.d("mylog", routes.toString())
        }catch (e:Exception){
            Log.d("mylog", e.toString())
            e.printStackTrace()
        }
        return routes
    }

    // S'exécute dans le thread d'interface utilisateur, après le processus d'analyse
    override fun onPostExecute(result: List<List<HashMap<String, String>>>?) {
        var points: ArrayList<LatLng?>
        var lineOptions: PolylineOptions? = null
        // Traversing through all the routes
        // Traversing through all the routes
        for (i in result!!.indices) {
            points = ArrayList()
            lineOptions = PolylineOptions()
            // Fetching i-th route
            val path =
                result!![i]
            // Fetching all the points in i-th route
            for (j in path.indices) {
                val point = path[j]
                val lat = point["lat"]!!.toDouble()
                val lng = point["lng"]!!.toDouble()
                val position = LatLng(lat, lng)
                points.add(position)
                //                Log.i("location_polyline", lat+", "+lng);
            }
            if (categorie == "bottom_book") {
               // BottomSheetFragmentBooking.points = points
               // BottomSheetFragmentBooking.parseRouteDistance()
            }
            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points)
            if (directionMode.equals("walking", ignoreCase = true)) {
                lineOptions.width(10f)
                lineOptions.color(Color.MAGENTA)
            } else {
                lineOptions.width(20f)
                lineOptions.color(Color.BLUE)
            }
            Log.d("mylog", "onPostExecute lineoptions decoded")
        }

        // Drawing polyline in the Google Map for the i-th route

        // Drawing polyline in the Google Map for the i-th route
        if (lineOptions != null) {
            //mMap.addPolyline(lineOptions);
            taskCallback!!.onTaskDone(lineOptions)
        } else {
            Log.d("mylog", "without Polylines drawn")
        }
    }
}
