package net.xaethos.todofrontend.test

import android.app.Activity
import android.support.test.rule.ActivityTestRule

inline fun <reified T : Activity> activityTestRule(): ActivityTestRule<T> = ActivityTestRule(T::class.java)
