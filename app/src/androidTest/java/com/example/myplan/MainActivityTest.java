package com.example.myplan;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;


import com.example.myplan.data.FileDataSource;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    private FileDataSource server;
    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        server = new FileDataSource(context);
        server.load();
    }

    @After
    public void tearDown() throws Exception {
        server.save();
    }

    @Test
    public void mainActivityTest() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.home_add_btn),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.main_drawer_layout),
                                        0),
                                4),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.add_plan_title_editText),
                        childAtPosition(
                                allOf(withId(R.id.add_plan_bgimage),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("新建测试1"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.add_plan_remarks_editText),
                        childAtPosition(
                                allOf(withId(R.id.add_plan_bgimage),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                1)),
                                3),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("新建备注测试1"), closeSoftKeyboard());

        DataInteraction relativeLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.add_plan_menu_listView),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                2)))
                .atPosition(0);
        relativeLayout.perform(click());

        ViewInteraction accessibleTextView = onView(
                allOf(withId(R.id.mdtp_date_picker_year), withText("2019"),
                        childAtPosition(
                                allOf(withId(R.id.mdtp_day_picker_selected_date_layout),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        accessibleTextView.perform(click());

        DataInteraction textViewWithCircularIndicator = onData(anything())
                .inAdapterView(childAtPosition(
                        allOf(withId(R.id.mdtp_animator), withContentDescription("年份列表: 2019")),
                        1))
                .atPosition(120);
        textViewWithCircularIndicator.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.mdtp_ok), withText("确定"),
                        childAtPosition(
                                allOf(withId(R.id.mdtp_done_background),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.mdtp_ok), withText("确定"),
                        childAtPosition(
                                allOf(withId(R.id.mdtp_done_background),
                                        childAtPosition(
                                                withId(R.id.mdtp_time_picker_dialog),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatButton2.perform(click());

        DataInteraction relativeLayout2 = onData(anything())
                .inAdapterView(allOf(withId(R.id.add_plan_menu_listView),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                2)))
                .atPosition(1);
        relativeLayout2.perform(click());

        DataInteraction appCompatTextView = onData(anything())
                .inAdapterView(allOf(withId(R.id.select_dialog_listview),
                        childAtPosition(
                                withId(R.id.contentPanel),
                                0)))
                .atPosition(2);
        appCompatTextView.perform(click());

        DataInteraction relativeLayout3 = onData(anything())
                .inAdapterView(allOf(withId(R.id.add_plan_menu_listView),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                2)))
                .atPosition(2);
        relativeLayout3.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.cancel_button), withText("取消"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatButton3.perform(click());

        DataInteraction relativeLayout4 = onData(anything())
                .inAdapterView(allOf(withId(R.id.add_plan_menu_listView),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                2)))
                .atPosition(3);
        relativeLayout4.perform(click());

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(allOf(withId(R.id.select_dialog_listview),
                        childAtPosition(
                                withId(R.id.contentPanel),
                                0)))
                .atPosition(0);
        appCompatCheckedTextView.perform(click());

        DataInteraction appCompatCheckedTextView2 = onData(anything())
                .inAdapterView(allOf(withId(R.id.select_dialog_listview),
                        childAtPosition(
                                withId(R.id.contentPanel),
                                0)))
                .atPosition(1);
        appCompatCheckedTextView2.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(android.R.id.button1), withText("确定"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                3)));
        appCompatButton4.perform(scrollTo(), click());

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.button_ok), withContentDescription("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.add_plan_toolBar),
                                        1),
                                0),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.list_item_title_textView), withText("新建测试1"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.list_item_layout),
                                        1),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("新建测试1")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.list_item_date_textView), withText("2020年12月25日"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.list_item_layout),
                                        1),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("2020年12月25日")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.list_item_remarks_textView), withText("新建备注测试1"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.list_item_layout),
                                        1),
                                2),
                        isDisplayed()));
        textView3.check(matches(withText("新建备注测试1")));

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.home_add_btn),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.main_drawer_layout),
                                        0),
                                4),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.add_plan_title_editText),
                        childAtPosition(
                                allOf(withId(R.id.add_plan_bgimage),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("新建测试2"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.add_plan_remarks_editText),
                        childAtPosition(
                                allOf(withId(R.id.add_plan_bgimage),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                1)),
                                3),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("新建备注测试2"), closeSoftKeyboard());

        DataInteraction relativeLayout5 = onData(anything())
                .inAdapterView(allOf(withId(R.id.add_plan_menu_listView),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                2)))
                .atPosition(0);
        relativeLayout5.perform(click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.mdtp_ok), withText("确定"),
                        childAtPosition(
                                allOf(withId(R.id.mdtp_done_background),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatButton5.perform(click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.mdtp_ok), withText("确定"),
                        childAtPosition(
                                allOf(withId(R.id.mdtp_done_background),
                                        childAtPosition(
                                                withId(R.id.mdtp_time_picker_dialog),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatButton6.perform(click());

        DataInteraction relativeLayout6 = onData(anything())
                .inAdapterView(allOf(withId(R.id.add_plan_menu_listView),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                2)))
                .atPosition(1);
        relativeLayout6.perform(click());

        DataInteraction appCompatTextView2 = onData(anything())
                .inAdapterView(allOf(withId(R.id.select_dialog_listview),
                        childAtPosition(
                                withId(R.id.contentPanel),
                                0)))
                .atPosition(2);
        appCompatTextView2.perform(click());

        DataInteraction relativeLayout7 = onData(anything())
                .inAdapterView(allOf(withId(R.id.add_plan_menu_listView),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                2)))
                .atPosition(3);
        relativeLayout7.perform(click());

        DataInteraction appCompatCheckedTextView3 = onData(anything())
                .inAdapterView(allOf(withId(R.id.select_dialog_listview),
                        childAtPosition(
                                withId(R.id.contentPanel),
                                0)))
                .atPosition(2);
        appCompatCheckedTextView3.perform(click());

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(android.R.id.button1), withText("确定"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                3)));
        appCompatButton7.perform(scrollTo(), click());

        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.button_ok), withContentDescription("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.add_plan_toolBar),
                                        1),
                                0),
                        isDisplayed()));
        actionMenuItemView2.perform(click());

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.list_item_title_textView), withText("新建测试2"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.list_item_layout),
                                        1),
                                0),
                        isDisplayed()));
        textView4.check(matches(withText("新建测试2")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.list_item_remarks_textView), withText("新建备注测试2"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.list_item_layout),
                                        1),
                                2),
                        isDisplayed()));
        textView5.check(matches(withText("新建备注测试2")));

        DataInteraction relativeLayout8 = onData(anything())
                .inAdapterView(allOf(withId(R.id.home_listView),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                3)))
                .atPosition(0);
        relativeLayout8.perform(click());

        ViewInteraction actionMenuItemView3 = onView(
                allOf(withId(R.id.editor_icon), withContentDescription("编辑"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.editor_menu_toolBar),
                                        1),
                                3),
                        isDisplayed()));
        actionMenuItemView3.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.add_plan_title_editText), withText("新建测试1"),
                        childAtPosition(
                                allOf(withId(R.id.add_plan_bgimage),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("修改测试1"));

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.add_plan_title_editText), withText("修改测试1"),
                        childAtPosition(
                                allOf(withId(R.id.add_plan_bgimage),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatEditText6.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.add_plan_remarks_editText), withText("新建备注测试1"),
                        childAtPosition(
                                allOf(withId(R.id.add_plan_bgimage),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                1)),
                                3),
                        isDisplayed()));
        appCompatEditText7.perform(replaceText("修改备注测试1"));

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.add_plan_remarks_editText), withText("修改备注测试1"),
                        childAtPosition(
                                allOf(withId(R.id.add_plan_bgimage),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                1)),
                                3),
                        isDisplayed()));
        appCompatEditText8.perform(closeSoftKeyboard());

        DataInteraction relativeLayout9 = onData(anything())
                .inAdapterView(allOf(withId(R.id.add_plan_menu_listView),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                2)))
                .atPosition(0);
        relativeLayout9.perform(click());

        ViewInteraction appCompatButton8 = onView(
                allOf(withId(R.id.mdtp_ok), withText("确定"),
                        childAtPosition(
                                allOf(withId(R.id.mdtp_done_background),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatButton8.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton9 = onView(
                allOf(withId(R.id.mdtp_ok), withText("确定"),
                        childAtPosition(
                                allOf(withId(R.id.mdtp_done_background),
                                        childAtPosition(
                                                withId(R.id.mdtp_time_picker_dialog),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatButton9.perform(click());

        DataInteraction relativeLayout10 = onData(anything())
                .inAdapterView(allOf(withId(R.id.add_plan_menu_listView),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                2)))
                .atPosition(1);
        relativeLayout10.perform(click());

        DataInteraction appCompatTextView3 = onData(anything())
                .inAdapterView(allOf(withId(R.id.select_dialog_listview),
                        childAtPosition(
                                withId(R.id.contentPanel),
                                0)))
                .atPosition(1);
        appCompatTextView3.perform(click());

        DataInteraction relativeLayout11 = onData(anything())
                .inAdapterView(allOf(withId(R.id.add_plan_menu_listView),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                2)))
                .atPosition(3);
        relativeLayout11.perform(click());

        DataInteraction appCompatCheckedTextView4 = onData(anything())
                .inAdapterView(allOf(withId(R.id.select_dialog_listview),
                        childAtPosition(
                                withId(R.id.contentPanel),
                                0)))
                .atPosition(2);
        appCompatCheckedTextView4.perform(click());

        DataInteraction appCompatCheckedTextView5 = onData(anything())
                .inAdapterView(allOf(withId(R.id.select_dialog_listview),
                        childAtPosition(
                                withId(R.id.contentPanel),
                                0)))
                .atPosition(3);
        appCompatCheckedTextView5.perform(click());

        ViewInteraction appCompatButton10 = onView(
                allOf(withId(android.R.id.button1), withText("确定"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                3)));
        appCompatButton10.perform(scrollTo(), click());

        ViewInteraction actionMenuItemView4 = onView(
                allOf(withId(R.id.button_ok), withContentDescription("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.add_plan_toolBar),
                                        1),
                                0),
                        isDisplayed()));
        actionMenuItemView4.perform(click());

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.editor_bkg_title_textView), withText("修改测试1"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.editor_bkg_frame),
                                        0),
                                0),
                        isDisplayed()));
        textView6.check(matches(withText("修改测试1")));

        ViewInteraction appCompatImageButton = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.editor_menu_toolBar),
                                childAtPosition(
                                        withId(R.id.editor_menu_drawerLayout),
                                        0)),
                        0),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.list_item_title_textView), withText("修改测试1"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.list_item_layout),
                                        1),
                                0),
                        isDisplayed()));
        textView7.check(matches(withText("修改测试1")));

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.list_item_date_textView), withText("2020年12月30日"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.list_item_layout),
                                        1),
                                1),
                        isDisplayed()));
        textView8.check(matches(withText("2020年12月30日")));

        ViewInteraction textView9 = onView(
                allOf(withId(R.id.list_item_remarks_textView), withText("修改备注测试1"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.list_item_layout),
                                        1),
                                2),
                        isDisplayed()));
        textView9.check(matches(withText("修改备注测试1")));

        DataInteraction relativeLayout12 = onData(anything())
                .inAdapterView(allOf(withId(R.id.home_listView),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                3)))
                .atPosition(1);
        relativeLayout12.perform(click());

        ViewInteraction actionMenuItemView5 = onView(
                allOf(withId(R.id.delete_icon), withContentDescription("删除"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.editor_menu_toolBar),
                                        1),
                                1),
                        isDisplayed()));
        actionMenuItemView5.perform(click());

        ViewInteraction appCompatButton11 = onView(
                allOf(withId(android.R.id.button1), withText("确定"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton11.perform(scrollTo(), click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("打开侧滑菜单"),
                        childAtPosition(
                                allOf(withId(R.id.main_toolBar),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.main_navigationView),
                                        0)),
                        4),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction appCompatButton12 = onView(
                allOf(withId(android.R.id.button1), withText("确定"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton12.perform(scrollTo(), click());
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
