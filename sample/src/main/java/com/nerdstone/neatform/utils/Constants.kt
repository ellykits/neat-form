package com.nerdstone.neatform.utils

object Constants {
    val PREVIOUS_DATA = """
        {
            "age": {
              "meta_data": {
                "openmrs_entity": "",
                "openmrs_entity_id": "",
                "openmrs_entity_parent": ""
              },
              "type": "TextInputEditTextNFormView",
              "value": "54"
            },
            "condition": {
              "type": "TextInputEditTextNFormView",
              "value": "Get's tired easily"
            },
            "child": {
              "meta_data": {
                "openmrs_entity": "",
                "openmrs_entity_id": "",
                "openmrs_entity_parent": ""
              },
              "type": "TextInputEditTextNFormView",
              "value": "child"
            },
            "dob": {
              "type": "DateTimePickerNFormView",
              "value": 1589555422331
            },
            "time": {
              "type": "DateTimePickerNFormView",
              "value": 1589555422335
            },
            "adult": {
              "type": "TextInputEditTextNFormView",
              "value": "0723721920"
            },
            "email_subscription": {
              "type": "CheckBoxNFormView",
              "value": {
                "email_subscription": "Subscribe to email notifications"
              },
              "visible": true
            },
            "no_prev_pregnancies": {
              "type": "NumberSelectorNFormView",
              "value": 1,
              "visible": true
            },
            "gender": {
              "type": "SpinnerNFormView",
              "value": {
                "value": "Female"
              }
            },
            "country": {
              "type": "SpinnerNFormView",
              "value": {
                "meta_data": {
                  "country_code": "+61"
                },
                "value": "Australia",
                "visible": true
              },
              "visible": true
            },
            "choose_language": {
              "type": "MultiChoiceCheckBox",
              "value": {
                "kisw": {
                  "meta_data": {
                    "openmrs_entity": "",
                    "openmrs_entity_id": "A123123123123",
                    "openmrs_entity_parent": ""
                  },
                  "value": "Kiswahili",
                  "visible": true
                },
                "french": {
                  "meta_data": {
                    "openmrs_entity": "",
                    "openmrs_entity_id": "A123123123123",
                    "openmrs_entity_parent": ""
                  },
                  "value": "French",
                  "visible": true
                }
              },
              "visible": true
            },
            "wiki_contribution": {
              "type": "RadioGroupView",
              "value": {
                "no": {
                  "value": "No",
                  "visible": true
                }
              },
              "visible": true
            }
        }
    """.trimIndent()
}