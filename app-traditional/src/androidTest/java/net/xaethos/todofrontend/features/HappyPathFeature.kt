package net.xaethos.todofrontend.features

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import net.xaethos.todofrontend.R
import net.xaethos.todofrontend.ToDoListActivity
import net.xaethos.todofrontend.test.activityTestRule
import org.hamcrest.Matchers.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class HappyPathFeature {

    @get:Rule var activityRule = activityTestRule<ToDoListActivity>()

    @Test fun seeToDoDetails() {
        // Scroll down list and tap on to do
        onView(withId(R.id.todo_list))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(20))
        onView(withText("To do 17")).perform(click())

        // Check that the details are shown.
        onView(withText(containsString("Details about To Do 17"))).check(matches(isDisplayed()))
    }

}
