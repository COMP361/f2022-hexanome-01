package ca.mcgill.splendorserver.models.registries;

import ca.mcgill.splendorserver.apis.GameController;
import ca.mcgill.splendorserver.apis.JsonHandler;
import ca.mcgill.splendorserver.models.cards.Card;
import ca.mcgill.splendorserver.models.cards.CardLevel;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Registry for Splendor development cards.
 */
public class CardRegistry implements Registry<Card> {

  @SuppressWarnings("serial")
  private static final HashMap<Integer, Card> data = new HashMap<Integer, Card>();

  static {
    try {
      //deck 1
      InputStream is = GameController.class.getClassLoader().getResourceAsStream("Deck1.txt");
      JSONArray deck1 = (JSONArray) JsonHandler.decodeJsonRequest(
          new String(is.readAllBytes(), StandardCharsets.UTF_8));

      for (Object obj : deck1) {
        Card card =
            new Card((JSONObject) (JsonHandler.decodeJsonRequest((String) obj)), CardLevel.LEVEL1);
        data.put(card.getId(), card);
      }

      //deck 2
      is = GameController.class.getClassLoader().getResourceAsStream("Deck2.txt");
      JSONArray deck2 = (JSONArray) JsonHandler.decodeJsonRequest(
          new String(is.readAllBytes(), StandardCharsets.UTF_8));

      for (Object obj : deck2) {
        Card card =
            new Card((JSONObject) (JsonHandler.decodeJsonRequest((String) obj)), CardLevel.LEVEL2);
        data.put(card.getId(), card);
      }

      //deck 3
      is = GameController.class.getClassLoader().getResourceAsStream("Deck3.txt");
      JSONArray deck3 = (JSONArray) JsonHandler.decodeJsonRequest(
          new String(is.readAllBytes(), StandardCharsets.UTF_8));

      for (Object obj : deck3) {
        Card card =
            new Card((JSONObject) (JsonHandler.decodeJsonRequest((String) obj)), CardLevel.LEVEL3);
        data.put(card.getId(), card);
      }

      //orient deck 1
      is = GameController.class.getClassLoader().getResourceAsStream("exDeck1.txt");
      JSONArray exDeck1 = (JSONArray) JsonHandler.decodeJsonRequest(
          new String(is.readAllBytes(), StandardCharsets.UTF_8));

      for (Object obj : exDeck1) {
        Card card = new Card((JSONObject) (JsonHandler.decodeJsonRequest((String) obj)),
            CardLevel.ORIENTLEVEL1);
        data.put(card.getId(), card);
      }

      //orient deck 2
      is = GameController.class.getClassLoader().getResourceAsStream("exDeck2.txt");
      JSONArray exDeck2 = (JSONArray) JsonHandler.decodeJsonRequest(
          new String(is.readAllBytes(), StandardCharsets.UTF_8));

      for (Object obj : exDeck2) {
        Card card = new Card((JSONObject) (JsonHandler.decodeJsonRequest((String) obj)),
            CardLevel.ORIENTLEVEL2);
        data.put(card.getId(), card);
      }

      //orient deck 3
      is = GameController.class.getClassLoader().getResourceAsStream("exDeck3.txt");
      JSONArray exDeck3 = (JSONArray) JsonHandler.decodeJsonRequest(
          new String(is.readAllBytes(), StandardCharsets.UTF_8));

      for (Object obj : exDeck3) {
        Card card = new Card((JSONObject) (JsonHandler.decodeJsonRequest((String) obj)),
            CardLevel.ORIENTLEVEL3);
        data.put(card.getId(), card);
      }
    } catch (IOException e) {
      throw new RegistryException("decks", e);
    }
  }

  /**
   * Getter for all cards.
   *
   * @return a collection of all cards
   */
  public static Collection<Card> getCards() {
    return data.values();
  }

  public static Set<Integer> listIds() {
    return data.keySet();
  }

  public static Card of(int id) {
    return data.get(id);
  }
}
