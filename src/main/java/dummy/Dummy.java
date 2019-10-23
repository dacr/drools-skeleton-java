package dummy;

import org.kie.api.*;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

public class Dummy {
    static Logger logger = LoggerFactory.getLogger(Dummy.class);
    public static void main(String[] args) {
        KieServices kServices = KieServices.Factory.get();
        KieContainer kContainer = kServices.getKieClasspathContainer();
        KieBase kbase = kContainer.getKieBase("HelloKB");
        KieSession ksession = kbase.newKieSession();
        try {
            ksession.setGlobal("logger", LoggerFactory.getLogger("HelloKB"));
            ksession.insert(new Someone("joe"));
            ksession.fireAllRules();
            Stream<HelloResponse> messages =
                    ksession.getObjects()
                            .stream()
                            .filter(item -> item instanceof HelloResponse)
                            .map(item -> (HelloResponse)item);
            assert(messages.filter(s -> s.getMessage().equals("Hello joe")).findFirst().isPresent());
        } finally {
            ksession.dispose();
        }
    }
}
