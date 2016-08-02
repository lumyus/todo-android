package net.xaethos.todofrontend.singleactivity.features

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.hasDescendant
import android.support.test.espresso.matcher.ViewMatchers.hasSibling
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.isNotChecked
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.test.suitebuilder.annotation.LargeTest
import net.xaethos.todofrontend.singleactivity.MainActivity
import net.xaethos.todofrontend.singleactivity.R
import net.xaethos.todofrontend.singleactivity.test.activityTestRule
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class HappyPathFeature {

    @get:Rule var activityRule = activityTestRule<MainActivity>()

    @Test fun seeTodoDetails() {
        // Scroll down list and tap on to do
        onView(withId(R.id.todo_list))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(1))
        onView(withText("Do stuff")).perform(click())

        // Check that the details are shown.
        onView(withText("I have stuff to do")).check(matches(isDisplayed()))

        // Go back to the list
        pressBack()
        onView(withId(R.id.todo_list)).check(matches(isDisplayed()))
    }

    @Test fun createTodo() {
        // Fabulous tap
        onView(withId(R.id.fab)).perform(click())

        // Check that the create view is shown.
//        onView(withText("New todo")).check(matches(isDisplayed()))

        // Fill in form and submit
        onView(withId(R.id.todo_title)).perform(typeText("Test creating todo"))
        onView(withId(R.id.todo_detail)).perform(typeText("And then we're peachy"))
        onView(withId(R.id.fab)).perform(click())

        // We should be back in the list, with our new to do displayed
        onView(withId(R.id.todo_list)).check(matches(isDisplayed()))
        onView(withText("Test creating todo")).check(matches(isDisplayed()))

        // Since we tested creating a to do, mark it as done
        onView(allOf(isNotChecked(), hasSibling(hasDescendant(withText("Test creating todo")))))
                .perform(click())
    }

    @Test fun editTodo() {
        // Scroll down list and tap on to do
        onView(withId(R.id.todo_list))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(1))
        onView(withText("Do stuff")).perform(click())

        // Check that the details are shown.
        onView(withText(containsString("I have stuff to do"))).check(matches(isDisplayed()))

        // Fabulous tap
        onView(withId(R.id.fab)).perform(click())

        // Check that the edit view is shown.
//        onView(withText("Edit todo")).check(matches(isDisplayed()))

        // Fill in form and submit
        onView(withId(R.id.todo_detail)).perform(ViewActions.replaceText("I have stuff to test"))
        onView(withId(R.id.fab)).perform(click())

        // We should be back in the detail view, with our updates
        onView(withText("I have stuff to test")).check(matches(isDisplayed()))
    }

}
