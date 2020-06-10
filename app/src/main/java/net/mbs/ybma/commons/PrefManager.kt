package net.mbs.ybma.commons

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class PrefManager @SuppressLint("CommitPrefEdits") constructor(var context: Context?) {
    var pref: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null

    var pref1: SharedPreferences? = null
    var editor1: SharedPreferences.Editor? = null

    var pref2: SharedPreferences? = null
    var editor2: SharedPreferences.Editor? = null

    var pref3: SharedPreferences? = null
    var editor3: SharedPreferences.Editor? = null

    var pref4: SharedPreferences? = null
    var editor4: SharedPreferences.Editor? = null

    var pref5: SharedPreferences? = null
    var editor5: SharedPreferences.Editor? = null

    var pref6: SharedPreferences? = null
    var editor6: SharedPreferences.Editor? = null

    var pref7: SharedPreferences? = null
    var editor7: SharedPreferences.Editor? = null

    var pref8: SharedPreferences? = null
    var editor8: SharedPreferences.Editor? = null

    //mode shareed
    var PRIVATE_MODE: Int = 0

    var PREF_NAME: String? = "youbouma"

    var IS_FIRST_TIME_LAUNCH: String = "IsFirstTimeLaunch"
    var IS_FIRST_TIME_LAUNCH1: String = "IsFirstTimeLaunch1"
    var IS_FIRST_TIME_LAUNCH2: String = "IsFirstTimeLaunch2"
    var IS_FIRST_TIME_LAUNCH3: String = "IsFirstTimeLaunch3"
    var IS_FIRST_TIME_LAUNCH4: String = "IsFirstTimeLaunch4"
    var IS_FIRST_TIME_LAUNCH5: String = "IsFirstTimeLaunch5"
    var IS_FIRST_TIME_LAUNCH6: String = "IsFirstTimeLaunch6"
    var IS_FIRST_TIME_LAUNCH7: String = "IsFirstTimeLaunch7"
    var IS_FIRST_TIME_LAUNCH8: String = "IsFirstTimeLaunch8"

    init {
        pref = context!!.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref!!.edit()
        pref1 = context!!.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor1 = pref1!!.edit()
        pref2 = context!!.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor2 = pref2!!.edit()
        pref3 = context!!.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor3 = pref3!!.edit()
        pref4 = context!!.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor4 = pref4!!.edit()
        pref5 = context!!.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor5 = pref5!!.edit()
        pref6 = context!!.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor6 = pref6!!.edit()
        pref7 = context!!.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor7 = pref7!!.edit()
        pref8 = context!!.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor8 = pref8!!.edit()
    }

    fun setFirstTimeLaunch(isFirstTime:Boolean) {
        editor!!.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime)
        editor!!.commit()
    }

    fun  isFirstTimeLaunch():Boolean {
        return pref!!.getBoolean(IS_FIRST_TIME_LAUNCH, true)
    }

  fun setFirstTimeLaunch1(isFirstTime:Boolean) {
        editor1!!.putBoolean(IS_FIRST_TIME_LAUNCH1, isFirstTime)
        editor1!!.commit()
    }

   fun isFirstTimeLaunch1() :Boolean{
        return pref1!!.getBoolean(IS_FIRST_TIME_LAUNCH1, true)
    }

   fun  setFirstTimeLaunch2(isFirstTime:Boolean) {
        editor2!!.putBoolean(IS_FIRST_TIME_LAUNCH2, isFirstTime)
        editor2!!.commit()
    }

   fun isFirstTimeLaunch2():Boolean {
        return pref2!!.getBoolean(IS_FIRST_TIME_LAUNCH2, true)
    }

    fun setFirstTimeLaunch3(isFirstTime:Boolean) {
        editor3!!.putBoolean(IS_FIRST_TIME_LAUNCH3, isFirstTime)
        editor3!!.commit()
    }

    fun isFirstTimeLaunch3():Boolean {
        return pref3!!.getBoolean(IS_FIRST_TIME_LAUNCH3, true)
    }

   fun setFirstTimeLaunch4(isFirstTime:Boolean) {
        editor4!!.putBoolean(IS_FIRST_TIME_LAUNCH4, isFirstTime)
        editor4!!.commit()
    }

   fun isFirstTimeLaunch4():Boolean {
        return pref4!!.getBoolean(IS_FIRST_TIME_LAUNCH4, true)
    }

    fun setFirstTimeLaunch5(isFirstTime:Boolean) {
        editor5!!.putBoolean(IS_FIRST_TIME_LAUNCH5, isFirstTime)
        editor5!!.commit()
    }

   fun isFirstTimeLaunch5():Boolean {
        return pref5!!.getBoolean(IS_FIRST_TIME_LAUNCH5, true)
    }

    fun setFirstTimeLaunch6(isFirstTime:Boolean) {
        editor6!!.putBoolean(IS_FIRST_TIME_LAUNCH6, isFirstTime)
        editor6!!.commit()
    }

   fun isFirstTimeLaunch6():Boolean {
        return pref6!!.getBoolean(IS_FIRST_TIME_LAUNCH6, false)
    }

  fun setFirstTimeLaunch7(isFirstTime:Boolean) {
        editor7!!.putBoolean(IS_FIRST_TIME_LAUNCH7, isFirstTime)
        editor7!!.commit()
    }

   fun isFirstTimeLaunch7():Boolean {
        return pref7!!.getBoolean(IS_FIRST_TIME_LAUNCH7, true)
    }

    fun setFirstTimeLaunch8(isFirstTime:Boolean) {
        editor8!!.putBoolean(IS_FIRST_TIME_LAUNCH8, isFirstTime)
        editor8!!.commit()
    }

    fun isFirstTimeLaunch8():Boolean {
        return pref8!!.getBoolean(IS_FIRST_TIME_LAUNCH8, true)
    }

}