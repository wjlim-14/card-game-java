import java.util.*;

public class Game {

    Player playerOne;
    Player playerTwo;
    Player playerThree;
    // arraylist of players still in the game
    ArrayList<Player> playerInGame = new ArrayList<>();

    // input player Name
    public void enterName() {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter player 1 name: ");
        playerOne = new Player(input.nextLine());
        playerInGame.add(playerOne);

        System.out.print("Enter player 2 name: ");
        playerTwo = new Player(input.nextLine());
        playerInGame.add(playerTwo);

        System.out.print("Enter player 3 name: ");
        playerThree = new Player(input.nextLine());
        playerInGame.add(playerThree);
    }

    // when start the game from beginning
    public void startGame(int stageWhat) {

        if (stageWhat == 1) { // phase 1
            System.out.println("\n**** 3-Player Phase ****");
            Card.shuffleCards();
            int count = 0;
            // create a queue(card on hand) for each player
            for (Player thisPlayer: playerInGame) {
                if(count == 0){
                    LinkedList<Card> listThisPlayer = new LinkedList<>(Card.distributeCards(18));
                    Queue<String> queueThisPlayer = stringCardsInQueue(listThisPlayer, 18); // put 5 cards into 1 String and add to queue
                    thisPlayer.assignCards(queueThisPlayer); // assign to the player's card on hand
                }
                else {
                    LinkedList<Card> listThisPlayer = new LinkedList<>(Card.distributeCards(17));
                    Queue<String> queueThisPlayer = stringCardsInQueue(listThisPlayer, 17); // put 5 cards into 1 String and add to queue
                    thisPlayer.assignCards(queueThisPlayer); // assign to the player's card on hand
                }
                count ++;
            }
        }

        else if (stageWhat == 2) { // phase 2
            System.out.println("**** 2-Player Phase ****");
            Card.shuffleCards();
            // create a queue(card on hand) for each player
            for (Player thisPlayer: playerInGame) {
                LinkedList<Card> listThisPlayer = new LinkedList<>(Card.distributeCards(26));
                Queue<String> queueThisPlayer = stringCardsInQueue(listThisPlayer, 26); // put 5 cards into 1 String and add to queue
                thisPlayer.assignCards(queueThisPlayer); // assign to the player's card on hand
            }
        }

        // display every players' card on hand
        System.out.println("\nAvailable Cards: ");
        for (Player thisPlayer: playerInGame) {
            thisPlayer.setNewScore(0); // reset new score to 0
            System.out.println(thisPlayer.getName() + " : " + thisPlayer.getCardsOnHand());
        }
        askForShuffle(stageWhat);
    }

    // ask whether need to shuffle the cards
    public void askForShuffle(int stageWhat) {
        System.out.println("\nPress S to Shuffle or Press Enter to Start.");
        Scanner input = new Scanner(System.in);
        String userChoice = input.nextLine();
        if (userChoice.equals("S")  || userChoice.equals("s")) { // yes, shuffle
            System.out.println("<S is pressed>");
            Card.shuffleCards();
            reassign(stageWhat);
        }
        else { // no, start game
            if (stageWhat == 1) {
                System.out.println("<Enter is pressed>");
                runRound(1);
            }
            else if (stageWhat == 2) {
                System.out.println("<Enter is pressed>");
                runRound(2);
            }
        }
    }

    // use this after shuffle
    public void reassign(int stageWhat) {
        if (stageWhat == 1)
            startGame(1);
        else if (stageWhat == 2)
            startGame(2);
    }

    // process of the game
    public void runRound(int stageWhat) {
        int round = 1;
        int stopGame;

        if (stageWhat == 1)
            stopGame = 3; // phase 1
        else
            stopGame = 4; // phase 2

        while (round < stopGame + 1) {
            System.out.println("\n*** ROUND " + round + " ***\n");

            // put all player's card into an arraylist to loop (before sort)
            ArrayList<String> arrayCardsForEachRound = new ArrayList<>();
            for (Player thisPlayer : playerInGame)
                arrayCardsForEachRound.add(thisPlayer.cardsForEachRound());

            // find the highest score
            int highest = findWinner1();

            // put all player's card into an arraylist to loop (after sort)
            arrayCardsForEachRound = sortCards(arrayCardsForEachRound);

            System.out.println("Cards at Hand: ");
            for (int i = 0; i < playerInGame.size(); i++) {
                // for winner
                if (playerInGame.get(i).getOldScore() == highest)
                    System.out.println(playerInGame.get(i).getName() + " : " + arrayCardsForEachRound.get(i) + " | Point = " + playerInGame.get(i).getOldScore() + " | Win");
                    // for loser
                else
                    System.out.println(playerInGame.get(i).getName() + " : " + arrayCardsForEachRound.get(i) + " | Point = " + playerInGame.get(i).getOldScore());
            }

            System.out.println("\nScore: "); // only players with highest score get points

            for (Player thisPlayer : playerInGame) {
                // if is winner, then add the point(oldScore) into their final score(newScore)
                if (thisPlayer.getOldScore() == highest)
                    thisPlayer.setNewScore(thisPlayer.getNewScore() + highest);
                    // if not, then maintain previous score(newScore)
                else
                    thisPlayer.setNewScore(thisPlayer.getNewScore());
                System.out.println(thisPlayer.getName() + " = " + thisPlayer.getNewScore());
            }

            // display every players' card on hand
            System.out.println("\nAvailable Cards: ");
            for (Player thisPlayer: playerInGame)
                System.out.println(thisPlayer.getName() + " : " + thisPlayer.getCardsOnHand());

            if (round != stopGame) // to find the winners
                promptEnter("\nPress ENTER to next round.");
            else {
                if (stageWhat == 1)
                    winnerWith3Ppl();
                else
                    winnerWith2Ppl();
            }

            round += 1;
        }
    }

    // determine the winner / tie in 2 player round
    public void winnerWith2Ppl() {
        int lowest = findWinner2();
        if (lowest == -1) { // if no loser, play again
            promptEnter("We don't have any loser, let's play again!");
            startGame(2);
        }
        else {
            Player loser = playerInGame.get(0);
            for (Player thisPlayer : playerInGame) // find the loser
                if (thisPlayer.getNewScore() == lowest)
                    loser = thisPlayer;

            playerInGame.remove(loser); // remove the loser from game

            System.out.println("\n***** " + playerInGame.get(0).getName() + " is the WINNER! *****\n");
            promptEnter("Press Enter to exit game.");
        }
    }

    // determine the winner / tie in 3 player round
    public void winnerWith3Ppl(){
        int lowest = findWinner2();
        if (lowest == -1){ // if no loser, play again
            promptEnter("It's a tie! Press Enter to start a new game.");
            startGame(1);
        }
        else {
            Player loser = playerInGame.get(0);
            for (Player thisPlayer : playerInGame) // find the loser
                if (thisPlayer.getNewScore() == lowest)
                    loser = thisPlayer;

            playerInGame.remove(loser); // remove the loser from game

            // print the winners for this stage
            System.out.println("\n***** " + playerInGame.get(0).getName() + " and " + playerInGame.get(1).getName() + " proceed to 2-Player phase *****\n");
            promptEnter("Press Enter to continue.");
            startGame(2);
        }
    }

    // to find winner for each round
    private int findWinner1() {
        // initialise the largest score
        int largestScore = playerInGame.get(0).getOldScore();

        for (int i = 1; i < playerInGame.size(); i++)
            if (playerInGame.get(i).getOldScore() > largestScore)
                largestScore = playerInGame.get(i).getOldScore();

        return largestScore;
    }

    // to find winner for each game(phase)
    private int findWinner2() {
        // as a flag, if tie, then tie += 1
        int tie = 0;
        // initialise the lowest score
        int lowestScore = playerInGame.get(0).getNewScore();

        for (int i = 1; i < playerInGame.size(); i++)
            if (playerInGame.get(i).getNewScore() < lowestScore)
                lowestScore = playerInGame.get(i).getNewScore();

        // make sure not tie (tie > 1 means tie)
        for (Player thisPlayer : playerInGame)
            if (thisPlayer.getNewScore() == lowestScore)
                tie += 1;

        if (tie > 1)
            lowestScore = -1; //When tie return -1

        return lowestScore;
    }

    // put into queue which has been separated equally(all cards become string)
    public static LinkedList<String> stringCardsInQueue (LinkedList<Card> cards, int numberOfCards) {
        LinkedList<String> temp = new LinkedList<>();
        int i = 0;
        while(i < numberOfCards) {
            int count = 0;
            String cardsForARound = ""; // combine 5 cards into a String
            for (int j = i; j < numberOfCards; j++) {
                if (count == 0)
                    cardsForARound = cards.get(j).toString();
                else
                    cardsForARound = cardsForARound + " " + cards.get(j).toString();
                count++;
                i++;
                if(count == 5 || i == numberOfCards) break; // limit of a String in each queue
            }
            temp.add(cardsForARound);
        }
        cards.clear();
        return temp;
    }

    // before sort the cards we need to change String back to Card, after that return String
    public ArrayList<String> sortCards(ArrayList<String> cardsBeforeSorting) {
        ArrayList<String> sortedCards = new ArrayList<>();
        for(String card: cardsBeforeSorting){ // for each player's cards
            ArrayList<Card> temp = new ArrayList<>();
            String[] split = card.split(" "); // split those 5 String
            for (String s : split) { // convert 5 String to Card
                char stringSuit = s.charAt(0);
                char stringFace = s.charAt(1);
                Card newCard = new Card(String.valueOf(stringSuit), String.valueOf(stringFace), null);
                temp.add(newCard);
            }
            Collections.sort(temp); // sort it using compareTo from Card class
            String cardsForARound = ""; // change back to String
            for (int j = 0; j < temp.size(); j++) {
                if (j == 0)
                    cardsForARound = temp.get(j).toString();
                else
                    cardsForARound = cardsForARound + " " + temp.get(j).toString();
            }
            sortedCards.add(cardsForARound);
        }
        return sortedCards;
    }

    // pause the program
    public void promptEnter(String message) {
        System.out.println(message);
        Scanner input = new Scanner(System.in);
        input.nextLine();
    }
}