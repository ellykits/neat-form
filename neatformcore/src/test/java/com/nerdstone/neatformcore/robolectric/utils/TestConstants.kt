package com.nerdstone.neatformcore.robolectric.utils

object TestConstants {
    private const val SAMPLE_DIR = "sample/"
    const val SAMPLE_ONE_FORM_FILE = SAMPLE_DIR + "sample_one_form.json"
    const val SAMPLE_JSON = "{\n" +
            "  \"form\": \"Profile\",\n" +
            "  \"is_multi_step\": true,\n" +
            "  \"rules_file\": \"rules/yml/sample_one_form_rules.yml\",\n" +
            "  \"count\": 1,\n" +
            "  \"steps\": [\n" +
            "    {\n" +
            "      \"title\": \"Test and counselling\",\n" +
            "      \"step_number\": 1,\n" +
            "      \"fields\": [\n" +
            "        {\n" +
            "          \"name\": \"adult\",\n" +
            "          \"type\": \"edit_text\",\n" +
            "          \"properties\": {\n" +
            "            \"hint\": \"This is an adult\",\n" +
            "            \"type\": \"name\",\n" +
            "            \"text\": \"Catch me if you can\"\n" +
            "          },\n" +
            "          \"meta_data\": {\n" +
            "            \"openmrs_entity\": \"\",\n" +
            "            \"openmrs_entity_id\": \"\",\n" +
            "            \"openmrs_entity_parent\": \"\"\n" +
            "          },\n" +
            "          \"validation\": \"length() < 8; !contains(@)\",\n" +
            "          \"subjects\": \"age:number, child:text\",\n" +
            "          \"required_status\": \"True:please add username\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"age\",\n" +
            "          \"type\": \"edit_text\",\n" +
            "          \"properties\": {\n" +
            "            \"hint\": \"Enter your age (will be multiplied by 10)\",\n" +
            "            \"type\": \"name\",\n" +
            "            \"padding\": \"8\"\n" +
            "          },\n" +
            "          \"meta_data\": {\n" +
            "            \"openmrs_entity\": \"\",\n" +
            "            \"openmrs_entity_id\": \"\",\n" +
            "            \"openmrs_entity_parent\": \"\"\n" +
            "          },\n" +
            "          \"required_status\": \"yes:Please add age\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"child\",\n" +
            "          \"type\": \"edit_text\",\n" +
            "          \"properties\": {\n" +
            "            \"hint\": \"I am a child\",\n" +
            "            \"type\": \"name\"\n" +
            "          },\n" +
            "          \"meta_data\": {\n" +
            "            \"openmrs_entity\": \"\",\n" +
            "            \"openmrs_entity_id\": \"\",\n" +
            "            \"openmrs_entity_parent\": \"\"\n" +
            "          },\n" +
            "          \"required_status\": \"yes:Please add age\",\n" +
            "          \"subjects\": \"age:number, adult:text, email_subscription:map\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"email_subscription\",\n" +
            "          \"type\": \"checkbox\",\n" +
            "          \"properties\": {\n" +
            "            \"text\": \"Subscribe to email notifications\"\n" +
            "          },\n" +
            "          \"required_status\": \"yes:Please specify if you want subscription\",\n" +
            "          \"subjects\": \"age:number\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"gender\",\n" +
            "          \"type\": \"spinner\",\n" +
            "          \"properties\": {\n" +
            "            \"text\": \"Choose your gender\"\n" +
            "          },\n" +
            "          \"options\": [\n" +
            "            {\n" +
            "              \"name\": \"none\",\n" +
            "              \"text\": \"Select gender\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"female\",\n" +
            "              \"text\": \"Female\",\n" +
            "              \"meta_data\": {\n" +
            "                \"openmrs_entity\": \"\",\n" +
            "                \"openmrs_entity_id\": \"A123390123123\",\n" +
            "                \"openmrs_entity_parent\": \"\"\n" +
            "              }\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"male\",\n" +
            "              \"text\": \"Male\",\n" +
            "              \"meta_data\": {\n" +
            "                \"openmrs_entity\": \"\",\n" +
            "                \"openmrs_entity_id\": \"S8918313\",\n" +
            "                \"openmrs_entity_parent\": \"\"\n" +
            "              }\n" +
            "            }\n" +
            "          ],\n" +
            "          \"subjects\": \"email_subscription:map\",\n" +
            "          \"required_status\": \"yes:Please specify your gender\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"choose_language\",\n" +
            "          \"type\": \"multi_choice_checkbox\",\n" +
            "          \"properties\": {\n" +
            "            \"text\": \"Pick the languages you are proficient in.\"\n" +
            "          },\n" +
            "          \"options\": [\n" +
            "            {\n" +
            "              \"name\": \"eng\",\n" +
            "              \"text\": \"English\",\n" +
            "              \"meta_data\": {\n" +
            "                \"openmrs_entity\": \"\",\n" +
            "                \"openmrs_entity_id\": \"A123123123123\",\n" +
            "                \"openmrs_entity_parent\": \"\"\n" +
            "              }\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"french\",\n" +
            "              \"text\": \"French\",\n" +
            "              \"meta_data\": {\n" +
            "                \"openmrs_entity\": \"\",\n" +
            "                \"openmrs_entity_id\": \"A123123123123\",\n" +
            "                \"openmrs_entity_parent\": \"\"\n" +
            "              }\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"kisw\",\n" +
            "              \"text\": \"Kiswahili\",\n" +
            "              \"meta_data\": {\n" +
            "                \"openmrs_entity\": \"\",\n" +
            "                \"openmrs_entity_id\": \"A123123123123\",\n" +
            "                \"openmrs_entity_parent\": \"\"\n" +
            "              }\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"need_help\",\n" +
            "              \"text\": \"Help me choose\",\n" +
            "              \"is_exclusive\": true,\n" +
            "              \"meta_data\": {\n" +
            "                \"openmrs_entity\": \"\",\n" +
            "                \"openmrs_entity_id\": \"A918928912\",\n" +
            "                \"openmrs_entity_parent\": \"\"\n" +
            "              }\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"none\",\n" +
            "              \"text\": \"None of the above\",\n" +
            "              \"is_exclusive\": true,\n" +
            "              \"meta_data\": {\n" +
            "                \"openmrs_entity\": \"\",\n" +
            "                \"openmrs_entity_id\": \"A123123123123\",\n" +
            "                \"openmrs_entity_parent\": \"\"\n" +
            "              }\n" +
            "            }\n" +
            "          ],\n" +
            "          \"required_status\": \"yes:Please specify your languages\",\n" +
            "          \"subjects\": \"email_subscription:map, gender:text\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"language_none\",\n" +
            "          \"type\": \"edit_text\",\n" +
            "          \"properties\": {\n" +
            "            \"hint\": \"Specify your language\",\n" +
            "            \"type\": \"name\"\n" +
            "          },\n" +
            "          \"meta_data\": {\n" +
            "            \"openmrs_entity\": \"\",\n" +
            "            \"openmrs_entity_id\": \"\",\n" +
            "            \"openmrs_entity_parent\": \"\"\n" +
            "          },\n" +
            "          \"required_status\": \"true:Please specify language\",\n" +
            "          \"subjects\": \"choose_language:map\"\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}"
}
