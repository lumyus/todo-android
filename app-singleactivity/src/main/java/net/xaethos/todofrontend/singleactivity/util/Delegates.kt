package net.xaethos.todofrontend.singleactivity.util

import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction2
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

fun textViewText(textView: TextView): ReadWriteProperty<Any?, CharSequence?> =
        mapping<TextView, CharSequence?>(textView, TextView::getText, TextView::setText)

fun compoundButtonChecked(button: CompoundButton): ReadWriteProperty<Any?, Boolean> =
        mapping(button, CompoundButton::isChecked, CompoundButton::setChecked)

fun viewEnabled(view: View): ReadWriteProperty<Any?, Boolean> =
        mapping(view, View::isEnabled, View::setEnabled)

fun <T, V> forwarding(target: T,
                      kProperty: KProperty1<T, V>) = object : ReadOnlyProperty<Any?, V> {
    override fun getValue(thisRef: Any?, property: KProperty<*>) = kProperty.getter(target)
}

fun <T, V> forwarding(target: T,
                      kProperty: KMutableProperty1<T, V>) = object : ReadWriteProperty<Any?, V> {
    override fun getValue(thisRef: Any?, property: KProperty<*>) = kProperty.getter(target)
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: V) {
        kProperty.setter(target, value)
    }
}

fun <T, V> mapping(target: T,
                   getter: KFunction1<T, V>,
                   setter: KFunction2<T, V, Unit>) = object : ReadWriteProperty<Any?, V> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): V = getter(target)
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: V) {
        setter(target, value)
    }
}
