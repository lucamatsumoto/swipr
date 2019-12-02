package com.example.myapplication;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BuyerPath {

    @Rule
    public ActivityTestRule<Login> mActivityTestRule = new ActivityTestRule<>(Login.class);

    @Test
    public void buyerPath() {
        ViewInteraction fn = onView(
                allOf(withText("Sign in"),
                        childAtPosition(
                                allOf(withId(R.id.sign_in_button),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                0),
                        isDisplayed()));
        fn.perform(click());

        ViewInteraction chip = onView(
                allOf(withId(R.id.b_plate), withText("B Plate"),
                        childAtPosition(
                                allOf(withId(R.id.dining_hall_group),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                6)),
                                0),
                        isDisplayed()));
        chip.perform(click());

        ViewInteraction chip2 = onView(
                allOf(withId(R.id.covel), withText("Covel"),
                        childAtPosition(
                                allOf(withId(R.id.dining_hall_group),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                6)),
                                1),
                        isDisplayed()));
        chip2.perform(click());

        ViewInteraction chip3 = onView(
                allOf(withId(R.id.de_neve), withText("De Neve"),
                        childAtPosition(
                                allOf(withId(R.id.dining_hall_group),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                6)),
                                2),
                        isDisplayed()));
        chip3.perform(click());

        ViewInteraction chip4 = onView(
                allOf(withId(R.id.feast), withText("Feast"),
                        childAtPosition(
                                allOf(withId(R.id.dining_hall_group),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                6)),
                                3),
                        isDisplayed()));
        chip4.perform(click());

        ViewInteraction chip5 = onView(
                allOf(withId(R.id.de_neve), withText("De Neve"),
                        childAtPosition(
                                allOf(withId(R.id.dining_hall_group),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                6)),
                                2),
                        isDisplayed()));
        chip5.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.price_value), withText("$10.00"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.activity_main),
                                        1),
                                4),
                        isDisplayed()));
        textView.check(matches(withText("$10.00")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.time_value), withText("14:00 - 18:00"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.activity_main),
                                        1),
                                7),
                        isDisplayed()));
        textView2.check(matches(withText("14:00 - 18:00")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.b_buyer_button), withText("BUYER"),
                        childAtPosition(
                                allOf(withId(R.id.buyer_tags),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                0)),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("BUYER")));

        ViewInteraction button = onView(
                allOf(withId(R.id.b_seller_tag),
                        childAtPosition(
                                allOf(withId(R.id.buyer_tags),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                0)),
                                1),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.post_swipe),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.activity_main),
                                        1),
                                11),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.post_swipe), withText("Find me a Swipe daddy!"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.activity_main),
                                        0),
                                15),
                        isDisplayed()));
        appCompatButton.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
