package org.drools.ansible.rulebook.integration.core.jpy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.drools.ansible.rulebook.integration.api.domain.RulesSet;

import java.util.Map;
import java.util.Objects;

import static org.drools.ansible.rulebook.integration.api.RulesExecutor.OBJECT_MAPPER;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "command")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AstRulesEngineCommand.CreateRuleset.class, name = "create-ruleset"),
        @JsonSubTypes.Type(value = AstRulesEngineCommand.Dispose.class, name = "dispose"),
        @JsonSubTypes.Type(value = AstRulesEngineCommand.RetractFact.class, name = "retract-fact"),
        @JsonSubTypes.Type(value = AstRulesEngineCommand.AssertFact.class, name = "assert-fact"),
        @JsonSubTypes.Type(value = AstRulesEngineCommand.AssertEvent.class, name = "assert-event"),
        @JsonSubTypes.Type(value = AstRulesEngineCommand.GetFacts.class, name = "get-facts")
})
public interface AstRulesEngineCommand {
    public final class CreateRuleset implements AstRulesEngineCommand {
        @JsonProperty("ruleset")
        private RulesSet ruleset;

        public CreateRuleset() {
        }

        public CreateRuleset(RulesSet ruleset) {
            this.ruleset = ruleset;
        }
        public CreateRuleset(Map<String, Object> ruleset) {
            this.ruleset = OBJECT_MAPPER.convertValue(ruleset, RulesSet.class);
        }

        public void setRuleset(RulesSet ruleset) {
            this.ruleset = ruleset;
        }

        public RulesSet getRuleset() {
            return ruleset;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CreateRuleset that = (CreateRuleset) o;
            return Objects.equals(ruleset, that.ruleset);
        }

        @Override
        public int hashCode() {
            return Objects.hash(ruleset);
        }
    }

    public final class Dispose implements AstRulesEngineCommand {
        @JsonProperty("session-id")
        private long sessionId;

        public Dispose() {
        }

        public Dispose(long sessionId) {
            this.sessionId = sessionId;
        }

        public long getSessionId() {
            return sessionId;
        }

        public void setSessionId(long sessionId) {
            this.sessionId = sessionId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Dispose dispose = (Dispose) o;
            return sessionId == dispose.sessionId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(sessionId);
        }
    }


    public final class RetractFact implements AstRulesEngineCommand {
        @JsonProperty("session-id")
        private long sessionId;
        @JsonProperty("fact")
        private Map<String, Object> fact;

        public RetractFact() {
        }

        public RetractFact(long sessionId, Map<String, Object> fact) {
            this.sessionId = sessionId;
            this.fact = fact;
        }

        public long getSessionId() {
            return sessionId;
        }

        public void setSessionId(long sessionId) {
            this.sessionId = sessionId;
        }

        public Map<String, Object> getFact() {
            return fact;
        }

        public void setFact(Map<String, Object> fact) {
            this.fact = fact;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RetractFact that = (RetractFact) o;
            return sessionId == that.sessionId && Objects.equals(fact, that.fact);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sessionId, fact);
        }
    }

    public final class AssertFact implements AstRulesEngineCommand {
        @JsonProperty("session-id")
        private long sessionId;
        @JsonProperty("fact")
        private Map<String, Object> fact;

        public AssertFact() {
        }

        public AssertFact(long sessionId, Map<String, Object> fact) {
            this.sessionId = sessionId;
            this.fact = fact;
        }

        public long getSessionId() {
            return sessionId;
        }

        public Map<String, Object> getFact() {
            return fact;
        }

        public void setSessionId(long sessionId) {
            this.sessionId = sessionId;
        }

        public void setFact(Map<String, Object> fact) {
            this.fact = fact;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AssertFact that = (AssertFact) o;
            return sessionId == that.sessionId && Objects.equals(fact, that.fact);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sessionId, fact);
        }
    }


    public final class AssertEvent implements AstRulesEngineCommand {
        @JsonProperty("session-id")
        private long sessionId;
        @JsonProperty("event")
        private Map<String, Object> event;

        public AssertEvent() {
        }

        public AssertEvent(long sessionId, Map<String, Object> event) {
            this.sessionId = sessionId;
            this.event = event;
        }

        public long getSessionId() {
            return sessionId;
        }

        public void setSessionId(long sessionId) {
            this.sessionId = sessionId;
        }

        public Map<String, Object> getEvent() {
            return event;
        }

        public void setEvent(Map<String, Object> event) {
            this.event = event;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AssertEvent that = (AssertEvent) o;
            return sessionId == that.sessionId && Objects.equals(event, that.event);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sessionId, event);
        }
    }


    public final class GetFacts implements AstRulesEngineCommand {
        @JsonProperty("session-id")
        private long sessionId;

        public GetFacts() {
        }

        public GetFacts(long sessionId) {
            this.sessionId = sessionId;
        }

        public long getSessionId() {
            return sessionId;
        }

        public void setSessionId(long sessionId) {
            this.sessionId = sessionId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GetFacts getFacts = (GetFacts) o;
            return sessionId == getFacts.sessionId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(sessionId);
        }
    }

}
