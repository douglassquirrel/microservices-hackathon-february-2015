package combo;

import uk.org.fyodor.generators.Generator;
import uk.org.fyodor.generators.characters.CharacterSetFilter;

final class RDG extends uk.org.fyodor.generators.RDG {

    static Generator<String> topicName() {
        return string(10, CharacterSetFilter.LettersAndDigits);
    }

    static Generator<String> subscriptionId() {
        return string(10, CharacterSetFilter.LettersAndDigits);
    }

    private RDG() {
    }
}
