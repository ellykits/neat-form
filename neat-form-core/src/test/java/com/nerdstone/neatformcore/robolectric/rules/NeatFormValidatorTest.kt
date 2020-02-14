package com.nerdstone.neatformcore.robolectric.rules

import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import com.google.android.material.textfield.TextInputLayout
import com.nerdstone.neatformcore.CoroutineTestRule
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.TestNeatFormApp
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.form.json.JsonFormBuilder
import com.nerdstone.neatformcore.form.json.JsonFormConstants
import com.nerdstone.neatformcore.views.containers.MultiChoiceCheckBox
import com.nerdstone.neatformcore.views.containers.RadioGroupView
import com.nerdstone.neatformcore.views.containers.VerticalRootView
import com.nerdstone.neatformcore.views.widgets.CheckBoxNFormView
import com.nerdstone.neatformcore.views.widgets.NumberSelectorNFormView
import com.nerdstone.neatformcore.views.widgets.SpinnerNFormView
import com.nerdstone.neatformcore.views.widgets.TextInputEditTextNFormView
import io.mockk.spyk
import kotlinx.android.synthetic.main.sample_custom_form_layout.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

const val VALIDATION_FORM = """
{
  "form": "Validation test",
  "rules_file": "rules/yml/sample_one_form_rules.yml",
  "steps": [
    {
      "title": "Test Validation",
      "fields": [
        {
          "name": "adult",
          "type": "text_input_edit_text",
          "properties": {
            "hint": "Whats your phone number"
          },
          "meta_data": {
            "openmrs_entity": "",
            "openmrs_entity_id": "",
            "openmrs_entity_parent": ""
          },
          "validation": [
            {
              "condition": "value.length() <= 10",
              "message": "value should be less than or equal to ten digits"
            }
          ],
          "required_status": "Yes:please add phone number"
        },
        {
          "meta_data": {
            "openmrs_entity": "",
            "openmrs_entity_id": "",
            "openmrs_entity_parent": ""
          },
          "name": "email_subscription",
          "type": "checkbox",
          "properties": {
            "text": "Subscribe to email notifications"
          },
          "required_status": "yes:Please specify if you want subscription"
        },
        {
          "name": "gender",
          "type": "spinner",
          "properties": {
            "text": "Choose your gender"
          },
          "options": [
            {
              "name": "female",
              "text": "Female",
              "meta_data": {
                "openmrs_entity": "",
                "openmrs_entity_id": "A123390123123",
                "openmrs_entity_parent": ""
              }
            },
            {
              "name": "male",
              "text": "Male",
              "meta_data": {
                "openmrs_entity": "",
                "openmrs_entity_id": "S8918313",
                "openmrs_entity_parent": ""
              }
            }
          ],
          "required_status": "yes:Please specify your gender"
        },
        {
          "name": "choose_language",
          "type": "multi_choice_checkbox",
          "properties": {
            "text": "Pick the languages you are proficient in."
          },
          "options": [
            {
              "name": "eng",
              "text": "English",
              "meta_data": {
                "openmrs_entity": "",
                "openmrs_entity_id": "A123123123123",
                "openmrs_entity_parent": ""
              }
            },
            {
              "name": "french",
              "text": "French",
              "meta_data": {
                "openmrs_entity": "",
                "openmrs_entity_id": "A123123123123",
                "openmrs_entity_parent": ""
              }
            }
          ],
          "validation": [
            {
              "condition": "value['french'] == null",
              "message": "You are not from France!"
            }
          ],
          "required_status": "yes:Please specify your languages"
        },
        {
          "name": "wiki_contribution",
          "type": "radio_group",
          "properties": {
            "text": "Have you ever contributed to or written a page in the Wiki?"
          },
          "options": [
            {
              "name": "yes",
              "text": "Yes"
            },
            {
              "name": "dont_know",
              "text": "Dont know"
            }
          ],
          "validation": [
            {
              "condition": "!value.containsKey('dont_know')",
              "message": "Don't know is not an option"
            }
          ],
          "required_status": "yes:Wiki contribution field is required."
        },
        {
          "name": "no_prev_pregnancies",
          "type": "number_selector",
          "properties": {
            "visible_numbers": "5",
            "max_value": "10",
            "first_number": "0",
            "text": "Number of previous pregnancies"
          },
          "validation": [
            {
              "condition": "value < 4",
              "message": "Advice woman to register for Family Planning"
            }
          ],
          "required_status": "True:Please specify the time you clocked in"
        }
      ]
    }
  ]
}
    """

@RunWith(RobolectricTestRunner::class)
@Config(application = TestNeatFormApp::class)
@ExperimentalCoroutinesApi
@Ignore("I still don't understand how this test passes individually but fails when run with others")
class `Test form validation ` {

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()
    private val activity = Robolectric.buildActivity(AppCompatActivity::class.java).setup()
    private val mainLayout: LinearLayout = LinearLayout(activity.get())
    private lateinit var formBuilder: JsonFormBuilder

    @Test
    fun `Should display error message and return empty map when required fields are missing`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            formBuilder = spyk(
                JsonFormBuilder(VALIDATION_FORM.trimIndent(), activity.get(), mainLayout)
            )

            formBuilder.defaultContextProvider = coroutinesTestRule.testDispatcherProvider
            launch { formBuilder.buildForm() }.join()

            Assert.assertTrue(formBuilder.getFormData().isEmpty())
            Assert.assertTrue(formBuilder.getFormDataAsJson() == "")
            Assert.assertTrue(formBuilder.formValidator.requiredFields.size == 6)
        }

    @Test
    fun `Should display error message when there is invalid input`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            formBuilder = spyk(
                JsonFormBuilder(VALIDATION_FORM.trimIndent(), activity.get(), mainLayout)
            )

            formBuilder.defaultContextProvider = coroutinesTestRule.testDispatcherProvider
            launch { formBuilder.buildForm() }.join()

            val scrollView = mainLayout.getChildAt(0) as ScrollView
            val verticalRootView = scrollView.getChildAt(0) as VerticalRootView
            val editTextNFormView = verticalRootView.getChildAt(0) as TextInputEditTextNFormView
            val multiChoiceCheckBox = verticalRootView.getChildAt(3) as MultiChoiceCheckBox


            (editTextNFormView.viewDetails.view as TextInputLayout).editText?.setText("90091239009123")
            editTextNFormView.viewDetails.view.visibility =
                View.VISIBLE // Will trigger handling required fields
            Assert.assertTrue(editTextNFormView.error != null && editTextNFormView.error == "value should be less than or equal to ten digits")
            Assert.assertTrue(formBuilder.formValidator.invalidFields.size == 1)
            Assert.assertTrue(formBuilder.formValidator.requiredFields.size == 5)
            Assert.assertTrue(formBuilder.getFormData().isEmpty())

            val multiChoiceCheckBoxErrorMessage = multiChoiceCheckBox.findViewById<TextView>(R.id.errorMessageTextView)
            val checkBox1 = multiChoiceCheckBox.getChildAt(1) as CheckBox
            checkBox1.isChecked = true
            multiChoiceCheckBox.viewDetails.view.visibility = View.VISIBLE
            Assert.assertTrue(multiChoiceCheckBoxErrorMessage.text == "")

            checkBox1.isChecked = false
            multiChoiceCheckBox.viewDetails.view.visibility = View.VISIBLE
            Assert.assertTrue(multiChoiceCheckBoxErrorMessage.text == "Please specify your languages")

        }

    @Test
    fun `Should return valid form data when validation succeeds`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            updateViewValues()
            Assert.assertTrue(formBuilder.getFormData().isNotEmpty())
            Assert.assertTrue(formBuilder.getFormData().size == 6)
            Assert.assertTrue(formBuilder.getFormData().containsKey("adult"))
            Assert.assertTrue(formBuilder.getFormData().containsKey("email_subscription"))
            Assert.assertTrue(formBuilder.getFormData().containsKey("gender"))
            Assert.assertTrue(formBuilder.getFormData().containsKey("choose_language"))
            Assert.assertTrue(formBuilder.getFormData().containsKey("wiki_contribution"))
            Assert.assertTrue(formBuilder.getFormData().containsKey("no_prev_pregnancies"))
        }

    @Test
    fun `Should return field values with their metadata`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            updateViewValues()
            val formData = formBuilder.getFormData()
            Assert.assertTrue(formData["adult"]?.metadata != null && formData["adult"]?.value == "1234567890")
            Assert.assertTrue(
                formData["email_subscription"]?.metadata != null && (formData["email_subscription"]?.value as HashMap<*, *>).containsKey(
                    "email_subscription"
                )
            )
            Assert.assertTrue((formData["gender"]?.value as NFormViewData).metadata != null)
            Assert.assertTrue((formData["choose_language"]?.value as HashMap<*, *>).size == 1)
            Assert.assertTrue(
                (formData["wiki_contribution"]?.value as HashMap<*, *>).containsKey(
                    "yes"
                )
            )
            Assert.assertTrue(formData["no_prev_pregnancies"]?.value == 2)
        }

    @Test
    fun `Should return json string of form values`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            updateViewValues()
            Assert.assertNotNull(formBuilder.getFormDataAsJson())
            Assert.assertTrue(formBuilder.getFormDataAsJson().contains(JsonFormConstants.FORM_NAME))
            Assert.assertTrue(formBuilder.getFormDataAsJson().contains(JsonFormConstants.FORM_DATA))
        }

    /**
     * Update values of the views and trigger the trackRequireFields method by changing their visibility
     */
    private fun updateViewValues() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            formBuilder = spyk(
                JsonFormBuilder(VALIDATION_FORM.trimIndent(), activity.get(), mainLayout)
            )

            formBuilder.defaultContextProvider = coroutinesTestRule.testDispatcherProvider
            launch { formBuilder.buildForm() }.join()

            val scrollView = mainLayout.getChildAt(0) as ScrollView
            val verticalRootView = scrollView.getChildAt(0) as VerticalRootView
            val editTextNFormView = verticalRootView.getChildAt(0) as TextInputEditTextNFormView
            val checkBoxNFormView = verticalRootView.getChildAt(1) as CheckBoxNFormView
            val spinnerNFormView = verticalRootView.getChildAt(2) as SpinnerNFormView
            val multiChoiceCheckBox = verticalRootView.getChildAt(3) as MultiChoiceCheckBox
            val radioGroupView = verticalRootView.getChildAt(4) as RadioGroupView
            val numberSelectorNFormView = verticalRootView.getChildAt(5) as NumberSelectorNFormView

            (editTextNFormView.viewDetails.view as TextInputLayout).editText?.setText("1234567890")
            editTextNFormView.viewDetails.view.visibility = View.VISIBLE

            checkBoxNFormView.isChecked = true
            checkBoxNFormView.viewDetails.view.visibility = View.VISIBLE

            val materialSpinner = spinnerNFormView.getChildAt(0) as SmartMaterialSpinner<*>
            materialSpinner.setSelection(1)
            materialSpinner.isSelected = true
            spinnerNFormView.viewDetails.view.visibility = View.VISIBLE

            val checkBox1 = multiChoiceCheckBox.getChildAt(1) as CheckBox
            checkBox1.isChecked = true
            multiChoiceCheckBox.viewDetails.view.visibility = View.VISIBLE

            val radioButton1 = radioGroupView.getChildAt(1) as RadioButton
            radioButton1.isChecked = true
            radioGroupView.viewDetails.view.visibility = View.VISIBLE

            val linearLayout = numberSelectorNFormView.getChildAt(1) as LinearLayout
            val textView3 = linearLayout.getChildAt(2) as TextView
            textView3.performClick()
            numberSelectorNFormView.viewDetails.view.visibility = View.VISIBLE
        }
    }

}