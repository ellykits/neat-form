package com.nerdstone.neatformcore.junit;

import com.nerdstone.neatformcore.domain.model.NForm;
import com.nerdstone.neatformcore.domain.model.NFormContent;
import com.nerdstone.neatformcore.domain.model.NFormViewProperty;
import com.nerdstone.neatformcore.form.json.JsonFormParser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FormJsonParserTest {

    private String json;

    @Before
    public void setUp() {
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
                "               \"validation\": [\n" +
                "                  \"{username}.length() < 8\",\n" +
                "                  \"!{username}.contains(@)\"\n" +
                "               ],\n" +
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
                "               ],\n" +
                "               \"validation\": [\n" +
                "                  \"{username}.length() < 8\",\n" +
                "                  \"!{username}.contains(@)\"\n" +
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
                "               ],\n" +
                "               \"validation\": [\n" +
                "                  \"{username}.length() < 8\",\n" +
                "                  \"!{username}.contains(@)\"\n" +
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
                "               ],\n" +
                "               \"validation\": [\n" +
                "                  \"{username}.length() < 8\",\n" +
                "                  \"!{username}.contains(@)\"\n" +
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
                "               ],\n" +
                "               \"validation\": [\n" +
                "                  \"{username}.length() < 8\",\n" +
                "                  \"!{username}.contains(@)\"\n" +
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
                "}";
    }

    @Test
    public void testJsonParserWithValidJson() {
        NForm nForm = JsonFormParser.parseJson(json);
        assertEquals(nForm.getFormName(), "Profile");
        assertEquals(nForm.getCount(), 5);
        assertEquals(nForm.getSteps().size(), 5);
        assertNotNull(nForm);
    }

    @Test
    public void testThatNFormContentIsCorrectlyParsed() {
        NForm nForm = JsonFormParser.parseJson(json);
        NFormContent nFormContent = nForm.getSteps().get(0);
        assertEquals(nFormContent.getStepName(), "Behaviour and counselling");
        assertEquals(nFormContent.getStepNumber(), 1);
        assertEquals(nFormContent.getFields().size(), 2);
    }

    @Test
    public void testThatNFormViewPropertyIsCorrectlyParsed() {
        NForm nForm = JsonFormParser.parseJson(json);
        NFormViewProperty property = nForm.getSteps().get(0).getFields().get(0);
        assertEquals(property.getName(), "username");
        assertEquals(property.getRequiredStatus(), "yes -> Please add username");
        assertEquals(property.getType(), "edit_text");
    }

    @Test
    public void testThatNFormRuleIsCorrectlyParsed() {
        NForm nForm = JsonFormParser.parseJson(json);
        NFormViewProperty property = nForm.getSteps().get(0).getFields().get(0);
        assertEquals(property.getViewRules().size(), 1);
        assertEquals(property.getViewRules().get(0).getAction(), "HIDE");
        assertEquals(property.getViewRules().get(0).getCondition(), "{age} > 30");
    }

    @Test
    public void testThatNFormSubViewPropertyCorrectlyParsed() {
        NForm nForm = JsonFormParser.parseJson(json);
        NFormViewProperty property = nForm.getSteps().get(4).getFields().get(1);
        assertEquals(property.getOptions().size(), 2);
        assertEquals(property.getOptions().get(0).getName(), "female");
        assertEquals(property.getOptions().get(0).getLabel(), "Female");
        assertEquals(property.getOptions().get(1).getName(), "male");
        assertEquals(property.getOptions().get(1).getLabel(), "Male");
        assertEquals(property.getOptions().get(0).getViewMetadata().size(), 3);
        assertEquals(property.getOptions().get(1).getViewMetadata().size(), 3);
    }

    @Test
    public void testJsonParserWithNullAndEmptyJson() {
        assertNull(JsonFormParser.parseJson(null));
        assertNull(JsonFormParser.parseJson(""));
    }
}
