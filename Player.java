import java.util.*;

public class Player {

    private final String name;
    private int oldScore; // For each round
    private int newScore; // For each stage
    private Queue<String> cardsOnHand;

    // create a player when start a game
    Player(String name) {
        this.name = name;
        oldScore = 0;
        newScore = 0;
    }

    // assign cards to each player
    public void assignCards(Queue<String> assignedCards) {
        cardsOnHand = assignedCards;
    }

    public Queue<String> getCardsOnHand() {
        return cardsOnHand;
    }

    public String getName() {
        return name;
    }

    public int getOldScore() {
        return oldScore;
    }

    public int getNewScore() {
        return newScore;
    }

    public void setNewScore(int newScore) {
        this.newScore = newScore;
    }

    // cards for each round
    public String cardsForEachRound() {
        // take the first element in queue (5 cards)
        String cardsForThisRound = cardsOnHand.poll();
        calScore1(cardsForThisRound); // calculate score for this round
        return cardsForThisRound; // return the rest of the elements in queue
    }

    // to calculate score for each suit and find largest
    private void calScore1(String cards) {
        String[] splitCards = cards.split(" "); // split 5 cards
        Stack<String> suitOfd = new Stack<>();
        int scoreOfd = 0;
        Stack<String> suitOfc = new Stack<>();
        int scoreOfc = 0;
        Stack<String> suitOfh = new Stack<>();
        int scoreOfh = 0;
        Stack<String> suitOfs = new Stack<>();
        int scoreOfs = 0;

        // to separate 5 cards into 1 card and add to stack according to the type of suit
        for (String thisCard : splitCards) {
            char charSuit = thisCard.charAt(0); // the suit, need to be in char not String
            String thisFace = String.valueOf(thisCard.charAt(1)); // the face, need to be in String not char
            switch (charSuit) {
                case 'd': suitOfd.push(thisFace); break;
                case 'c': suitOfc.push(thisFace); break;
                case 'h': suitOfh.push(thisFace); break;
                case 's': suitOfs.push(thisFace); break;
            }
        }

        // calculate score for each suit
        scoreOfd += calScore2(suitOfd);
        scoreOfc += calScore2(suitOfc);
        scoreOfh += calScore2(suitOfh);
        scoreOfs += calScore2(suitOfs);

        // put all score for each suit into an array to find the largest score
        int[] allSuitScore = new int[]{scoreOfd, scoreOfc, scoreOfh, scoreOfs};
        // set the largest score for oldScore
        oldScore = findLargest(allSuitScore);
    }

    // to get value using key
    private int calScore2(Stack<String> calThisStack) {
        int scoreOfThisStack = 0;

        // find their value according to their key
        for (String key: calThisStack) {
            int thisScore = Card.getArrayFace().get(key);
            scoreOfThisStack += thisScore; // add all points together
        }
        
        return scoreOfThisStack;
    }

    // to find largest combination
    private static int findLargest(int[] allSuitScore) {
        // initialise the largest score
        int largestScore = allSuitScore[0];

        for (int i = 1; i < allSuitScore.length; i++)
            if (allSuitScore[i] > largestScore)
                largestScore = allSuitScore[i];

        return largestScore;
    }
}
