package com.company;

import java.awt.*;
import java.awt.event.InputEvent;

/**
 * Created by Joel.Bartlett18 on 10/12/2017.
 */
public class Mouse {

    private Robot bot = null;

    private int leftMask = InputEvent.BUTTON1_MASK;
    private int rightMask = InputEvent.BUTTON3_MASK;


    public Mouse() throws AWTException { bot = new Robot(); }

    public void performActions(boolean left, boolean right, double x, double y) throws AWTException {
        handleLeft(left);
        handleRight(right);
        move((int) x,(int) y);
    }

    private void handleLeft(boolean state) {
        if(state) {
            bot.mousePress(leftMask);
            System.out.println("Left clicked");
        } else {
            bot.mouseRelease(leftMask);
            System.out.println("Left unclicked");
        }
    }

    private void handleRight(boolean state) {
        if(state) {
            bot.mousePress(rightMask);
        } else {
            bot.mouseRelease(rightMask);
        }
    }

    private void move(int x, int y) throws AWTException {
        Point currentPos = MouseInfo.getPointerInfo().getLocation();
        int currentX = (int) currentPos.getX();
        int currentY = (int) currentPos.getY();
        bot.mouseMove(currentX + x,currentY - y);
    }

}
