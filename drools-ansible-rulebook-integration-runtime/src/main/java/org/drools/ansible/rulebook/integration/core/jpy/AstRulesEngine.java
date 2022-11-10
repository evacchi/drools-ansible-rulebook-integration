package org.drools.ansible.rulebook.integration.core.jpy;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.drools.ansible.rulebook.integration.api.RuleFormat;
import org.drools.ansible.rulebook.integration.api.RuleNotation;
import org.drools.ansible.rulebook.integration.api.RulesExecutor;
import org.json.JSONObject;

import java.util.Map;

public class AstRulesEngine {

    AstRulesEngineInternal internal = new AstRulesEngineInternal();

    public long createRuleset(String rulesetString) {
        return internal.createRuleset(RuleNotation.CoreNotation.INSTANCE.toRulesSet(RuleFormat.JSON, rulesetString));
    }

    public void dispose(long sessionId) {
        internal.dispose(sessionId);
    }

    /**
     * @return error code (currently always 0)
     */
    public String retractFact(long sessionId, String serializedFact) {
        Map<String, Object> fact = new JSONObject(serializedFact).toMap();
        return toJson(internal.retractFact(sessionId, fact));
    }

    public String assertFact(long sessionId, String serializedFact) {
        Map<String, Object> fact = new JSONObject(serializedFact).toMap();
        return toJson(internal.assertEvent(sessionId, fact));
    }

    public String assertEvent(long sessionId, String serializedFact) {
        Map<String, Object> fact = new JSONObject(serializedFact).toMap();
        return toJson(internal.assertEvent(sessionId, fact));
    }

    public String getFacts(long session_id) {
        return toJson(internal.getFacts(session_id));
    }

    private String toJson(Object elem) {
        try {
            return RulesExecutor.OBJECT_MAPPER.writeValueAsString(elem);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
