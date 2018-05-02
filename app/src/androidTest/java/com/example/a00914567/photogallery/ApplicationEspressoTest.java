package com.example.a00914567.photogallery;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.EspressoKey;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;

import junit.framework.AssertionFailedError;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


public class ApplicationEspressoTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void ensureSearchWorks(){
        onView(withId(R.id.searchButton)).perform(click());
        onView(withId(R.id.startDateView)).perform(typeText("2018/01/01"),closeSoftKeyboard());
        onView(withId(R.id.endDateView)).perform(typeText("2018/12/12"),closeSoftKeyboard());
        onView(withId(R.id.searchEnterButton)).perform(click());
        onView(withId(R.id.rightButton)).perform(click());
        onView(withId(R.id.rightButton)).perform(click());
        onView(withId(R.id.rightButton)).perform(click());
        onView(withId(R.id.rightButton)).perform(click());
        onView(withId(R.id.rightButton)).perform(click());
        onView(withId(R.id.leftButton)).perform(click());
        onView(withId(R.id.leftButton)).perform(click());
        onView(withId(R.id.leftButton)).perform(click());
    }
}
