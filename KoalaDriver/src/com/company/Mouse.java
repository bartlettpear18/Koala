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

    private final int X_SCALAR = 10;
    private final int Y_SCALAR = 10;

    public Mouse() throws AWTException { bot = new Robot(); }

    public void move(int x, int y) throws AWTException {
        Point currentPos = MouseInfo.getPointerInfo().getLocation();
        int currentX = (int) currentPos.getX();
        int currentY = (int) currentPos.getY();
        bot.mouseMove(currentX + x,currentY - y);
    }

    public void pressLeft() {
        bot.mousePress(leftMask);
    }

    public void releaseLeft() { bot.mouseRelease(leftMask);}

    public void pressRight() { bot.mousePress(rightMask);}

    public void releasRight() { bot.mouseRelease(rightMask);}
}
