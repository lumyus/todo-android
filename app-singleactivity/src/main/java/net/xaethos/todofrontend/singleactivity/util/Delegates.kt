package net.xaethos.todofrontend.singleactivity.util

import android.widget.TextView
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun textViewText(textView: TextView): ReadWriteProperty<Any?, CharSequence?> =
        TextViewTextDelegate(textView)

private class TextViewTextDelegate(private val textView: TextView) : ReadWriteProperty<Any?, CharSequence?> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): CharSequence? {
        return textView.text
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: CharSequence?) {
        textView.text = value
    }
}
