package net.mbs.ybma

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.recycler_view.*
import net.mbs.ybma.assync.TaskLoadedCallback
import net.mbs.ybma.commons.PrefManager
import net.mbs.ybma.commons.SessionUser
import net.mbs.ybma.fragment.ui.home.HomeFragment
import net.mbs.ybma.models.DrawerMenu
import net.mbs.ybma.utils.Tools
import java.util.*

class HomeActivity : AppCompatActivity(), TaskLoadedCallback {

    companion object {
        var context: Context? = null
        var activity: Activity? = null
        var prefManager: PrefManager? = null
        var list: ArrayList<DrawerMenu> = ArrayList<DrawerMenu>()
         var fragmentManager: FragmentManager? = null
        var fragmentTransaction: FragmentTransaction? = null
        var drawer: DrawerLayout? = null

        //Chose fragement
        fun selectItem(post: Int) {
            var fragment: Fragment? = null
            val pos = list[post].mId
            val item = list[post].mText
            var tag = "home"
            if (SessionUser.getUserCategorie(context!!).equals("client")) {
                if (pos == 1L) {
                    fragment = HomeFragment()
                    tag = "home"
                }
            }
            //fragment Manager
            if (fragment != null) {
                fragmentManager = (context as HomeActivity).supportFragmentManager
                fragmentTransaction = fragmentManager!!.beginTransaction()
                fragmentTransaction!!.replace(R.id.content_frame, fragment, tag)
                if (pos != 1L)
                    fragmentTransaction!!.addToBackStack(item)
                else {
                    fragmentTransaction!!.addToBackStack(null)
                }
                fragmentTransaction!!.commit()
            }
            drawer!!.closeDrawer(GravityCompat.START)
        }
    }


    override fun onStart() {
        super.onStart()
        selectItem(0)
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private var actionBar: ActionBar? = null
    private var toolbar: Toolbar? = null
    var drawerAdapter: DrawerAdapter? = null
    private var mDrawerList: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        context = this@HomeActivity
        activity = this

        prefManager = PrefManager(this)
        val objetbundle = this.intent.extras
        val fragment_name = objetbundle!!.getString("fragment_name")
        initToolbar()
        initNavigationMenu()

        mDrawerList = findViewById<View>(R.id.rvdrawer) as RecyclerView
        mDrawerList!!.layoutManager = LinearLayoutManager(context)
        mDrawerList!!.setHasFixedSize(true)
        setDrawer()


        if (savedInstanceState == null) {
            if (fragment_name.equals(""))
                selectItem(1)
        }
    }

    private fun initToolbar() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setHomeButtonEnabled(true)
        actionBar!!.title = "Admin"
        Tools.setSystemBarColor(this, R.color.colorPrimary)
    }

    private fun initNavigationMenu() {
        val nav_view = findViewById<View>(R.id.nav_view) as NavigationView
        drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        ) {
        }
        drawer!!.setDrawerListener(toggle)
        toggle.syncState()
    
    }

    private fun setDrawer() {
        drawer!!.isFocusable = false
       list.clear()
        if (SessionUser.getUserCategorie(context!!).equals("client")
        ) {
            user_name.text = SessionUser.getPrenom(context!!)
                .toString() + " " + SessionUser.getNom(context!!)
            user_phone.text = SessionUser.getPhone(context!!)
           list.add(
                DrawerMenu(
                    1,
                    "",
                    getString(R.string.item_home),
                    R.drawable.ic_home_outline
                )
            )
           list.add(
                DrawerMenu(
                    13,
                    "",
                    getString(R.string.item_favorite),
                    R.drawable.ic_star_outline
                )
            )
           list.add(
                DrawerMenu(
                    2,
                    "",
                    getString(R.string.item_new),
                    R.drawable.ic_new
                )
            )
           list.add(
                DrawerMenu(
                    3,
                    "",
                    getString(R.string.item_confirmed),
                    R.drawable.ic_confirm
                )
            )
           list.add(
                DrawerMenu(
                    4,
                    "",
                    getString(R.string.item_onride),
                    R.drawable.ic_completed
                )
            )
           list.add(
                DrawerMenu(
                    5,
                    "",
                    getString(R.string.item_completed),
                    R.drawable.ic_rent_outline
                )
            )
           list.add(
                DrawerMenu(
                    6,
                    "",
                    getString(R.string.item_canceled),
                    R.drawable.ic_error
                )
            )
           list.add(
                DrawerMenu(
                    10,
                    "",
                    getString(R.string.item_new_booking),
                    R.drawable.ic_calendar
                )
            )
           list.add(
                DrawerMenu(
                    11,
                    "",
                    getString(R.string.item_confirmed_booking),
                    R.drawable.ic_calendar_check
                )
            )
           list.add(
                DrawerMenu(
                    12,
                    "",
                    getString(R.string.item_canceled_booking),
                    R.drawable.ic_calendar_cancel
                )
            )
           list.add(
                DrawerMenu(
                    14,
                    "",
                    getString(R.string.item_louer_vehicule),
                    R.drawable.ic_rent_outline
                )
            )
           list.add(
                DrawerMenu(
                    15,
                    "",
                    getString(R.string.item_vehicule_loue),
                    R.drawable.ic_rent_outline
                )
            )
           list.add(
                DrawerMenu(
                    16,
                    "",
                    getString(R.string.item_wallet),
                    R.drawable.ic_wallet
                )
            )
           list.add(
                DrawerMenu(
                    7,
                    "",
                    getString(R.string.item_profile),
                    R.drawable.ic_profile_outline
                )
            )
           list.add(
                DrawerMenu(
                    0,
                    "",
                    getString(R.string.item_logout),
                    R.drawable.ic_logout_outline
                )
            )
           list.add(
                DrawerMenu(
                    8,
                    "",
                    "divider",
                    0
                )
            )
           list.add(
                DrawerMenu(
                    9,
                    "",
                    getString(R.string.item_help),
                    0
                )
            )
           list.add(
                DrawerMenu(
                    17,
                    "",
                    getString(R.string.item_contact_us),
                    R.drawable.ic_assistance_outline
                )
            )
        }
        drawerAdapter = DrawerAdapter(list,context!!)
        mDrawerList!!.adapter = drawerAdapter
    }

    class DrawerAdapter(list: List<DrawerMenu>, var context: Context) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val mDrawerItems: List<DrawerMenu> = list
        override fun getItemViewType(position: Int): Int {
            return 1
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerView.ViewHolder {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.drawer_row, parent, false)
            return ViewHolderPosts(view)
        }

        override fun onBindViewHolder(
            holder1: RecyclerView.ViewHolder,
            position: Int
        ) {
            val holder = holder1 as ViewHolderPosts
            val item: DrawerMenu= mDrawerItems[position]
            if (item.mText.equals("divider")) {
                holder.line_divider.visibility = View.VISIBLE
                holder.layout_item.visibility = View.GONE
            } else {
                holder.line_divider.visibility = View.GONE
                holder.layout_item.visibility = View.VISIBLE
                holder.title.text = item.mText
                holder.llrow.setOnClickListener {
                   selectItem(
                        position
                    )
                }
            }
            if (item.mIconRes === 0) {
                holder.img.visibility = View.GONE
            } else {
                holder.img.visibility = View.VISIBLE
                holder.img.setImageDrawable(context.resources.getDrawable(item.mIconRes))
            }
        }



        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItemCount(): Int {
            return mDrawerItems.size
        }

        inner class ViewHolderPosts(convertView: View) :
            RecyclerView.ViewHolder(convertView) {
            var title: TextView
            var llrow: LinearLayout
            var img: ImageView
            var line_divider: View
            var layout_item: LinearLayout

            init {
                llrow = convertView.findViewById<View>(R.id.llrow) as LinearLayout
                title = convertView.findViewById<View>(R.id.tvtitle) as TextView
                img =
                    convertView.findViewById<View>(R.id.img) as ImageView
                line_divider =
                    convertView.findViewById(R.id.line_divider) as View
                layout_item =
                    convertView.findViewById<View>(R.id.layout_item) as LinearLayout
            }
        }

    }



    override fun onTaskDone(vararg values: Any) {
        if (SessionUser.getUserCategorie(context!!).equals("client")
        ) {
            if (SessionUser.getCurrentFragment(context!!).equals("home")) {
                if (HomeFragment.currentPolyline != null) HomeFragment.currentPolyline!!.remove()
                HomeFragment.currentPolyline =
                    HomeFragment.mMap.addPolyline(values[0] as PolylineOptions?)
                HomeFragment.currentPolyline!!.color = Color.DKGRAY
                val builder = LatLngBounds.Builder()
                val latLng1 = LatLng(
                    HomeFragment.departLocationReservation.latitude,
                    HomeFragment.departLocationReservation.longitude
                )
                val latLng2 = LatLng(
                    HomeFragment.destinationLocationReservation.latitude,
                    HomeFragment.destinationLocationReservation.longitude
                )
                builder.include(latLng1)
                builder.include(latLng2)
                HomeFragment.mMap.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        builder.build(),
                        17
                    )
                )
            } /* else if (M.getCurrentFragment(context).equals("mes_requetes_accueil")){
                if (FragmentHome.currentPolyline != null)
                    FragmentHome.currentPolyline.remove();
                FragmentHome.currentPolyline = FragmentHome.mMap.addPolyline((PolylineOptions) values[0]);
                FragmentHome.currentPolyline.setVehicleColor(Color.DKGRAY);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                LatLng latLng1 = new LatLng(FragmentHome.departLocationMesRequetes.getLatitude(), FragmentHome.departLocationMesRequetes.getLongitude());
                LatLng latLng2 = new LatLng(FragmentHome.destinationLocationMesRequetes.getLatitude(), FragmentHome.destinationLocationMesRequetes.getLongitude());
                builder.include(latLng1);
                builder.include(latLng2);
                FragmentHome.mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 17));
            } else if (M.getCurrentFragment(context).equals("historic")){
                if (BottomSheetFragmentHistoric.currentPolyline != null)
                    BottomSheetFragmentHistoric.currentPolyline.remove();
                BottomSheetFragmentHistoric.currentPolyline = BottomSheetFragmentHistoric.mMap.addPolyline((PolylineOptions) values[0]);
                BottomSheetFragmentHistoric.currentPolyline.setVehicleColor(Color.DKGRAY);

                LatLngBounds.Builder builder2 = new LatLngBounds.Builder();
                LatLng latLng3 = new LatLng(BottomSheetFragmentHistoric.clientLocation.getLatitude(),BottomSheetFragmentHistoric.clientLocation.getLongitude());
                LatLng latLng4 = new LatLng(BottomSheetFragmentHistoric.destinationLocation.getLatitude(),BottomSheetFragmentHistoric.destinationLocation.getLongitude());
                builder2.include(latLng3);
                builder2.include(latLng4);
//            BottomSheetFragmentRide.mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder2.build(), 100));
            }else{
                if (BottomSheetFragmentMyRide.currentPolyline != null)
                    BottomSheetFragmentMyRide.currentPolyline.remove();
                BottomSheetFragmentMyRide.currentPolyline = BottomSheetFragmentMyRide.mMap.addPolyline((PolylineOptions) values[0]);
                BottomSheetFragmentMyRide.currentPolyline.setVehicleColor(Color.DKGRAY);

                LatLngBounds.Builder builder2 = new LatLngBounds.Builder();
                LatLng latLng3 = new LatLng(BottomSheetFragmentMyRide.clientLocation.getLatitude(),BottomSheetFragmentMyRide.clientLocation.getLongitude());
                LatLng latLng4 = new LatLng(BottomSheetFragmentMyRide.destinationLocation.getLatitude(),BottomSheetFragmentMyRide.destinationLocation.getLongitude());
                builder2.include(latLng3);
                builder2.include(latLng4);
                BottomSheetFragmentMyRide.mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder2.build(), 100));
            }*/
        } else {
            /*if(M.getCurrentFragment(context).equals("requete")){
                if (BottomSheetFragmentRide.currentPolyline != null)
                    BottomSheetFragmentRide.currentPolyline.remove();
                BottomSheetFragmentRide.currentPolyline = BottomSheetFragmentRide.mMap.addPolyline((PolylineOptions) values[0]);
                BottomSheetFragmentRide.currentPolyline.setVehicleColor(Color.DKGRAY);

                LatLngBounds.Builder builder2 = new LatLngBounds.Builder();
                LatLng latLng3 = new LatLng(BottomSheetFragmentRide.clientLocation.getLatitude(),BottomSheetFragmentRide.clientLocation.getLongitude());
                LatLng latLng4 = new LatLng(BottomSheetFragmentRide.destinationLocation.getLatitude(),BottomSheetFragmentRide.destinationLocation.getLongitude());
                builder2.include(latLng3);
                builder2.include(latLng4);
//                BottomSheetFragmentRide.mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder2.build(), 17));
            }else{
                if (BottomSheetFragmentCourse.currentPolyline != null)
                    BottomSheetFragmentCourse.currentPolyline.remove();
                BottomSheetFragmentCourse.currentPolyline = BottomSheetFragmentCourse.mMap.addPolyline((PolylineOptions) values[0]);
                BottomSheetFragmentCourse.currentPolyline.setVehicleColor(Color.DKGRAY);

                LatLngBounds.Builder builder2 = new LatLngBounds.Builder();
                LatLng latLng3 = new LatLng(BottomSheetFragmentCourse.clientLocation.getLatitude(),BottomSheetFragmentCourse.clientLocation.getLongitude());
                LatLng latLng4 = new LatLng(BottomSheetFragmentCourse.destinationLocation.getLatitude(),BottomSheetFragmentCourse.destinationLocation.getLongitude());
                builder2.include(latLng3);
                builder2.include(latLng4);
//                BottomSheetFragmentCourse.mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder2.build(), 17));
            }*/
        }
    }


}