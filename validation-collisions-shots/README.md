[![Build Status](https://travis-ci.org/KarlWalsh/ComboClient.svg?branch=master)](https://travis-ci.org/KarlWalsh/ComboClient)

ComboClient
===========

##Example usage##
```java
public static void main(final String[] args) {
    final Combo combo = httpCombo(create("http://combo-squirrel.herokuapp.com"));

    combo.facts("chat", Map.class)
            .map(fact -> fact.get("who"))
            .filter(who -> who != null && !who.equals("chatterbot"))
            .forEach(who -> combo.publishFact("chat", chatMessage("Greetings %s, what a wonderful day!", who)));
}
```
The first line creates a combo instance that uses `org.springframework.web.client.RestTemplate` to communicate with the combo server located at the specified URI. *This instance of Combo is thread-safe.*

##Subscribing to topics##

```java
combo.subscribeTo("some.topic", Map.class)
```

This will create a `java.util.stream.Stream<T>` which produces facts as they arrive at the topic `some.topic` (in this example `<T>` is `java.lang.Map`). Gson is used to serilialise the json response from combo, so provide your desired type here. If you don't have one, then `java.lang.Map` may be a good choice.

The above example is mapping all chat facts to a String (the sender of the chat message) and filtering itself out of the incoming facts.

*The `java.util.stream.Stream.forEach` method will not return unless there is an exception. If you wish to subscribe to multiple topics, you will need to handle that yourself.*

##Publishing to topics##

```java
combo.publishFact("another.topic", fact)
```

This will serialise the fact to json (using Gson) and publish the json to the combo server

##Caveats##
Requires Java 8
