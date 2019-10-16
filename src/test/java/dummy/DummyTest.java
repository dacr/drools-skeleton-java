package dummy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.stream.Stream;


public class DummyTest {

    @Test
    void droolsSkeleton() {
        KieServices kServices = KieServices.Factory.get();
        KieContainer kContainer = kServices.getKieClasspathContainer();
        KieBase kbase = kContainer.getKieBase("HelloKB");
        KieSession ksession = kbase.newKieSession();
        try {
            ksession.setGlobal("logger", LoggerFactory.getLogger("HelloKB"));
            ksession.insert(new Hello("world"));
            ksession.fireAllRules();
            Stream<HelloResponse> messages =
                    ksession.getObjects()
                            .stream()
                            .filter(item -> item instanceof HelloResponse)
                            .map(item -> (HelloResponse)item);
            assertEquals(Optional.of("Hello world"), messages.map(s -> s.getMessage()).findFirst());
        } finally {
            ksession.dispose();
        }

    }
}
