package combo;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

final class FactCollector<T> implements Consumer<T> {

    private final List<T> facts = new LinkedList<>();

    @Override public void accept(final T fact) {
        facts.add(fact);
    }

    List<T> facts() {
        return facts;
    }
}
