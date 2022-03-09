package com.BlackJackGame;

import javax.swing.*;

public class BlackJackDriver {

    public static JFrame menuFrame = new JFrame(); //This is the frame which we will show when the user opens the game. It will contain basic options like 'Play' and 'Exit'.
    public static JFrame gameFrame = new JFrame(); //This is the frame in which the real blackjack game will be played.

    private static int playerScore = 0;
    private static int dealerScore = 0;
    static int currentBalance = 1000;

    static Game newGame = new Game(gameFrame);
    private static boolean isFirstTime = true; //this boolean value will check if the game is newly started for the first time.

    public static enum STATE
 //This enum represents the state of the game which is either menu or game. While it is menu, we will show the user the menu. While it is game, we will show the user the game.
    {
        MENU, GAME
    };

    public static STATE currentState = STATE.MENU;

    public static void main(String[] args) throws InterruptedException
    {
        if(currentState == STATE.MENU)
        {
            openMenu();
        }
    }

    public static void openMenu()
    {
        menuFrame.setTitle("BLACKJACK!");
        menuFrame.setSize(1130, 665);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setResizable(false);

        BJOptionsComponent beginningComponent = new BJOptionsComponent();
        menuFrame.add(beginningComponent);
        menuFrame.setVisible(true);
    }
    public static Thread gameRefeshThread = new Thread() //This enum represents the state of the game which is either menu or game. While it is menu, we will show the user the menu. While it is game, we will show the user the game.
    {
        public void run()
        {
            while(true)
            {
                newGame.atmosphereComponent.refresh(currentBalance, playerScore, dealerScore-1, newGame.faceDown);
 //this line calls the refresh method of the GameComponent atmosphere component which is declared inside the Game class. This method updates the score and balance values.
 //the reason why we put the parameter dealerscore-1 is because dealerscore starts as 1 in our game.
            }
        }
    };

    public static Thread gameCheckThread = new Thread() //the second thread continually[while(true)] checks the game for a round over situation.
    {
        public void run()
        {
            while(true)
            {
                if(isFirstTime || newGame.roundOver) { //if this is the first time the game is started or the round is over (which was thanks to the checkHand method in Game),
                    System.out.println("Refreshing Game!");
                    if (newGame.dealerWon) {
                        dealerScore++;
                        currentBalance -= BJGameComponent.currentBet;
                    } else {
                        playerScore++;
                        currentBalance += BJGameComponent.currentBet * 2;
                    }
                    gameFrame.getContentPane().removeAll();  //we remove everything from the frame.
                    newGame = new Game(gameFrame); //we initialize a new game on the same frame.
                    newGame.formGame();  //we set the atmosphere of the game(which is everything except the cards.)

                    isFirstTime = false;
                }
            }
        }
    };
}
