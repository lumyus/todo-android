package net.xaethos.todofrontend.singleactivity.util

import android.os.Bundle
import android.widget.TextView
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun textViewText(textView: TextView): ReadWriteProperty<Any?, CharSequence?> =
        TextViewTextDelegate(textView)

fun Bundle.stringProperty(default: String? = null): ReadWriteProperty<Any?, String?> =
        BundleDelegate(this, default)

private class TextViewTextDelegate(private val textView: TextView) : ReadWriteProperty<Any?, CharSequence?> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): CharSequence? {
        return textView.text
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: CharSequence?) {
        textView.text = value
    }
}

private class BundleDelegate(private val bundle: Bundle, private val default: String? = null) : ReadWriteProperty<Any?, String?> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): String? {
        return bundle.getString(property.name)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) {
        bundle.putString(property.name, value)
    }
}
