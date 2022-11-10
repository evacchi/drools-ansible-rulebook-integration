package org.drools.ansible.rulebook.integration.test.jpy;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.drools.ansible.rulebook.integration.core.jpy.AstRulesEngineCommand;
import org.drools.ansible.rulebook.integration.core.jpy.AstRulesEngineCommand.*;
import org.junit.Test;

import java.util.Map;

import static org.drools.ansible.rulebook.integration.api.RulesExecutor.OBJECT_MAPPER;
import static org.junit.Assert.assertEquals;

public class AstRulesEngineCommandTest {

    @Test
    public void testCreateRulesetDeSerialization() throws JsonProcessingException {
        AstRulesEngineCommand createRuleset = new CreateRuleset(
                Map.of("some", "value"));
        String result = OBJECT_MAPPER.writeValueAsString(createRuleset);
        String expected = "{\"command\":\"create-ruleset\",\"ruleset\":{\"some\":\"value\"}}";
        assertEquals(expected, result);

        AstRulesEngineCommand deserialized = OBJECT_MAPPER.readValue(expected, CreateRuleset.class);

        assertEquals(createRuleset, deserialized);
    }

    @Test
    public void testAssertEventDeSerialization() throws JsonProcessingException {
        AstRulesEngineCommand assertEvent = new AssertEvent(10,
                Map.of("some", "value"));
        String result = OBJECT_MAPPER.writeValueAsString(assertEvent);
        String expected = "{\"command\":\"assert-event\",\"session-id\":10,\"event\":{\"some\":\"value\"}}";
        assertEquals(expected, result);

        AstRulesEngineCommand deserialized = OBJECT_MAPPER.readValue(expected, AstRulesEngineCommand.class);

        assertEquals(assertEvent, deserialized);
    }

    @Test
    public void testAssertFactDeSerialization() throws JsonProcessingException {
        AstRulesEngineCommand assertFact = new AssertFact(10,
                Map.of("some", "value"));
        String result = OBJECT_MAPPER.writeValueAsString(assertFact);
        String expected = "{\"command\":\"assert-fact\",\"session-id\":10,\"fact\":{\"some\":\"value\"}}";
        assertEquals(expected, result);

        AstRulesEngineCommand deserialized = OBJECT_MAPPER.readValue(expected, AstRulesEngineCommand.class);

        assertEquals(assertFact, deserialized);
    }

    @Test
    public void testRetractFactDeSerialization() throws JsonProcessingException {
        AstRulesEngineCommand retractFact = new RetractFact(10,
                Map.of("some", "value"));
        String result = OBJECT_MAPPER.writeValueAsString(retractFact);
        String expected = "{\"command\":\"retract-fact\",\"session-id\":10,\"fact\":{\"some\":\"value\"}}";
        assertEquals(expected, result);

        AstRulesEngineCommand deserialized = OBJECT_MAPPER.readValue(expected, AstRulesEngineCommand.class);

        assertEquals(retractFact, deserialized);
    }

    @Test
    public void testGetFactsDeSerialization() throws JsonProcessingException {
        AstRulesEngineCommand getFacts = new GetFacts(10);
        String result = OBJECT_MAPPER.writeValueAsString(getFacts);
        String expected = "{\"command\":\"get-facts\",\"session-id\":10}";
        assertEquals(expected, result);

        AstRulesEngineCommand deserialized = OBJECT_MAPPER.readValue(expected, AstRulesEngineCommand.class);

        assertEquals(getFacts, deserialized);
    }


    @Test
    public void testDisposeDeSerialization() throws JsonProcessingException {
        AstRulesEngineCommand dispose = new Dispose(10);
        String result = OBJECT_MAPPER.writeValueAsString(dispose);
        String expected = "{\"command\":\"dispose\",\"session-id\":10}";
        assertEquals(expected, result);

        AstRulesEngineCommand deserialized = OBJECT_MAPPER.readValue(expected, AstRulesEngineCommand.class);

        assertEquals(dispose, deserialized);
    }

}
