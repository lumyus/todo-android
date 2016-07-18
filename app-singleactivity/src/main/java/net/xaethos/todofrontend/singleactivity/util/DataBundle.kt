package net.xaethos.todofrontend.singleactivity.util

import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class DataBundle(val bundle: Bundle) {
    companion object {
        val bundleString = object : ReadWriteProperty<DataBundle, String?> {
            override fun getValue(thisRef: DataBundle, property: KProperty<*>): String? {
                return thisRef.bundle.getString(property.name)
            }

            override fun setValue(thisRef: DataBundle, property: KProperty<*>, value: String?) {
                thisRef.bundle.putString(property.name, value)
            }
        }
    }
}
