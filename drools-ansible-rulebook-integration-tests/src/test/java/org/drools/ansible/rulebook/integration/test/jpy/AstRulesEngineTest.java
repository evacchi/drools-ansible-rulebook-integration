package org.drools.ansible.rulebook.integration.test.jpy;

import com.fasterxml.jackson.core.type.TypeReference;
import org.drools.ansible.rulebook.integration.api.JsonTest;
import org.drools.ansible.rulebook.integration.api.RulesExecutor;
import org.drools.ansible.rulebook.integration.core.jpy.AstRulesEngine;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.drools.ansible.rulebook.integration.api.io.JsonMapper.readValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;


public class AstRulesEngineTest {
    @Test
    public void testJpyApi() {

        String rules = JsonTest.JSON1;

        AstRulesEngine engine = new AstRulesEngine();
        long id = engine.createRuleset(rules);

        String result = engine.assertFact(id, "{ \"sensu\": { \"data\": { \"i\":1 } } }");

        assertNotNull(result);
    }

    @Test
    public void testBrokenApi() throws IOException {
        try (InputStream s = getClass().getClassLoader().getResourceAsStream("broken.json")) {
            String rules = new String(s.readAllBytes());

            AstRulesEngine engine = new AstRulesEngine();
            assertThrows(UnsupportedOperationException.class, () -> engine.createRuleset(rules));
        }
    }

    @Test
    public void testRetractFact() throws IOException {
        try (InputStream s = getClass().getClassLoader().getResourceAsStream("retract_fact.json")) {
            String rules = new String(s.readAllBytes());

            AstRulesEngine engine = new AstRulesEngine();
            long id = engine.createRuleset(rules);
            engine.assertFact(id, "{\"j\": 42}");
            engine.assertFact(id, "{\"i\": 67}");
            String retractedFact = "{\"i\": 67}";
            String r = engine.retractFact(id, retractedFact);

            List<Map<String, Map>> v = readValue(r);

            assertEquals(v.get(0).get("r_0").get("m"), new JSONObject(retractedFact).toMap());
        }
    }

}