package net.mbs.ybma.assync

import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import net.mbs.ybma.R
import org.json.JSONObject
import java.util.*

class PointsParser(
    mContext: Context,
    directionMode: String,
    categorie: String
) :
    AsyncTask<String, Int, List<List<HashMap<String, String>>>>() {
    private var taskCallback: TaskLoadedCallback?=null
    private var directionMode = "driving"
    private var mContext:Context?=null
    private var categorie :String?=null

   init {
        taskCallback = mContext as TaskLoadedCallback
        this.directionMode = directionMode
        this.mContext = mContext
        this.categorie = categorie
    }

    // Parsing the data in non-ui thread
    override fun doInBackground(vararg jsonData: String): List<List<HashMap<String, String>>>? {

        val jObject: JSONObject
        var routes: List<List<HashMap<String, String>>>? = null

        try {
            jObject = JSONObject(jsonData[0])
            Log.d("mylog", jsonData[0])
            val parser = DataParser(mContext)
            Log.d("mylog", parser.toString())

            // Starts parsing data
            routes = parser.parse(jObject)
            Log.d("mylog", "Executing routes")
            Log.d("mylog", routes!!.toString())

        } catch (e: Exception) {
            Log.d("mylog", e.toString())
            e.printStackTrace()
        }

        return routes
    }

    // Executes in UI thread, after the parsing process
    /**
     * onPostExecute
     *@param List<List<HasMap<String,String>>>
     * @param result
     */

    override fun onPostExecute(result: List<List<HashMap<String, String>>>) {
        var points: ArrayList<LatLng>
        var lineOptions: PolylineOptions? = null
        // Traversing through all the routes
        for (i in result.indices) {
            points = ArrayList()
            lineOptions = PolylineOptions()
            // Fetching i-th route
            val path = result[i]
            // Fetching all the points in i-th route
            for (j in path.indices) {
                val point = path[j]
                val lat = java.lang.Double.parseDouble(point["lat"]!!)
                val lng = java.lang.Double.parseDouble(point["lng"]!!)
                val position = LatLng(lat, lng)
                points.add(position)
            }

            if (categorie == "bottom_book") {
                //BottomSheetFragmentBooking.points = points
               // BottomSheetFragmentBooking.parseRouteDistance()
            }
            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points)
            if (directionMode.equals("walking", ignoreCase = true)) {
                lineOptions.width(10f)
                lineOptions.color(Color.MAGENTA)
            } else {
                lineOptions.width(15f)
                lineOptions.color(R.color.colorPrimaryDark)
            }
            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                //mMap.addPolyline(lineOptions);
                taskCallback!!.onTaskDone(lineOptions)

            } else {
                Log.d("mylog", "without Polylines drawn")
            }
            Log.d("mylog", "onPostExecute lineoptions decoded")
        }


    }
}
