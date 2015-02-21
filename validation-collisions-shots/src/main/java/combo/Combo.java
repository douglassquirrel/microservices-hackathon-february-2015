package combo;

import java.util.stream.Stream;

public interface Combo {

    <T> Stream<T> facts(String topicName, Class<? extends T> classOfT);

    <T> void publishFact(String topicName, T fact);

}
