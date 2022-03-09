package com.BlackJackGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;

public class Game {  //TODO: DISPLAY THE SUM OF CURRENT HAND & THE GAME CURRENTLY CANNOT TIE

    ArrayList<Card> dealerHand;
    ArrayList<Card> playerHand;

    public boolean faceDown;
    public boolean dealerWon;
    public volatile boolean roundOver;
    //this boolean value will tell the program if the round is over.
    //added keyword volatile because: "To ensure that changes made by one thread are visible to other
    //threads you must always add some synchronization between the threads.
    //The simplest way to do this is to make the shared variable volatile.


    JFrame frame;
    Deck deck;
    BJGameComponent atmosphereComponent;
    BJGameComponent cardComponent;

    JButton btnHit;
    JButton btnStand;
    JButton btnDouble;
    JButton btnExit;

    public Game(JFrame f)
    {
        deck = new Deck();
        deck.shuffleDeck();
        dealerHand = new ArrayList<Card>();
        playerHand = new ArrayList<Card>();
        atmosphereComponent = new BJGameComponent(dealerHand, playerHand);
        frame = f;
        faceDown = true;
        dealerWon = true;
        roundOver = false;
    }

    public void formGame() //this method will help us create the background of our game.
    {
        System.out.println("GAME CREATED.");
        frame.setTitle("BLACKJACK!");
        frame.setSize(1130,665);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        btnHit = new JButton("HIT");
        btnHit.setBounds(390,550,100,50);
        btnHit.setFont(new Font("Comic Sans MS", Font.BOLD, 16));

        btnStand = new JButton("STAND");
        btnStand.setBounds(520,550,100,50);
        btnStand.setFont(new Font("Comic Sans MS", Font.BOLD, 16));

        btnDouble = new JButton("DBL");
        btnDouble.setBounds(650,550,100,50);
        btnDouble.setFont(new Font("Comic Sans MS", Font.BOLD, 16));

        btnExit = new JButton("EXIT");
        btnExit.setBounds(930,240,190,50);
        btnExit.setFont(new Font("Comic Sans MS", Font.BOLD, 16));

        //ADD BUTTONS
        frame.add(btnHit);
        frame.add(btnStand);
        frame.add(btnDouble);
        frame.add(btnExit);

        btnExit.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "You have left the casino with" + BlackJackDriver.currentBalance + ".");
                System.exit(0);
            }
        });

        atmosphereComponent = new BJGameComponent(dealerHand, playerHand);
        atmosphereComponent.setBounds(0,0,1130,665);
        frame.add(atmosphereComponent);
        frame.setVisible(true);
    }

    public void startGame() //this method starts the game: the cards are drawn and are printed out.
    {
        for(int i = 0; i < 2; i++) //we add the first two cards on top of the deck to dealer's hand.
        {
            dealerHand.add(deck.getCard(i));
        }
        for(int i = 2; i <4; i++) //we add the third and fourth card on top of the deck to the player's hand.
        {
            playerHand.add(deck.getCard(i));
        }
        for(int i = 0; i < 4; i++) //we then remove these cards from the game. This way, we literally 'drew' the cards and those four cards are no longer in the deck.
        {
            deck.removeCard(0);
        }

        cardComponent = new BJGameComponent(dealerHand, playerHand);
        cardComponent.setBounds(0,0,1130,665);
        frame.add(cardComponent);
        frame.setVisible(true);

        checkHand(dealerHand);
        checkHand(playerHand);

        btnHit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addCard(playerHand);
                checkHand(playerHand);
                if(getSumOfHand(playerHand) < 17 && getSumOfHand(dealerHand) < 17)
                {
                    addCard(dealerHand);
                    checkHand(dealerHand);
                }
            }
        });

        btnStand.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                while(getSumOfHand(dealerHand) < 17)
                {
                    addCard(dealerHand);
                    checkHand(dealerHand);
                }
                if((getSumOfHand(dealerHand)< 21) && getSumOfHand(playerHand) < 21)
                {
                    if(getSumOfHand(playerHand) > getSumOfHand(dealerHand))
                    {
                        faceDown = false;
                        dealerWon = false;
                        JOptionPane.showMessageDialog(frame, "PLAYER WINS");
                        rest();
                        roundOver = true;
                    }
                    else{
                        faceDown = false;
                        JOptionPane.showMessageDialog(frame, "DEALER WINS");
                        rest();
                        roundOver = true;
                    }
                }
            }
        });
    }

    public void checkHand(ArrayList<Card> hand)
    {
        if(hand.equals(playerHand))
        {
            if(getSumOfHand(hand) == 21)
            {
                faceDown = false;
                dealerWon = false;
                JOptionPane.showMessageDialog(frame, "PLAYER WINS - BLACKJACK!");
                rest();
                roundOver = true;
            }
            else if(getSumOfHand(hand) > 21)
            {
                faceDown = false;
                JOptionPane.showMessageDialog(frame, "PLAYER BUSTED - DEALER WINS!");
                rest();
                roundOver = true;
            }
        }
        else
        {
            if(getSumOfHand(hand) == 21)
            {
                faceDown = false;
                JOptionPane.showMessageDialog(frame, "DEALER WINS - BLACKJACK!");
                rest();
                roundOver = true;
            }
            else if(getSumOfHand(hand) > 21)
            {
                faceDown = false;
                dealerWon = false;
                JOptionPane.showMessageDialog(frame, "DEALER BUSTS - PLAYER WINS!");
                rest();
                roundOver = true;
            }
        }
    }


    public void addCard(ArrayList<Card> hand)
    {
        hand.add(deck.getCard(0));
        deck.removeCard(0);
        faceDown = true;
    }

    public boolean hasAceInHand(ArrayList<Card> hand)
    {
        for(int i = 0; i < hand.size(); i++)
        {
            if(hand.get(i).getValue() == 11)
            {
                return true;
            }
        }
        return false;
    }

    public int aceCountInHand(ArrayList<Card> hand)
    {
        int aceCount = 0;
        for(int i = 0; i < hand.size(); i++)
        {
            if(hand.get(i).getValue() == 11)
            {
                aceCount++;
            }
        }
        return aceCount;
    }

    public int getSumWithHighAce(ArrayList<Card> hand)
    {
        int handSum = 0;
        for(int i = 0; i < hand.size(); i++)
        {
            handSum = handSum + hand.get(i).getValue();
        }
        return handSum;
    }

    public int getSumOfHand(ArrayList<Card> hand)
    {
        if(hasAceInHand(hand))
        {
            if(getSumWithHighAce(hand) <= 21)
            {
                return getSumWithHighAce(hand);
            }
            else{
                for(int i = 0; i < aceCountInHand(hand); i++)
                {
                    int sumOfHand = getSumWithHighAce(hand) - (i+1)*10;
                    if(sumOfHand <= 21)
                    {
                        return sumOfHand;
                    }
                }
            }
        }
        else{
            int sumOfHand = 0;
            for(int i = 0; i < hand.size(); i++)
            {
                sumOfHand = sumOfHand + hand.get(i).getValue();
            }
            return sumOfHand;
        }
        return 22;
    }

    public static void rest()
    {
        try{
            Thread.sleep(500);
        }
        catch(InterruptedException e) {}
    }
}
