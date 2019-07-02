package com.mycode.myapplication.ui

import android.os.SystemClock
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.mycode.myapplication.R

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit




class MainActivityTest{
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)
    private var activity : MainActivity? = null

    @Before
    fun setUp() {
        activity = activityRule.activity
    }
    @After
    fun tearDown() {
        activity = null
    }
    @Test
    fun firstRowWasClickedInRecyclerView(){
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(3))
        onView(withId(R.id.slideUp)).perform(swipeUp())
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions
            .actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(2))
        onView(withId(R.id.navigate)).check(matches(isEnabled()))
        onView(withId(R.id.navigate)).perform(click())
        onView(withId(R.id.navigate)).check(doesNotExist())
    }
    @Test
    fun secondRowWasClickedInRecyclerView(){
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(3))
        onView(withId(R.id.slideUp)).perform(swipeUp())
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions
            .actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(2))
        onView(withId(R.id.navigate)).check(matches(isEnabled()))
        onView(withId(R.id.navigate)).perform(click())
        onView(withId(R.id.navigate)).check(doesNotExist())
    }

    @Test
    fun thirdRowWasClickedInRecyclerView(){
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(3))
        onView(withId(R.id.slideUp)).perform(swipeUp())
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions
            .actionOnItemAtPosition<RecyclerView.ViewHolder>(2, click()))
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(2))
        onView(withId(R.id.navigate)).check(matches(isEnabled()))
        onView(withId(R.id.navigate)).perform(click())
        onView(withId(R.id.navigate)).check(doesNotExist())
    }
    @Test
    fun fourthRowWasClickedInRecyclerView(){
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(3))
        onView(withId(R.id.slideUp)).perform(swipeUp())
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions
            .actionOnItemAtPosition<RecyclerView.ViewHolder>(3, click()))
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(2))
        onView(withId(R.id.navigate)).check(matches(isEnabled()))
        onView(withId(R.id.navigate)).perform(click())
        onView(withId(R.id.navigate)).check(doesNotExist())
    }
    @Test
    fun fifthRowWasClickedInRecyclerView(){
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(3))
        onView(withId(R.id.slideUp)).perform(swipeUp())
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions
            .actionOnItemAtPosition<RecyclerView.ViewHolder>(4, click()))
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(2))
        onView(withId(R.id.navigate)).check(matches(isEnabled()))
        onView(withId(R.id.navigate)).perform(click())
        onView(withId(R.id.navigate)).check(doesNotExist())
    }

}