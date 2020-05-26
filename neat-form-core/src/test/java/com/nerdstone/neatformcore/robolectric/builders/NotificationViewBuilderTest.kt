package com.nerdstone.neatformcore.robolectric.builders

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.TestNeatFormApp
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher
import com.nerdstone.neatformcore.views.widgets.NotificationNFormView
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowAlertDialog
import org.robolectric.shadows.ShadowTextView

@RunWith(RobolectricTestRunner::class)
@Config(application = TestNeatFormApp::class, shadows = [ShadowTextView::class])
class NotificationViewBuilderTest : BaseJsonViewBuilderTest() {

    private val notificationNFormView = NotificationNFormView(activity.get())
    private val viewProperty = spyk(NFormViewProperty())

    @Before
    fun `Before doing anything else`() {
        notificationNFormView.formValidator = this.formValidator
        viewProperty.name = "notification_view"
        viewProperty.type = "toast_notification"
        notificationNFormView.viewProperties = viewProperty
        viewProperty.viewAttributes = hashMapOf(
            "text" to "I am sample notification text",
            "title" to "Notification"
        )
    }

    @Test
    fun `Should have title and text`() {
        ViewUtils.setupView(notificationNFormView, viewProperty, formBuilder)
        val constraintLayout = notificationNFormView.getChildAt(0) as ViewGroup
        val titleTextView = constraintLayout.findViewById<TextView>(R.id.notificationTitleTextView)
        val notificationTextTextView =
            constraintLayout.findViewById<TextView>(R.id.notificationTextTextView)
        Assert.assertEquals(constraintLayout.childCount, 5)
        Assert.assertEquals(titleTextView.text.toString(), "Notification")
        Assert.assertEquals(
            notificationTextTextView.text.toString(),
            "I am sample notification text"
        )
        //Validation is set to true always
        Assert.assertTrue(notificationNFormView.validateValue())
    }

    @Test
    fun `Should change text and background color of toast notification`() {
        notificationNFormView.viewProperties.viewAttributes?.set("text_color", "#cccccc")
        notificationNFormView.viewProperties.viewAttributes?.set("background_color", "#fc3c5c")
        ViewUtils.setupView(notificationNFormView, viewProperty, formBuilder)
        val constraintLayout = notificationNFormView.getChildAt(0) as ViewGroup
        val titleTextView = constraintLayout.findViewById<TextView>(R.id.notificationTitleTextView)
        Assert.assertNotEquals(titleTextView.currentTextColor ,  -1)
        Assert.assertNotEquals((notificationNFormView.background as ColorDrawable).color ,  -1)
    }

    @Test
    fun `Should dismiss the notification when cancel is clicked`() {
        notificationNFormView.viewProperties.viewAttributes?.set("dismissible", true)
        ViewUtils.setupView(notificationNFormView, viewProperty, formBuilder)
        val constraintLayout = notificationNFormView.getChildAt(0) as ViewGroup
        val cancelIcon = constraintLayout.findViewById<ImageView>(R.id.notificationCancelIcon)
        Assert.assertEquals(cancelIcon.visibility, View.VISIBLE)
        cancelIcon.performClick()
        Assert.assertEquals(notificationNFormView.visibility, View.GONE)
    }

    @Test
    fun `Should create the right notification type`() {
        notificationNFormView.viewProperties.viewAttributes?.set(
            "notification_dialog_title",
            "Dialog title"
        )
        notificationNFormView.viewProperties.viewAttributes?.set(
            "notification_dialog_text",
            "I am dialog text"
        )
        ViewUtils.setupView(notificationNFormView, viewProperty, formBuilder)
        val constraintLayout = notificationNFormView.getChildAt(0) as ViewGroup
        val infoIcon = constraintLayout.findViewById<ImageView>(R.id.notificationInfoIcon)
        Assert.assertEquals(infoIcon.visibility, View.VISIBLE)
        infoIcon.performClick()
        val shownDialogs = ShadowAlertDialog.getShownDialogs()
        Assert.assertEquals(shownDialogs.size, 1)
        Assert.assertTrue(ShadowAlertDialog.getLatestDialog() is AlertDialog)
    }

    @Test
    fun `Should update text and title dynamically`() {
        notificationNFormView.viewProperties.viewAttributes?.set(
            "title", "Title: {title_calculation}"
        )
        notificationNFormView.viewProperties.viewAttributes?.set("text", "Text: {text_calculation}")
        ViewUtils.setupView(notificationNFormView, viewProperty, formBuilder)
        val constraintLayout = notificationNFormView.getChildAt(0) as ViewGroup

        //Title should be updated when calculation changes
        notificationNFormView.onCalculationChanged(Pair("title_calculation", "Your goes here"))
        val titleTextView = constraintLayout.findViewById<TextView>(R.id.notificationTitleTextView)
        Assert.assertEquals(titleTextView.text.toString(), "Title: Your goes here")

        //Text should be updated when calculation changes
        notificationNFormView.onCalculationChanged(Pair("text_calculation", "Your goes here"))
        val notificationTextTextView =
            constraintLayout.findViewById<TextView>(R.id.notificationTextTextView)
        Assert.assertEquals(notificationTextTextView.text.toString(), "Text: Your goes here")
    }

    @After
    fun `After everything else`() {
        unmockkAll()
    }
}