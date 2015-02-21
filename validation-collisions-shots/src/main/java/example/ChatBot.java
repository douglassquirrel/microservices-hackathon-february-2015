package example;

import combo.Combo;

import java.util.HashMap;
import java.util.Map;

import static combo.ComboFactory.httpCombo;
import static java.net.URI.create;

final class ChatBot {

    public static void main(final String[] args) {
        final Combo combo = httpCombo(create("http://combo-squirrel.herokuapp.com"));

        combo.facts("chat", Map.class)
                .map(fact -> fact.get("who"))
                .filter(who -> who != null && !who.equals("chatterbot"))
                .forEach(who -> combo.publishFact("chat", chatMessage("Greetings %s, what a wonderful day!", who)));
    }

    private static Map<String, Object> chatMessage(final String message, final Object... parameters) {
        final String parameterisedMessage = String.format(message, parameters);

        final Map<String, Object> fact = new HashMap<>();
        fact.put("who", "chatterbot");
        fact.put("says", parameterisedMessage);
        return fact;
    }
}
