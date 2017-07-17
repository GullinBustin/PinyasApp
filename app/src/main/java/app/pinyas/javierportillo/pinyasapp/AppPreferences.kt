package app.pinyas.javierportillo.pinyasapp

import android.content.Context
import android.content.SharedPreferences

class AppPreferences private constructor(context: Context) {
    private val mPref: SharedPreferences
    private var mEditor: SharedPreferences.Editor? = null
    private var mBulkUpdate = false

    /**
     * Enum representing your setting names or key for your setting.
     */
    enum class Key {
        DRIVER_ACTUAL_STATUS,
    }

    init {
        mPref = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE)
    }

    fun put(key: Key, `val`: String) {
        doEdit()
        mEditor!!.putString(key.name, `val`)
        doCommit()
    }

    fun put(key: Key, `val`: Int) {
        doEdit()
        mEditor!!.putInt(key.name, `val`)
        doCommit()
    }

    fun put(key: Key, `val`: Boolean) {
        doEdit()
        mEditor!!.putBoolean(key.name, `val`)
        doCommit()
    }

    fun put(key: Key, `val`: Float) {
        doEdit()
        mEditor!!.putFloat(key.name, `val`)
        doCommit()
    }

    /**
     * Convenience method for storing doubles.
     *
     *
     * There may be instances where the accuracy of a double is desired.
     * SharedPreferences does not handle doubles so they have to
     * cast to and from String.

     * @param key The enum of the preference to store.
     * *
     * @param val The new value for the preference.
     */
    fun put(key: Key, `val`: Double) {
        doEdit()
        mEditor!!.putString(key.name, `val`.toString())
        doCommit()
    }

    fun put(key: Key, `val`: Long) {
        doEdit()
        mEditor!!.putLong(key.name, `val`)
        doCommit()
    }

    fun getString(key: Key, defaultValue: String): String {
        return mPref.getString(key.name, defaultValue)
    }

    fun getString(key: Key): String {
        return mPref.getString(key.name, null)
    }

    fun getInt(key: Key): Int {
        return mPref.getInt(key.name, 0)
    }

    fun getInt(key: Key, defaultValue: Int): Int {
        return mPref.getInt(key.name, defaultValue)
    }

    fun getLong(key: Key): Long {
        return mPref.getLong(key.name, 0)
    }

    fun getLong(key: Key, defaultValue: Long): Long {
        return mPref.getLong(key.name, defaultValue)
    }

    fun getFloat(key: Key): Float {
        return mPref.getFloat(key.name, 0f)
    }

    fun getFloat(key: Key, defaultValue: Float): Float {
        return mPref.getFloat(key.name, defaultValue)
    }

    /**
     * Convenience method for retrieving doubles.
     *
     *
     * There may be instances where the accuracy of a double is desired.
     * SharedPreferences does not handle doubles so they have to
     * cast to and from String.

     * @param key The enum of the preference to fetch.
     */
    @JvmOverloads fun getDouble(key: Key, defaultValue: Double = 0.0): Double {
        try {
            return java.lang.Double.valueOf(mPref.getString(key.name, defaultValue.toString()))!!
        } catch (nfe: NumberFormatException) {
            return defaultValue
        }

    }

    fun getBoolean(key: Key, defaultValue: Boolean): Boolean {
        return mPref.getBoolean(key.name, defaultValue)
    }

    fun getBoolean(key: Key): Boolean {
        return mPref.getBoolean(key.name, false)
    }

    /**
     * Remove keys from SharedPreferences.

     * @param keys The enum of the key(s) to be removed.
     */
    fun remove(vararg keys: Key) {
        doEdit()
        for (key in keys) {
            mEditor!!.remove(key.name)
        }
        doCommit()
    }

    /**
     * Remove all keys from SharedPreferences.
     */
    fun clear() {
        doEdit()
        mEditor!!.clear()
        doCommit()
    }

    fun edit() {
        mBulkUpdate = true
        mEditor = mPref.edit()
    }

    fun commit() {
        mBulkUpdate = false
        mEditor!!.commit()
        mEditor = null
    }

    private fun doEdit() {
        if (!mBulkUpdate && mEditor == null) {
            mEditor = mPref.edit()
        }
    }

    private fun doCommit() {
        if (!mBulkUpdate && mEditor != null) {
            mEditor!!.commit()
            mEditor = null
        }
    }

    companion object {
        private val SETTINGS_NAME = "candrago-client-settings"
        private var sSharedPrefs: AppPreferences? = null


        fun getInstance(context: Context): AppPreferences {
            if (sSharedPrefs == null) {
                sSharedPrefs = AppPreferences(context.applicationContext)
            }
            return sSharedPrefs as AppPreferences
        }

        val instance: AppPreferences
            get() {
                if (sSharedPrefs != null) {
                    return sSharedPrefs as AppPreferences
                }
                throw IllegalArgumentException("Should use getInstance(Context) at least once before using this method.")
            }
    }
}