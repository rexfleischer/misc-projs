/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.output;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 *
 * @author REx
 */
public class SystemOutput 
{
    private Robot robot;
    
    private int actionPause;
    
    public SystemOutput(Robot robot, int actionPause) throws AWTException
    {
        this.robot = robot;
        this.actionPause = actionPause;
    }
    
    public void leftClick(Rectangle target)
    {
        if (target == null)
        {
            throw new NullPointerException("target");
        }
        leftClick(
                target.x + (target.width / 2), 
                target.y + (target.height / 2));
    }
    
    public void leftClick(int x, int y)
    {
        if (x < 0)
        {
            throw new NullPointerException("x cannot be less then 0");
        }
        if (y < 0)
        {
            throw new NullPointerException("y cannot be less then 0");
        }
        robot.mouseMove(x, y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(actionPause);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }
    
    public void targetOnMouse(Rectangle target)
    {
        if (target == null)
        {
            throw new NullPointerException("target");
        }
        robot.mouseMove(
                target.x + (target.width / 2), 
                target.y + (target.height / 2));
        robot.delay(actionPause);
    }
    
    public void pause(int milli)
    {
        if (milli < 1)
        {
            throw new NullPointerException("milli");
        }
        robot.delay(milli);
    }
    
    public void typeReturn()
    {
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.delay(actionPause);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.delay(actionPause);
    }
    
    public void typeString(String output)
    {
        if (output == null)
        {
            throw new NullPointerException("output");
        }
        String outputString = output.toUpperCase();
        for(int i = 0; i < outputString.length(); i++)
        {
            switch(outputString.charAt(i))
            {
                case ' ':
                    robot.keyPress(KeyEvent.VK_SPACE);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_SPACE);
                    robot.delay(actionPause);
                    break;
                case '0':
                    robot.keyPress(KeyEvent.VK_0);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_0);
                    robot.delay(actionPause);
                    break;
                case '1':
                    robot.keyPress(KeyEvent.VK_1);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_1);
                    robot.delay(actionPause);
                    break;
                case '2':
                    robot.keyPress(KeyEvent.VK_2);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_2);
                    robot.delay(actionPause);
                    break;
                case '3':
                    robot.keyPress(KeyEvent.VK_3);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_3);
                    robot.delay(actionPause);
                    break;
                case '4':
                    robot.keyPress(KeyEvent.VK_4);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_4);
                    robot.delay(actionPause);
                    break;
                case '5':
                    robot.keyPress(KeyEvent.VK_5);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_5);
                    robot.delay(actionPause);
                    break;
                case '6':
                    robot.keyPress(KeyEvent.VK_6);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_6);
                    robot.delay(actionPause);
                    break;
                case '7':
                    robot.keyPress(KeyEvent.VK_7);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_7);
                    robot.delay(actionPause);
                    break;
                case '8':
                    robot.keyPress(KeyEvent.VK_8);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_8);
                    robot.delay(actionPause);
                    break;
                case '9':
                    robot.keyPress(KeyEvent.VK_9);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_9);
                    robot.delay(actionPause);
                    break;
                case 'A':
                    robot.keyPress(KeyEvent.VK_A);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_A);
                    robot.delay(actionPause);
                    break;
                case 'B':
                    robot.keyPress(KeyEvent.VK_B);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_B);
                    robot.delay(actionPause);
                    break;
                case 'C':
                    robot.keyPress(KeyEvent.VK_C);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_C);
                    robot.delay(actionPause);
                    break;
                case 'D':
                    robot.keyPress(KeyEvent.VK_D);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_D);
                    robot.delay(actionPause);
                    break;
                case 'E':
                    robot.keyPress(KeyEvent.VK_E);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_E);
                    robot.delay(actionPause);
                    break;
                case 'F':
                    robot.keyPress(KeyEvent.VK_F);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_F);
                    robot.delay(actionPause);
                    break;
                case 'G':
                    robot.keyPress(KeyEvent.VK_G);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_G);
                    robot.delay(actionPause);
                    break;
                case 'H':
                    robot.keyPress(KeyEvent.VK_H);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_H);
                    robot.delay(actionPause);
                    break;
                case 'I':
                    robot.keyPress(KeyEvent.VK_I);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_I);
                    robot.delay(actionPause);
                    break;
                case 'J':
                    robot.keyPress(KeyEvent.VK_J);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_J);
                    robot.delay(actionPause);
                    break;
                case 'K':
                    robot.keyPress(KeyEvent.VK_K);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_K);
                    robot.delay(actionPause);
                    break;
                case 'L':
                    robot.keyPress(KeyEvent.VK_L);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_L);
                    robot.delay(actionPause);
                    break;
                case 'M':
                    robot.keyPress(KeyEvent.VK_M);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_M);
                    robot.delay(actionPause);
                    break;
                case 'N':
                    robot.keyPress(KeyEvent.VK_N);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_N);
                    robot.delay(actionPause);
                    break;
                case 'O':
                    robot.keyPress(KeyEvent.VK_O);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_O);
                    robot.delay(actionPause);
                    break;
                case 'P':
                    robot.keyPress(KeyEvent.VK_P);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_P);
                    robot.delay(actionPause);
                    break;
                case 'Q':
                    robot.keyPress(KeyEvent.VK_Q);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_Q);
                    robot.delay(actionPause);
                    break;
                case 'R':
                    robot.keyPress(KeyEvent.VK_R);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_R);
                    robot.delay(actionPause);
                    break;
                case 'S':
                    robot.keyPress(KeyEvent.VK_S);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_S);
                    robot.delay(actionPause);
                    break;
                case 'T':
                    robot.keyPress(KeyEvent.VK_T);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_T);
                    robot.delay(actionPause);
                    break;
                case 'U':
                    robot.keyPress(KeyEvent.VK_U);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_U);
                    robot.delay(actionPause);
                    break;
                case 'V':
                    robot.keyPress(KeyEvent.VK_V);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_V);
                    robot.delay(actionPause);
                    break;
                case 'W':
                    robot.keyPress(KeyEvent.VK_W);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_W);
                    robot.delay(actionPause);
                    break;
                case 'X':
                    robot.keyPress(KeyEvent.VK_X);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_X);
                    robot.delay(actionPause);
                    break;
                case 'Y':
                    robot.keyPress(KeyEvent.VK_Y);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_Y);
                    robot.delay(actionPause);
                    break;
                case 'Z':
                    robot.keyPress(KeyEvent.VK_Z);
                    robot.delay(actionPause);
                    robot.keyRelease(KeyEvent.VK_Z);
                    robot.delay(actionPause);
                    break;
                default:
                    break;
            }
        }
    }
}
