package net.xaethos.todofrontend.singleactivity.features

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.test.suitebuilder.annotation.LargeTest
import net.xaethos.todofrontend.singleactivity.R
import net.xaethos.todofrontend.singleactivity.SingleActivity
import net.xaethos.todofrontend.singleactivity.test.activityTestRule
import org.hamcrest.Matchers.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class HappyPathFeature {

    @get:Rule var activityRule = activityTestRule<SingleActivity>()

    @Test fun seeTodoDetails() {
        // Scroll down list and tap on to do
        onView(withId(R.id.todo_list))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(1))
        onView(withText("Do stuff")).perform(click())

        // Check that the details are shown.
        onView(withText(containsString("I have stuff to do"))).check(matches(isDisplayed()))
    }

}
