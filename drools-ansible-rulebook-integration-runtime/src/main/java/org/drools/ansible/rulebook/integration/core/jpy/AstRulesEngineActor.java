package org.drools.ansible.rulebook.integration.core.jpy;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.drools.ansible.rulebook.integration.api.RulesExecutor;
import org.drools.ansible.rulebook.integration.core.jpy.AstRulesEngineCommand.AssertEvent;
import org.drools.ansible.rulebook.integration.core.jpy.AstRulesEngineCommand.AssertFact;
import org.drools.ansible.rulebook.integration.core.jpy.AstRulesEngineCommand.CreateRuleset;
import org.drools.ansible.rulebook.integration.core.jpy.AstRulesEngineCommand.Dispose;
import org.drools.ansible.rulebook.integration.core.jpy.AstRulesEngineCommand.GetFacts;
import org.drools.ansible.rulebook.integration.core.jpy.AstRulesEngineCommand.RetractFact;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AstRulesEngineActor {
    private Queue<String> outgoing = new ConcurrentLinkedQueue<>();
    private Queue<AstRulesEngineCommand> incoming = new ConcurrentLinkedQueue<>();
    private AstRulesEngineInternal astRulesEngine = new AstRulesEngineInternal();

    public void tell(String serializedCommand) {
        try {
            AstRulesEngineCommand command =
                    RulesExecutor.OBJECT_MAPPER.readValue(serializedCommand, AstRulesEngineCommand.class);

            if (command instanceof CreateRuleset) {
                CreateRuleset createRuleset = (CreateRuleset) command;
                long sessionId =
                        astRulesEngine.createRuleset(createRuleset.getRuleset());
                outgoing.offer(String.valueOf(sessionId));
            } else if (command instanceof Dispose) {
                Dispose dispose = (Dispose) command;
                astRulesEngine.dispose(dispose.getSessionId());
            } else if (command instanceof RetractFact) {
                RetractFact rf = (RetractFact) command;
                String result = toJson(astRulesEngine.retractFact(rf.getSessionId(), rf.getFact()));
                outgoing.offer(result);
            } else if (command instanceof AssertFact) {
                AssertFact af = (AssertFact) command;
                String result = toJson(astRulesEngine.assertFact(af.getSessionId(), af.getFact()));
                outgoing.offer(result);
            } else if (command instanceof AssertEvent) {
                AssertEvent ae = (AssertEvent) command;
                String result = toJson(astRulesEngine.assertEvent(ae.getSessionId(), ae.getEvent()));
                outgoing.offer(result);
            } else if (command instanceof GetFacts) {
                GetFacts gf = (GetFacts) command;
                String facts = toJson(astRulesEngine.getFacts(gf.getSessionId()));
                outgoing.offer(facts);
            } else {
                throw new UnsupportedOperationException(serializedCommand);
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String toJson(Object o) throws JsonProcessingException {
        return RulesExecutor.OBJECT_MAPPER.writeValueAsString(o);
    }

}
