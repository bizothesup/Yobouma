package net.mbs.ybma.commons

import android.content.Context
import android.content.SharedPreferences

object SessionUser {
    var sharedPreferences: SharedPreferences?=null
    val pref_name:String="setting_taxi"


    //Assign User In variable sharepprefrence

    fun setUsername(
        username: String?,
        mContext: Context
    ): Boolean {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        val editor: SharedPreferences.Editor =
            sharedPreferences!!.edit()
        editor.putString("username", username)
        return editor.commit()
    }

    fun getUsername(mContext: Context): String? {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        return sharedPreferences!!.getString("username", null)
    }

    fun setNom(nom: String?, mContext: Context): Boolean {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        val editor: SharedPreferences.Editor =
            sharedPreferences!!.edit()
        editor.putString("nom", nom)
        return editor.commit()
    }

    fun getNom(mContext: Context): String? {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        return sharedPreferences!!.getString("nom", null)
    }

    fun setPrenom(prenom: String?, mContext: Context): Boolean {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        val editor: SharedPreferences.Editor =
            sharedPreferences!!.edit()
        editor.putString("prenom", prenom)
        return editor.commit()
    }

    fun getPrenom(mContext: Context): String? {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        return sharedPreferences!!.getString("prenom", null)
    }

    fun setRouteDistance(
        prenom: String?,
        mContext: Context
    ): Boolean {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        val editor: SharedPreferences.Editor =
            sharedPreferences!!.edit()
        editor.putString("routedistance", prenom)
        return editor.commit()
    }

    fun getRouteDistance(mContext: Context): String? {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        return sharedPreferences!!.getString(
            "routedistance",
            null
        )
    }

    fun setRouteDuration(
        prenom: String?,
        mContext: Context
    ): Boolean {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        val editor: SharedPreferences.Editor =
            sharedPreferences!!.edit()
        editor.putString("routeduration", prenom)
        return editor.commit()
    }

    fun getRouteDuration(mContext: Context): String? {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        return sharedPreferences!!.getString(
            "routeduration",
            null
        )
    }

    fun getCurrentFragment(mContext: Context): String? {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        return sharedPreferences!!.getString(
            "current_fragment",
            null
        )
    }

    fun setCurrentFragment(
        current_fragment: String?,
        mContext: Context
    ): Boolean {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        val editor: SharedPreferences.Editor =
            sharedPreferences!!.edit()
        editor.putString("current_fragment", current_fragment)
        return editor.commit()
    }

    fun setStatutConducteur(
        online: String?,
        mContext: Context
    ): Boolean {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        val editor: SharedPreferences.Editor =
            sharedPreferences!!.edit()
        editor.putString("statut_conducteur", online)
        return editor.commit()
    }

    fun getStatutConducteur(mContext: Context): String? {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        return sharedPreferences!!.getString(
            "statut_conducteur",
            null
        )
    }

    fun setCountry(
        country: String?,
        mContext: Context
    ): Boolean {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        val editor: SharedPreferences.Editor =
            sharedPreferences!!.edit()
        editor.putString("country", country)
        return editor.commit()
    }

    fun getCountry(mContext: Context): String? {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        return sharedPreferences!!.getString("country", null)
    }


//    public static boolean setEmail(String email, Context mContext) {
//       sharedPreferences = mContext.getSharedPreferences(pref_name, 0);
//        SharedPreferences.Editor editor =sharedPreferences!!.edit();
//        editor.putString("email", email);
//        return editor.commit();
//    }

    //    public static boolean setEmail(String email, Context mContext) {
    //       sharedPreferences = mContext.getSharedPreferences(pref_name, 0);
    //        SharedPreferences.Editor editor =sharedPreferences!!.edit();
    //        editor.putString("email", email);
    //        return editor.commit();
    //    }
    fun getEmail(mContext: Context): String? {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        return sharedPreferences!!.getString("email", null)
    }

    fun setEmail(email: String?, mContext: Context): Boolean {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        val editor: SharedPreferences.Editor =
            sharedPreferences!!.edit()
        editor.putString("email", email)
        return editor.commit()
    }

    fun getUserCategorie(mContext: Context): String? {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        return sharedPreferences!!.getString("user_cat", null)
    }

    fun setUserCategorie(
        user_cat: String?,
        mContext: Context
    ): Boolean {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        val editor: SharedPreferences.Editor =
            sharedPreferences!!.edit()
        editor.putString("user_cat", user_cat)
        return editor.commit()
    }

    fun getCoutByKm(mContext: Context): String? {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        return sharedPreferences!!.getString("cout_km", null)
    }

    fun setCoutByKm(
        cout_km: String?,
        mContext: Context
    ): Boolean {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        val editor: SharedPreferences.Editor =
            sharedPreferences!!.edit()
        editor.putString("cout_km", cout_km)
        return editor.commit()
    }

    fun setPhone(phone: String?, mContext: Context): Boolean {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        val editor: SharedPreferences.Editor =
            sharedPreferences!!.edit()
        editor.putString("phone", phone)
        return editor.commit()
    }

    fun getPhone(mContext: Context): String? {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        return sharedPreferences!!.getString("phone", null)
    }

    fun setCurrency(money: String?, mContext: Context): Boolean {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        val editor: SharedPreferences.Editor =
            sharedPreferences!!.edit()
        editor.putString("money", money)
        return editor.commit()
    }

    fun getCurrency(mContext: Context): String? {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        return sharedPreferences!!.getString("money", null)
    }

    fun setVehicleBrand(
        brand: String?,
        mContext: Context
    ): Boolean {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        val editor: SharedPreferences.Editor =
            sharedPreferences!!.edit()
        editor.putString("brand", brand)
        return editor.commit()
    }

    fun getBrand(mContext: Context): String? {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        return sharedPreferences!!.getString("brand", null)
    }

    fun setType(type: String?, mContext: Context): Boolean {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        val editor: SharedPreferences.Editor =
            sharedPreferences!!.edit()
        editor.putString("type", type)
        return editor.commit()
    }

    fun getType(mContext: Context): String? {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        return sharedPreferences!!.getString("type", null)
    }

    fun setVehicleColor(
        color: String?,
        mContext: Context
    ): Boolean {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        val editor: SharedPreferences.Editor =
            sharedPreferences!!.edit()
        editor.putString("color", color)
        return editor.commit()
    }

    fun getColor(mContext: Context): String? {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        return sharedPreferences!!.getString("color", null)
    }

    fun setVehicleModel(
        licence: String?,
        mContext: Context
    ): Boolean {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        val editor: SharedPreferences.Editor =
            sharedPreferences!!.edit()
        editor.putString("model", licence)
        return editor.commit()
    }

    fun getVehicleModel(mContext: Context): String? {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        return sharedPreferences!!.getString("model", null)
    }

    fun setVehicleNumberPlate(registration: String?, mContext: Context): Boolean {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        val editor: SharedPreferences.Editor =
            sharedPreferences!!.edit()
        editor.putString("numberplate", registration)
        return editor.commit()
    }

    fun getVehicleNumberPlate(mContext: Context): String? {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        return sharedPreferences!!.getString(
            "numberplate",
            null
        )
    }

    fun getInsurance(mContext: Context): String? {
        sharedPreferences = mContext.getSharedPreferences(pref_name, 0)
        return sharedPreferences!!.getString("insurance", null)
    }

    fun getTaxiType(mContext: Context): String? {
        sharedPreferences = mContext.getSharedPreferences(pref_name, 0)
        return sharedPreferences!!.getString("taxi_type", null)
    }

    fun setTaxiType(taxi_type: String?, mContext: Context): Boolean {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        val editor: SharedPreferences.Editor =
            sharedPreferences!!.edit()
        editor.putString("taxi_type", taxi_type)
        return editor.commit()
    }

    fun getTaxiTypeImage(mContext: Context): String? {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        return sharedPreferences!!.getString(
            "taxi_type_image",
            null
        )
    }

    fun setTaxiTypeImage(
        taxi_type_image: String?,
        mContext: Context
    ): Boolean {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        val editor: SharedPreferences.Editor =
            sharedPreferences!!.edit()
        editor.putString("taxi_type_image", taxi_type_image)
        return editor.commit()
    }

    fun setPhoto(photo: String?, mContext: Context): Boolean {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        val editor: SharedPreferences.Editor =
            sharedPreferences!!.edit()
        editor.putString("photo", photo)
        return editor.commit()
    }

    fun getLicence(mContext: Context): String? {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        return sharedPreferences!!.getString("licence", "")
    }

    fun setLicence(
        licence: String?,
        mContext: Context
    ): Boolean {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        val editor: SharedPreferences.Editor =
            sharedPreferences!!.edit()
        editor.putString("licence", licence)
        return editor.commit()
    }

    fun getNic(mContext: Context): String? {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        return sharedPreferences!!.getString("nic", "")
    }

    fun setNic(nic: String?, mContext: Context): Boolean {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        val editor: SharedPreferences.Editor =
            sharedPreferences!!.edit()
        editor.putString("nic", nic)
        return editor.commit()
    }

    fun getPhoto(mContext: Context): String? {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        return sharedPreferences!!.getString("photo", "")
    }

    fun setlogintype(
        logintype: String?,
        mContext: Context
    ): Boolean {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        val editor: SharedPreferences.Editor =
            sharedPreferences!!.edit()
        editor.putString("logintype", logintype)
        return editor.commit()
    }

    fun getlogintype(mContext: Context): String? {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        return sharedPreferences!!.getString("logintype", null)
    }

    fun setPushNotification(
        pn: Boolean?,
        mContext: Context
    ): Boolean {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        val editor: SharedPreferences.Editor =
            sharedPreferences!!.edit()
        editor.putBoolean("pushnotify", pn!!)
        return editor.commit()
    }

    fun isPushNotify(mContext: Context): Boolean? {
        sharedPreferences =
            mContext.getSharedPreferences(pref_name, 0)
        return sharedPreferences!!.getBoolean(
            "pushnotify",
            true
        )
    }

    fun logOut(mContext: Context) {
        setID(null, mContext)
        setNom(null, mContext)
        setPrenom(null, mContext)
        setPhone(null, mContext)
        setEmail(null, mContext)
        //        setUserCategorie(null,mContext);
        setCoutByKm(null, mContext)
        setPhoto(null, mContext)
        //        setPhoto(null,mContext);
        setlogintype(null, mContext)
        setUsername(null, mContext)
        setPushNotification(true, mContext)
        setUserCategorie(null, mContext)
        setCurrentFragment(null, mContext)
        setCurrency(null, mContext)
        setStatutConducteur(null, mContext)
        setVehicleBrand(null, mContext)
        setVehicleColor(null, mContext)
        setVehicleModel(null, mContext)
        setVehicleNumberPlate(null, mContext)
        sharedPreferences!!.getAll().clear()
    }

    fun setID(ID: String?, mContext: Context): Boolean {
        sharedPreferences = mContext.getSharedPreferences(pref_name, 0)
        val editor: SharedPreferences.Editor =
            sharedPreferences!!.edit()
        editor.putString("userid", ID)
        return editor.commit()
    }

    fun getID(mContext: Context): String? {
        sharedPreferences = mContext.getSharedPreferences(pref_name, 0)
        return sharedPreferences!!.getString("userid", "")
    }

    fun setUserName(s: String, mContext: Context):Boolean {
        sharedPreferences = mContext.getSharedPreferences(pref_name, 0)
        val editor: SharedPreferences.Editor =
            sharedPreferences!!.edit()
        editor.putString("username", s)
        return editor.commit()
    }
    fun  getUserName(mContext: Context):String?{
        sharedPreferences = mContext.getSharedPreferences(pref_name, 0)
        return sharedPreferences!!.getString("username", "")
    }
}