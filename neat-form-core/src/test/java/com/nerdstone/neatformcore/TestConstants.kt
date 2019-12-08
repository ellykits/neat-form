package com.nerdstone.neatformcore

object TestConstants {
    private const val SAMPLE_DIR = "sample/"
    const val SAMPLE_ONE_FORM_FILE = SAMPLE_DIR + "sample_one_form.json"
    const val SAMPLE_TWO_FORM_FILE = SAMPLE_DIR + "sample_two_form.json"
    const val SAMPLE_JSON =
        """{
              "form": "Profile",
              "rules_file": "rules/yml/sample_one_form_rules.yml",
              "steps": [
                {
                  "title": "Demographics",
                  "fields": [
                    {
                      "name": "adult",
                      "type": "text_input_edit_text",
                      "properties": {
                        "hint": "Enter adult's phone number",
                        "type": "name",
                        "padding": "8"
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
                      "subjects": "age:number, child:text, dob:number",
                      "required_status": "Yes:please add username"
                    },
                    {
                      "name": "age",
                      "type": "text_input_edit_text",
                      "properties": {
                        "hint": "Enter your age",
                        "type": "name",
                        "padding": "8"
                      },
                      "meta_data": {
                        "openmrs_entity": "",
                        "openmrs_entity_id": "",
                        "openmrs_entity_parent": ""
                      },
                      "validation": [
                        {
                          "condition": "value.matches(\"^\\\\d{1,2}?${'$'}\")",
                          "message": "Not a valid age"
                        }
                      ],
                      "required_status": "yes:Please add age"
                    },
                    {
                      "name": "child",
                      "type": "text_input_edit_text",
                      "properties": {
                        "hint": "I am a child",
                        "type": "name",
                        "padding": "8"
                      },
                      "meta_data": {
                        "openmrs_entity": "",
                        "openmrs_entity_id": "",
                        "openmrs_entity_parent": ""
                      },
                      "required_status": "yes:Please add age",
                      "subjects": "age:number, adult:text, email_subscription:map"
                    },
                    {
                      "name": "email_subscription",
                      "type": "checkbox",
                      "properties": {
                        "text": "Subscribe to email notifications"
                      },
                      "required_status": "yes:Please specify if you want subscription",
                      "subjects": "age:number"
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
                      "subjects": "email_subscription:map",
                      "required_status": "yes:Please specify your gender"
                    },
                    {
                      "name": "country",
                      "type": "spinner",
                      "properties": {
                        "text": "Choose your country of Origin",
                        "searchable": "Search country"
                      },
                      "options": [
                        {
                          "name": "none",
                          "text": "Select country"
                        },
                        {
                          "name": "america",
                          "text": "United States Of America",
                          "meta_data": {
                            "country_code": "+1"
                          }
                        },
                        {
                          "name": "canada",
                          "text": "Canada",
                          "meta_data": {
                            "country_code": "+1"
                          }
                        },
                        {
                          "name": "uk",
                          "text": "United Kingdom",
                          "meta_data": {
                            "country_code": "+44"
                          }
                        },
                        {
                          "name": "australia",
                          "text": "Australia",
                          "meta_data": {
                            "country_code": "+61"
                          }
                        },
                        {
                          "name": "Kenya",
                          "text": "Kenya",
                          "meta_data": {
                            "country_code": "+254"
                          }
                        },
                        {
                          "name": "tanzania",
                          "text": "Tanzania",
                          "meta_data": {
                            "country_code": "+255"
                          }
                        },
                        {
                          "name": "china",
                          "text": "China",
                          "meta_data": {
                            "country_code": "+86"
                          }
                        },
                        {
                          "name": "sweden",
                          "text": "Sweden",
                          "meta_data": {
                            "country_code": "+1"
                          }
                        },
                        {
                          "name": "russia",
                          "text": "Russia",
                          "meta_data": {
                            "country_code": "+7"
                          }
                        },
                        {
                          "name": "japan",
                          "text": "Japan",
                          "meta_data": {
                            "country_code": "+81"
                          }
                        },
                        {
                          "name": "singapore",
                          "text": "Singapore",
                          "meta_data": {
                            "country_code": "+65"
                          }
                        }
                      ],
                      "subjects": "email_subscription:map",
                      "required_status": "yes:Please specify your country"
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
                        },
                        {
                          "name": "kisw",
                          "text": "Kiswahili",
                          "meta_data": {
                            "openmrs_entity": "",
                            "openmrs_entity_id": "A123123123123",
                            "openmrs_entity_parent": ""
                          }
                        },
                        {
                          "name": "need_help",
                          "text": "Help me choose",
                          "is_exclusive": true,
                          "meta_data": {
                            "openmrs_entity": "",
                            "openmrs_entity_id": "A918928912",
                            "openmrs_entity_parent": ""
                          }
                        },
                        {
                          "name": "none",
                          "text": "None of the above",
                          "is_exclusive": true,
                          "meta_data": {
                            "openmrs_entity": "",
                            "openmrs_entity_id": "A123123123123",
                            "openmrs_entity_parent": ""
                          }
                        }
                      ],
                      "validation": [
                        {
                          "condition": "value['need_help'] == null",
                          "message": "You should be speaking at least one of the languages man!"
                        }
                      ],
                      "required_status": "yes:Please specify your languages",
                      "subjects": "email_subscription:map, gender:text"
                    },
                    {
                      "name": "language_none",
                      "type": "edit_text",
                      "properties": {
                        "hint": "Specify your language",
                        "type": "name"
                      },
                      "meta_data": {
                        "openmrs_entity": "",
                        "openmrs_entity_id": "",
                        "openmrs_entity_parent": ""
                      },
                      "required_status": "true:Please specify language",
                      "subjects": "choose_language:map"
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
                          "name": "no",
                          "text": "No"
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
                      "required_status": "yes:Wiki contribution field is required.",
                      "subjects": "email_subscription:map, gender:text"
                    },
                    {
                      "name": "wiki_contribution_yes",
                      "type": "edit_text",
                      "properties": {
                        "hint": "What was your contribution?",
                        "type": "name"
                      },
                      "validation": [
                        {
                          "condition": "value.startsWith('I')",
                          "message": "Your statement must start with I"
                        }
                      ],
                      "required_status": "true:Please specify your contributions",
                      "subjects": "wiki_contribution:map"
                    },
                    {
                      "name": "dob",
                      "type": "datetime_picker",
                      "properties": {
                        "hint": "Enter birth date",
                        "type": "date_picker",
                        "display_format": "dd/MM/YYYY"
                      },
                      "required_status": "true:Please specify your dob"
                    },
                    {
                      "name": "time",
                      "type": "datetime_picker",
                      "properties": {
                        "hint": "Enter time you clocked in",
                        "type": "time_picker",
                        "display_format": "hh:m a"
                      },
                      "required_status": "true:Please specify the time you clocked in"
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
                      "subjects": "email_subscription:map, gender:text",
                      "required_status": "no:Please specify the time you clocked in"
                    },
                    {
                      "name": "delivery_methods",
                      "type": "multi_choice_checkbox",
                      "properties": {
                        "text": "Previous delivery methods."
                      },
                      "options": [
                        {
                          "name": "vaginal",
                          "text": "Vaginal delivery",
                          "meta_data": {
                            "openmrs_entity": "",
                            "openmrs_entity_id": "A12312332123123",
                            "openmrs_entity_parent": ""
                          }
                        },
                        {
                          "name": "c-section",
                          "text": "C-Section",
                          "meta_data": {
                            "openmrs_entity": "",
                            "openmrs_entity_id": "A12123123123123",
                            "openmrs_entity_parent": ""
                          }
                        }
                      ],
                      "subjects": "no_prev_pregnancies:number"
                    }
                  ]
                }
              ]
            }
    """

}
