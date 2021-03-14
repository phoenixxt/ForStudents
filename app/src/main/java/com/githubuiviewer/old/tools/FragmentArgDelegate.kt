package com.githubuiviewer.old.tools

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class FragmentArgsDelegate<T : Any> : ReadWriteProperty<Fragment, T> {

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) =
        with(thisRef.arguments ?: Bundle().also(thisRef::setArguments)) {
            when (value) {
                is String -> putString(property.name, value)
                is Bundle -> putBundle(property.name, value)
                is Parcelable -> putParcelable(property.name, value)
                else -> throw IllegalStateException("Error type of property ${property.name}")
            }
        }

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return thisRef.arguments?.get(property.name) as? T
            ?: throw IllegalStateException("Property ${property.name} error")
    }
}