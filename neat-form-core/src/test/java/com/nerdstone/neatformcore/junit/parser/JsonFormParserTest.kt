package com.nerdstone.neatformcore.junit.parser

import com.nerdstone.neatformcore.form.json.JsonFormParser
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class `Test passing JSON files` {

    private var json: String? = null

    @Before
    fun `Before everything else`() {
        json = "{\n" +
                "   \"form\": \"Profile\",\n" +
                "   \"steps\": [\n" +
                "      {\n" +
                "         \"title\": \"Behaviour and counselling\",\n" +
                "         \"fields\": [\n" +
                "            {\n" +
                "               \"name\": \"username\",\n" +
                "               \"type\": \"edit_text\",\n" +
                "               \"properties\": {\n" +
                "                  \"hint\": \"Blood type test\",\n" +
                "                  \"type\": \"name\",\n" +
                "                  \"text_size\": 18.4\n" +
                "               },\n" +
                "               \"meta_data\": {\n" +
                "                  \"openmrs_entity\": \"\",\n" +
                "                  \"openmrs_entity_id\": \"\",\n" +
                "                  \"openmrs_entity_parent\": \"\"\n" +
                "               },\n" +
                "               \"rule\": [\n" +
                "                  {\n" +
                "                     \"action\": \"HIDE\",\n" +
                "                     \"condition\": \"{age} > 30\"\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"validation\": [\n" +
                "                   {\n" +
                "                       \"validation_name\": \"length\",\n" +
                "                       \"condition\": \"value.length() < 11\",\n" +
                "                       \"error_message\": \"value should be less than ten digits\"\n" +
                "                   },\n" +
                "                   {\n" +
                "                       \"validation_name\": \"phone number\",\n" +
                "                       \"condition\": \"value.matches(\\\"\\\\\\\\d{10}\\\")\",\n" +
                "                       \"error_message\": \"Not a valid phone number\"\n" +
                "                   }\n" +
                "               ],\n" +
                "                \"subjects\": \"age, medications\",\n" +
                "               \"required_status\": \"yes -> Please add username\" " +
                "            },\n" +
                "            {\n" +
                "               \"name\": \"age\",\n" +
                "               \"type\": \"calculation\",\n" +
                "               \"value\": \"{age} > 18 ? Adult : Child \"\n" +
                "            }\n" +
                "         ]\n" +
                "      },\n" +
                "      {\n" +
                "         \"title\": \"Woman Behavior\",\n" +
                "         \"fields\": [\n" +
                "            {\n" +
                "               \"name\": \"username\",\n" +
                "               \"type\": \"edit_text\",\n" +
                "               \"properties\": {\n" +
                "                  \"hint\": \"Blood type test\",\n" +
                "                  \"type\": \"name\",\n" +
                "                  \"text_size\": 18.4\n" +
                "               },\n" +
                "               \"meta_data\": {\n" +
                "                  \"openmrs_entity\": \"\",\n" +
                "                  \"openmrs_entity_id\": \"\",\n" +
                "                  \"openmrs_entity_parent\": \"\"\n" +
                "               },\n" +
                "               \"rule\": [\n" +
                "                  {\n" +
                "                     \"action\": \"HIDE\",\n" +
                "                     \"condition\": \"{age} > 30\"\n" +
                "                  }\n" +
                "               ]\n" +
                "            },\n" +
                "            {\n" +
                "               \"name\": \"age\",\n" +
                "               \"type\": \"calculation\",\n" +
                "               \"value\": \"{age} > 18 ? Adult : Child \"\n" +
                "            }\n" +
                "         ]\n" +
                "      },\n" +
                "      {\n" +
                "         \"title\": \"Physical Exam\",\n" +
                "         \"fields\": [\n" +
                "            {\n" +
                "               \"name\": \"username\",\n" +
                "               \"type\": \"edit_text\",\n" +
                "               \"properties\": {\n" +
                "                  \"hint\": \"Blood type test\",\n" +
                "                  \"type\": \"name\",\n" +
                "                  \"text_size\": 18.4\n" +
                "               },\n" +
                "               \"meta_data\": {\n" +
                "                  \"openmrs_entity\": \"\",\n" +
                "                  \"openmrs_entity_id\": \"\",\n" +
                "                  \"openmrs_entity_parent\": \"\"\n" +
                "               },\n" +
                "               \"rule\": [\n" +
                "                  {\n" +
                "                     \"action\": \"HIDE\",\n" +
                "                     \"condition\": \"{age} > 30\"\n" +
                "                  }\n" +
                "               ]\n" +
                "            },\n" +
                "            {\n" +
                "               \"name\": \"age\",\n" +
                "               \"type\": \"calculation\",\n" +
                "               \"value\": \"{age} > 18 ? Adult : Child \"\n" +
                "            }\n" +
                "         ]\n" +
                "      },\n" +
                "      {\n" +
                "         \"title\": \"Testing\",\n" +
                "         \"fields\": [\n" +
                "            {\n" +
                "               \"name\": \"username\",\n" +
                "               \"type\": \"edit_text\",\n" +
                "               \"properties\": {\n" +
                "                  \"hint\": \"Blood type test\",\n" +
                "                  \"type\": \"name\",\n" +
                "                  \"text_size\": 18.4\n" +
                "               },\n" +
                "               \"meta_data\": {\n" +
                "                  \"openmrs_entity\": \"\",\n" +
                "                  \"openmrs_entity_id\": \"\",\n" +
                "                  \"openmrs_entity_parent\": \"\"\n" +
                "               },\n" +
                "               \"rule\": [\n" +
                "                  {\n" +
                "                     \"action\": \"HIDE\",\n" +
                "                     \"condition\": \"{age} > 30\"\n" +
                "                  }\n" +
                "               ]\n" +
                "            },\n" +
                "            {\n" +
                "               \"name\": \"age\",\n" +
                "               \"type\": \"calculation\",\n" +
                "               \"value\": \"{age} > 18 ? Adult : Child \"\n" +
                "            }\n" +
                "         ]\n" +
                "      },\n" +
                "      {\n" +
                "         \"title\": \"Symptoms and Followup\",\n" +
                "         \"fields\": [\n" +
                "            {\n" +
                "               \"name\": \"username\",\n" +
                "               \"type\": \"edit_text\",\n" +
                "               \"properties\": {\n" +
                "                  \"hint\": \"Blood type test\",\n" +
                "                  \"type\": \"name\",\n" +
                "                  \"text_size\": 18.4\n" +
                "               },\n" +
                "               \"meta_data\": {\n" +
                "                  \"openmrs_entity\": \"\",\n" +
                "                  \"openmrs_entity_id\": \"\",\n" +
                "                  \"openmrs_entity_parent\": \"\"\n" +
                "               },\n" +
                "               \"rule\": [\n" +
                "                  {\n" +
                "                     \"action\": \"HIDE\",\n" +
                "                     \"condition\": \"{age} > 30\"\n" +
                "                  }\n" +
                "               ]\n" +
                "            },\n" +
                "{\n" +
                "   \"name\": \"gender\",\n" +
                "   \"type\": \"multi_choice_checkbox\",\n" +
                "   \"properties\": {\n" +
                "      \"text\": \"Select your gender\"\n" +
                "   },\n" +
                "   \"meta_data\": {\n" +
                "      \"openmrs_entity\": \"\",\n" +
                "      \"openmrs_entity_id\": \"\",\n" +
                "      \"openmrs_entity_parent\": \"\"\n" +
                "   },\n" +
                "   \"options\": [\n" +
                "      {\n" +
                "         \"name\": \"female\",\n" +
                "         \"text\": \"Female\",\n" +
                "         \"is_exclusive\": false,\n" +
                "         \"metadata\": {\n" +
                "            \"openmrs_entity\": \"120192AAAAAAAAAAAAAA\",\n" +
                "            \"openmrs_entity_id\": \"\",\n" +
                "            \"openmrs_entity_parent\": \"\"\n" +
                "         }\n" +
                "      },\n" +
                "      {\n" +
                "         \"name\": \"male\",\n" +
                "         \"text\": \"Male\",\n" +
                "         \"is_exclusive\": false,\n" +
                "         \"metadata\": {\n" +
                "            \"openmrs_entity\": \"120191AAAAAAAAAAAAAA\",\n" +
                "            \"openmrs_entity_id\": \"\",\n" +
                "            \"openmrs_entity_parent\": \"\"\n" +
                "         }\n" +
                "      }\n" +
                "   ]\n" +
                "}" +
                "            ,{\n" +
                "               \"name\": \"age\",\n" +
                "               \"type\": \"calculation\",\n" +
                "               \"value\": \"{age} > 18 ? Adult : Child \"\n" +
                "            }\n" +
                "         ]\n" +
                "      }\n" +
                "   ]\n" +
                "}"
    }

    @Test
    fun `Should parse a valid JSON into NForm model`() {
        val nForm = JsonFormParser.parseJson(json)
        assertEquals(nForm!!.formName, "Profile")
        assertEquals(nForm.steps.size.toLong(), 5)
        assertNotNull(nForm)
    }

    @Test
    fun `Should correctly parse form content`() {
        val nForm = JsonFormParser.parseJson(json)
        val nFormContent = nForm!!.steps[0]
        assertEquals(nFormContent.stepName, "Behaviour and counselling")
        assertEquals(nFormContent.fields.size.toLong(), 2)
    }

    @Test
    fun `Should properly parse view properties`() {
        val nForm = JsonFormParser.parseJson(json)
        val property = nForm!!.steps[0].fields[0]
        assertEquals(property.name, "username")
        assertEquals(2, property.validations?.size)
        assertTrue(property.subjects!!.contains("age"))
        assertEquals(property.requiredStatus, "yes -> Please add username")
        assertEquals(property.type, "edit_text")
    }

    @Test
    fun `Should properly parse sub view properties`() {
        val nForm = JsonFormParser.parseJson(json)
        val property = nForm!!.steps[4].fields[1]
        assertEquals(property.options!!.size.toLong(), 2)
        assertEquals(property.options!![0].name, "female")
        assertEquals(property.options!![0].text, "Female")
        assertEquals(property.options!![1].name, "male")
        assertEquals(property.options!![1].text, "Male")
        assertEquals(property.options!![0].viewMetadata!!.size.toLong(), 3)
        assertEquals(property.options!![1].viewMetadata!!.size.toLong(), 3)
    }

    @Test
    fun `Should not parse empty JSON or null`() {
        assertNull(JsonFormParser.parseJson(null))
        assertNull(JsonFormParser.parseJson(""))
    }
}
