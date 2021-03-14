package com.githubuiviewer.old.tools.sharedPrefsTools

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SharedPrefDelegate<T>(
    private val sharedPreferences: SharedPreferences,
    private val key: String,
    private val defValue: T,
) : ReadWriteProperty<Any?, T> {

  override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = with(sharedPreferences.edit()) {
    when (value) {
      is String -> putString(key, value)
      is Boolean -> putBoolean(key, value)
      else -> throw IllegalAccessError("Value was of unexpected type for key $key")
    }.apply()
  }

  @Suppress("UNCHECKED_CAST")
  override fun getValue(thisRef: Any?, property: KProperty<*>): T = with(sharedPreferences) {
    return when (defValue) {
      is String -> getString(key, defValue)
      is Boolean -> getBoolean(key, defValue)
      else -> null
    } as T
  }
}