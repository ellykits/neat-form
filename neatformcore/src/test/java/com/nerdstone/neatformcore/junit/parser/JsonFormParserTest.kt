package com.nerdstone.neatformcore.junit.parser

import com.nerdstone.neatformcore.form.json.JsonFormParser
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class JsonFormParserTest {

    private var json: String? = null

    @Before
    fun setUp() {
        json = "{\n" +
                "   \"form\": \"Profile\",\n" +
                "   \"is_multi_step\": true,\n" +
                "   \"count\": 5,\n" +
                "   \"steps\": [\n" +
                "      {\n" +
                "         \"title\": \"Behaviour and counselling\",\n" +
                "         \"step_number\": 1,\n" +
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
                "               \"validation\": \"{username}.length() < 8\",\n" +
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
                "         \"step_number\": 2,\n" +
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
                "         \"step_number\": 3,\n" +
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
                "         \"step_number\": 4,\n" +
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
                "         \"step_number\": 5,\n" +
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
                "      \"label\": \"Select your gender\"\n" +
                "   },\n" +
                "   \"meta_data\": {\n" +
                "      \"openmrs_entity\": \"\",\n" +
                "      \"openmrs_entity_id\": \"\",\n" +
                "      \"openmrs_entity_parent\": \"\"\n" +
                "   },\n" +
                "   \"options\": [\n" +
                "      {\n" +
                "         \"name\": \"female\",\n" +
                "         \"label\": \"Female\",\n" +
                "         \"is_exclusive\": false,\n" +
                "         \"metadata\": {\n" +
                "            \"openmrs_entity\": \"120192AAAAAAAAAAAAAA\",\n" +
                "            \"openmrs_entity_id\": \"\",\n" +
                "            \"openmrs_entity_parent\": \"\"\n" +
                "         }\n" +
                "      },\n" +
                "      {\n" +
                "         \"name\": \"male\",\n" +
                "         \"label\": \"Male\",\n" +
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
    fun testJsonParserWithValidJson() {
        val nForm = JsonFormParser.parseJson(json)
        assertEquals(nForm!!.formName, "Profile")
        assertEquals(nForm.count.toLong(), 5)
        assertEquals(nForm.steps!!.size.toLong(), 5)
        assertNotNull(nForm)
    }

    @Test
    fun testThatNFormContentIsCorrectlyParsed() {
        val nForm = JsonFormParser.parseJson(json)
        val nFormContent = nForm!!.steps[0]
        assertEquals(nFormContent.stepName, "Behaviour and counselling")
        assertEquals(nFormContent.stepNumber.toLong(), 1)
        assertEquals(nFormContent.fields.size.toLong(), 2)
    }

    @Test
    fun testThatNFormViewPropertyIsCorrectlyParsed() {
        val nForm = JsonFormParser.parseJson(json)
        val property = nForm!!.steps[0].fields[0]
        assertEquals(property.name, "username")
        assertEquals("{username}.length() < 8", property.validations)
        assertTrue(property.subjects!!.contains("age"))
        assertEquals(property.requiredStatus, "yes -> Please add username")
        assertEquals(property.type, "edit_text")
    }

    @Test
    fun testThatNFormSubViewPropertyCorrectlyParsed() {
        val nForm = JsonFormParser.parseJson(json)
        val property = nForm!!.steps[4].fields[1]
        assertEquals(property.options!!.size.toLong(), 2)
        assertEquals(property.options!![0].name, "female")
        assertEquals(property.options!![0].label, "Female")
        assertEquals(property.options!![1].name, "male")
        assertEquals(property.options!![1].label, "Male")
        assertEquals(property.options!![0].viewMetadata!!.size.toLong(), 3)
        assertEquals(property.options!![1].viewMetadata!!.size.toLong(), 3)
    }

    @Test
    fun testJsonParserWithNullAndEmptyJson() {
        assertNull(JsonFormParser.parseJson(null))
        assertNull(JsonFormParser.parseJson(""))
    }
}
