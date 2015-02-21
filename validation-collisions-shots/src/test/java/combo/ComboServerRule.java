package combo;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import com.google.gson.Gson;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.lang.String.format;

public final class ComboServerRule implements TestRule {

    private final WireMockServer server;

    public ComboServerRule(final int port) {
        this.server = new WireMockServer(port);
    }

    @Override public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override public void evaluate() throws Throwable {
                try {
                    server.start();
                    base.evaluate();
                } finally {
                    server.stop();
                }
            }
        };
    }

    public ComboServerResponseStep whenTopicIsSubscribedTo(final String topicName) {
        return (response, restOfTheResponses) -> server.givenThat(post(urlEqualTo(format("/topics/%s/subscriptions", topicName)))
                .willReturn(response.responseDefinitionBuilder));
    }

    public ComboServerResponseStep whenNextFactIsRequested(final String topicName, final String subscriptionId) {
        return (response, restOfTheResponses) -> {
            int i = 0;
            for (final ComboServerResponse nextResponse : combine(response, restOfTheResponses)) {
                server.givenThat(get(urlEqualTo(format("/topics/%s/subscriptions/%s/next", topicName, subscriptionId)))
                        .inScenario("sequence_of_responses")
                        .whenScenarioStateIs(i == 0 ? Scenario.STARTED : "response_" + (i - 1))
                        .willReturn(nextResponse.responseDefinitionBuilder)
                        .willSetStateTo("response_" + i));
                i = i + 1;
            }
        };
    }

    public ComboServerResponseStep whenFactIsPublished(final String topicName) {
        return (response, restOfTheResponses) -> server.givenThat(post(urlEqualTo(format("/topics/%s/facts", topicName)))
                .willReturn(response.responseDefinitionBuilder));
    }

    public void verifyFactWasPublished(final String topicName) {
        server.verify(postRequestedFor(urlEqualTo(format("/topics/%s/facts", topicName))));
    }

    public interface ComboServerResponseStep {
        void thenRespondWith(ComboServerResponse response, ComboServerResponse... restOfTheResponses);
    }

    public static final class ComboServerResponse {

        private final ResponseDefinitionBuilder responseDefinitionBuilder;

        private ComboServerResponse(final ResponseDefinitionBuilder responseDefinitionBuilder) {
            this.responseDefinitionBuilder = responseDefinitionBuilder;
        }

        public static ComboServerResponse subscriptionWithId(final String subscriptionId) {
            return new ComboServerResponse(aResponse()
                    .withHeader(Header.Keys.CONTENT_TYPE, Header.ContentType.JSON)
                    .withBody(subscriptionAsJson(subscriptionId)));
        }

        public static ComboServerResponse fact(final Object fact) {
            return new ComboServerResponse(aResponse()
                    .withHeader(Header.Keys.CONTENT_TYPE, Header.ContentType.JSON)
                    .withBody(new Gson().toJson(fact)));
        }

        public static ComboServerResponse ok() {
            return new ComboServerResponse(aResponse().withStatus(HttpStatus.OK));
        }

        public static ComboServerResponse noContent() {
            return new ComboServerResponse(aResponse().withStatus(HttpStatus.NO_CONTENT));
        }

        public static ComboServerResponse badRequest() {
            return new ComboServerResponse(aResponse().withStatus(HttpStatus.BAD_REQUEST));
        }

        private static String subscriptionAsJson(final String subscriptionId) {
            final Map<String, Object> subscriptionAsJson = new HashMap<>();
            subscriptionAsJson.put("subscription_id", subscriptionId);
            return new Gson().toJson(subscriptionAsJson);
        }

    }

    @SuppressWarnings("unchecked") private static <T> List<T> combine(final T firstT, final T... restOfTheTs) {
        final List<T> allTheTs = new ArrayList<>(Arrays.asList(restOfTheTs));
        allTheTs.add(0, firstT);
        return allTheTs;
    }

    static final class Header {
        static final class Keys {
            static final String CONTENT_TYPE = "Content-Type";
        }

        static final class ContentType {
            static final String JSON = "application/json";
        }
    }

    static final class HttpStatus {
        static final int OK = 200;
        static final int BAD_REQUEST = 400;
        static final int NO_CONTENT = 204;
    }
}
