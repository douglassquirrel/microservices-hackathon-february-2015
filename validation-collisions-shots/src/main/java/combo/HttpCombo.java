package combo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Stream.generate;

final class HttpCombo implements Combo {

    private final FactProvider factProvider;
    private final FactPublisher factPublisher;
    private final TopicSubscriber topicSubscriber;

    HttpCombo(final RestTemplate restTemplate) {
        this.factProvider = new FactProvider(restTemplate);
        this.factPublisher = new FactPublisher(restTemplate);
        this.topicSubscriber = new TopicSubscriber(restTemplate);
    }

    public <T> Stream<T> facts(final String topicName, final Class<? extends T> factClass) {
        final SubscriptionId subscriptionId = topicSubscriber.subscribeTo(topicName);
        return generate(() -> factProvider.nextFact(subscriptionId, factClass));
    }

    public <T> void publishFact(final String topicName, final T fact) {
        factPublisher.publishFact(topicName, fact);
    }

    private static final class FactProvider {

        private final RestTemplate restTemplate;

        private FactProvider(final RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }

        private <T> T nextFact(final SubscriptionId subscriptionId, final Class<? extends T> classOfFact) {
            final ResponseEntity<? extends T> response = restTemplate.getForEntity(Paths.nextFact(subscriptionId), classOfFact);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                return nextFact(subscriptionId, classOfFact);
            }

            return response.getBody();
        }
    }

    private static final class TopicSubscriber {

        private final RestTemplate restTemplate;

        private TopicSubscriber(final RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }

        private SubscriptionId subscribeTo(final String topicName) {
            final ResponseEntity<Map> response = restTemplate.postForEntity(Paths.subscriptions(topicName), "", Map.class);
            final String comboId = (String) response.getBody().get("subscription_id");
            return new SubscriptionId(topicName, comboId);
        }
    }

    private static final class FactPublisher {

        private final RestTemplate restTemplate;

        private FactPublisher(final RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }

        private <T> void publishFact(final String topicName, final T fact) {
            restTemplate.postForEntity(Paths.facts(topicName), fact, Void.class);
        }
    }

    private static final class SubscriptionId {

        private final String topicName;
        private final String comboId;

        private SubscriptionId(final String topicName, final String comboId) {
            this.topicName = topicName;
            this.comboId = comboId;
        }

        private String topicName() {
            return topicName;
        }

        private String comboId() {
            return comboId;
        }
    }

    private static final class Paths {

        private static URI subscriptions(final String topicName) {
            return new UriTemplate("/topics/{topicName}/subscriptions").expand(topicName);
        }

        private static URI nextFact(final SubscriptionId subscriptionId) {
            return new UriTemplate("/topics/{topicName}/subscriptions/{subscriptionId}/next")
                    .expand(subscriptionId.topicName(), subscriptionId.comboId());
        }

        public static URI facts(final String topicName) {
            return new UriTemplate("/topics/{topicName}/facts").expand(topicName);
        }

    }
}
