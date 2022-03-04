package com.BlackJack;

import java.util.Scanner;

public class Blackjack {
    public static void main(String[] args)
    {
        //Welcome Message
        System.out.println("Welcome to BlackJack!");

        //Create Playing Deck
        Deck playingDeck = new Deck();
        playingDeck.createFullDeck();
        playingDeck.shuffle();

        //Create a deck for the player
        Deck playerDeck = new Deck();

        //Create a deck for the dealer
        Deck dealerDeck = new Deck();

        double playerMoney = 100.00;
        Scanner userInput = new Scanner(System.in);

       //GAME LOOP
        while(playerMoney > 0.00)
        {
            //Play!
            System.out.println("Money: " + playerMoney + "\n" + "Place Your Bet:");
            double playerBet = userInput.nextDouble();
            if(playerBet > playerMoney)
            {
                System.out.println("You thought");
                break;
            }

            boolean endRound = false;

            //Start Dealing
            //Player gets two cards
            playerDeck.draw(playingDeck);
            playerDeck.draw(playingDeck);

            //Dealer gets two cards
            dealerDeck.draw(playingDeck);
            dealerDeck.draw(playingDeck);

            while(true)
            {
                //System.out.println("Your Hand:");
                System.out.println("Your Hand:" + playerDeck.toString());
                System.out.println("Deck Value: " + playerDeck.cardsValue());

                //Display Dealer Hand
                System.out.println("\n" + "Dealer Hand: " + dealerDeck.getCard(0).toString() + " & [Hidden Card]");
                System.out.println("Dealer Deck Value: " );

                //Player Decision
                System.out.println("Would you like to (1)Hit or (2)Stand?");
                int response = userInput.nextInt();

                //Hit
                if(response == 1)
                {
                    playerDeck.draw(playingDeck);
                    System.out.println("NEW Card:" + playerDeck.getCard(playerDeck.deckSize() -1).toString());

                    //Bust if > 21
                    if(playerDeck.cardsValue() > 21)
                    {
                        System.out.println("\n" + "BUST! - " + playerDeck.cardsValue());
                        playerMoney -= playerBet;
                        endRound = true;
                        break;
                    }
                }
               if(response == 2)
                {
                    break;
                }
            }

            //Reveal Dealer Cards
            System.out.println("\n" + "Dealer Cards: " + dealerDeck.toString());
            //See if dealer has more points than player
            if((dealerDeck.cardsValue() > playerDeck.cardsValue()) && endRound == false)
            {
                System.out.println("Dealer WINS!");
                endRound = true;
            }
            //Dealer Draws at 16 stand at 17
            while((dealerDeck.cardsValue() < 17 && endRound == false))
            {
                dealerDeck.draw(playingDeck);
                System.out.println("Dealer Draws:" + dealerDeck.getCard(dealerDeck.deckSize() -1 ).toString());
            }
            //Display Total value for dealer
            System.out.println("Dealers Hand: " + dealerDeck.cardsValue());

            //Determine if dealer busted
            if((dealerDeck.cardsValue() > 21) && endRound == false)
            {
                System.out.println("Dealer BUST! - YOU WIN!");
                playerMoney += playerBet;
                endRound = true;
            }

            //Determine if push
            if((playerDeck.cardsValue() == dealerDeck.cardsValue()) && endRound == false)
            {
                System.out.println("PUSH!");
                endRound = true;
            }

            if((playerDeck.cardsValue() > dealerDeck.cardsValue()) && endRound == false)
            {
                System.out.println("YOU WIN!");
                playerMoney += playerBet;
                endRound = true;
            }

            playerDeck.moveAllToDeck(playingDeck);
            dealerDeck.moveAllToDeck(playingDeck);
            System.out.println("\n" + "End of Hand.");
        }
        System.out.println("GAME OVER! - Oof.. You are AHT of money");

    }
}
