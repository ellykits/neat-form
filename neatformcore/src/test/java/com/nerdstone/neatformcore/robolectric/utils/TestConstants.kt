package com.nerdstone.neatformcore.robolectric.utils

object TestConstants {
    private const val SAMPLE_DIR = "sample/"
    const val SAMPLE_ONE_FORM_FILE = SAMPLE_DIR + "sample_one_form.json"
    const val SAMPLE_JSON =
        "{\n  \"form\": \"Profile\",\n  \"is_multi_step\": true,\n  \"rules_file\": \"rules/yml/sample_one_form_rules.yml\",\n " +
                " \"count\": 1,\n  \"steps\": [\n    {\n      \"title\": \"Test and counselling\",\n      \"step_number\": 1,\n   " +
                "   \"fields\": [\n        {\n          \"name\": \"adult\",\n          \"type\": \"edit_text\",\n     " +
                "     \"properties\": {\n            \"hint\": \"This is an adult\",\n            \"type\": \"name\",\n     " +
                "       \"text_size\": \"20\"\n          },\n          \"meta_data\": {\n            \"openmrs_entity\": \"\",\n        " +
                "    \"openmrs_entity_id\": \"\",\n            \"openmrs_entity_parent\": \"\"\n          },\n        " +
                "  \"validation\": \"length() < 8; !contains(@)\",\n          \"subjects\": \"age:number, child:text\",\n       " +
                "   \"required_status\": \"True:please add username\"\n        },\n        {\n          \"name\": \"age\",\n    " +
                "      \"type\": \"edit_text\",\n          \"properties\": {\n            \"hint\": \"Enter your age (will be multiplied by 10)\",\n    " +
                "        \"type\": \"name\",\n            \"text\": \"0\",\n            \"text_size\": \"18.6\",\n         " +
                "   \"padding\": \"20\"\n          },\n          \"meta_data\": {\n            \"openmrs_entity\": \"\",\n          " +
                "  \"openmrs_entity_id\": \"\",\n            \"openmrs_entity_parent\": \"\"\n          },\n      " +
                "    \"required_status\": \"yes:Please add age\"\n        },\n        {\n          \"name\": \"child\",\n  " +
                "        \"type\": \"edit_text\",\n          \"properties\": {\n            \"hint\": \"I am a child\",\n       " +
                "     \"type\": \"name\",\n            \"text_size\": \"20\"\n          },\n          \"meta_data\": {\n   " +
                "         \"openmrs_entity\": \"\",\n            \"openmrs_entity_id\": \"\",\n            \"openmrs_entity_parent\": \"\"\n    " +
                "      },\n          \"required_status\": \"yes:Please add age\",\n          \"subjects\": \"age\"\n        }\n      ]\n    }\n  ]\n}"
}
