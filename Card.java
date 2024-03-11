import java.util.*;
import java.awt.image.BufferedImage;

public class Card implements Comparable<Card> {

    String suit;
    String face;
    private BufferedImage cardImage;

    Card (String suit, String face, BufferedImage cardImage) {
        this.suit = suit;
        this.face = face;
        this.cardImage = cardImage;
    }

    // array for suit
    private static final String[] arraySuit = new String[] { "d", "c", "h", "s" };
    // hashmap for face with its points
    private static final Map<String, Integer> arrayFace = new HashMap<>();
    // arraylist of all cards without shuffle
    private static final ArrayList<Card> allCardsWithoutShuffle = new ArrayList<>();
    // arraylist of all cards with shuffle
    private static final ArrayList<Card> allCards = new ArrayList<>();
    // index for card distribution
    private static int index;

    // to initialise the number(Key) with their point(Value)
    static {
        arrayFace.put("A", 1);
        arrayFace.put("2", 2);
        arrayFace.put("3", 3);
        arrayFace.put("4", 4);
        arrayFace.put("5", 5);
        arrayFace.put("6", 6);
        arrayFace.put("7", 7);
        arrayFace.put("8", 8);
        arrayFace.put("9", 9);
        arrayFace.put("X", 10);
        arrayFace.put("J", 10);
        arrayFace.put("Q", 10);
        arrayFace.put("K", 10);

        createAllNewCards();
    }

    // create a set of cards and shuffle it
    public static void createAllNewCards() {

        for (String currentKey : arrayFace.keySet()) { // to get all keys(number)
            for (String s : arraySuit) { // to get all suits
                Card newCard = new Card(s, currentKey, null);
                allCards.add(newCard); // add to arraylist, later will be shuffled
                allCardsWithoutShuffle.add(newCard); // with correct sequence
            }
        }
        shuffleCards();
    }

    // shuffle the all cards
    public static void shuffleCards() {
        Collections.shuffle(allCards);
    }

    // get the distributed cards
    public static ArrayList<Card> distributeCards(int numberOfCards) {
        ArrayList<Card> distributedCards = new ArrayList<>();

        // assign the card to the player
        for (int i = index, j = 0; i < 52 && j < numberOfCards; j++, i++) {
            try{
                distributedCards.add(allCards.get(i));
                index++;

                // reset the index
                if (index == 52) {
                    index = 0;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return distributedCards;
    }

    public static Map<String, Integer> getArrayFace() {
        return arrayFace;
    }

    public static ArrayList<Card> getAllCards() {
        return allCards;
    }

    public String getFace() {
        return face;
    }

    public static ArrayList<Card> getAllCardsWithoutShuffle() {
        return allCardsWithoutShuffle;
    }

    public String getSuit() {
        return suit;
    }

    public BufferedImage getCardImage() {
        return cardImage;
    }

    @Override
    public String toString() {
        return suit + face;
    }

    @Override
    public int compareTo(Card o) {
        int result = 0;
        result = this.suit.compareTo(o.suit);
        if (result == 0)
            result = arrayFace.get(this.face).compareTo(arrayFace.get(o.face)); // compare their point (value)
        return result;
    }
}