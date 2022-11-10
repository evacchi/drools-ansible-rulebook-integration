package org.drools.ansible.rulebook.integration.core.jpy;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.drools.ansible.rulebook.integration.api.RulesExecutor;
import org.drools.ansible.rulebook.integration.core.jpy.AstRulesEngineCommand.*;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AstRulesEngineActor {
    private Queue<String> outgoing = new ConcurrentLinkedQueue<>();
    private Queue<AstRulesEngineCommand> incoming = new ConcurrentLinkedQueue<>();
    private AstRulesEngine astRulesEngine = new AstRulesEngine();

    public void tell(String serializedCommand) {
        try {
            AstRulesEngineCommand command =
                    RulesExecutor.OBJECT_MAPPER.readValue(serializedCommand, AstRulesEngineCommand.class);

            if (command instanceof CreateRuleset) {
                CreateRuleset createRuleset = (CreateRuleset) command;
                long sessionId =
                        astRulesEngine.createRuleset(
                                toJson(createRuleset.getRuleset()));
                outgoing.offer(String.valueOf(sessionId));
            } else if (command instanceof Dispose) {
                Dispose dispose = (Dispose) command;
                astRulesEngine.dispose(dispose.getSessionId());
            } else if (command instanceof RetractFact) {
                RetractFact rf = (RetractFact) command;
                String result = astRulesEngine.retractFact(rf.getSessionId(), toJson(rf.getFact()));
                outgoing.offer(result);
            } else if (command instanceof AssertFact) {
                AssertFact af = (AssertFact) command;
                String result = astRulesEngine.assertFact(af.getSessionId(), toJson(af.getFact()));
                outgoing.offer(result);
            } else if (command instanceof AssertEvent) {
                AssertEvent ae = (AssertEvent) command;
                String result = astRulesEngine.assertEvent(ae.getSessionId(), toJson(ae.getEvent()));
                outgoing.offer(result);
            } else if (command instanceof GetFacts) {
                GetFacts gf = (GetFacts) command;
                String facts = astRulesEngine.getFacts(gf.getSessionId());
                outgoing.offer(facts);
            } else {
                throw new UnsupportedOperationException(serializedCommand);
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String toJson(Map<String, Object> map) throws JsonProcessingException {
        return RulesExecutor.OBJECT_MAPPER.writeValueAsString(map);
    }

}
