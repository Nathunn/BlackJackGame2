package com.BlackJackGame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BJOptionsComponent extends JComponent implements ActionListener {

private JButton btnPlay = new JButton("PLAY");
private JButton btnExit = new JButton("EXIT");
private static BufferedImage backgroundImage;

    public BJOptionsComponent()//in the default constructor of this class,
    {
        //we add action listeners to all buttons to control what will happen when the user clicks them.
        btnPlay.addActionListener(this);
        btnExit.addActionListener(this);
    }

    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;

        try
        {
            backgroundImage = ImageIO.read(new File("images/background.jpg"));
        }
        catch (IOException e) {}

        g2.drawImage(backgroundImage, 0, 0, null);

        g2.setFont(new Font("Comic Sans MS", Font.BOLD, 100));
        g2.setColor(Color.GREEN);
        g2.drawString("Welcome",380, 100);
        g2.drawString("to",380, 100);
        g2.drawString("BLACKJACK!",380, 100);

        g2.setFont(new Font("Arial", Font.BOLD, 30));
        g2.drawString("L", 220, 580);

        btnPlay.setBounds(500,300,150,80);
        btnExit.setBounds(500,400,150,80);

        btnPlay.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
        btnExit.setFont(new Font("Comic Sans MS", Font.BOLD, 40));

        super.add(btnPlay);//super refers to the JComponent. Thus, with these codes, we add the four buttons to the component
        super.add(btnExit);
    }

    public void actionPerformed(ActionEvent e)
    {
        JButton selectedButton = (JButton)e.getSource();

        if(selectedButton == btnExit)
        {
            System.exit(0);
        }
        else if(selectedButton == btnPlay)
        {
            BlackJackDriver.currentState = BlackJackDriver.STATE.GAME;
            BlackJackDriver.menuFrame.dispose();
            BlackJackDriver.gameRefeshThread.start();
            BlackJackDriver.gameCheckThread.start();

        }

    }
}
