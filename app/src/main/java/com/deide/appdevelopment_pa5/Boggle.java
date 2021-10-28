/**
 * This program plays a single player version of the game Boggle
 * CPSC 312-01. Fall 2021
 * Programming Assignment 5
 * No sources to cite
 *
 * @ConnorDeide
 * @Version v1.0 10/27/2021
 */

package com.deide.appdevelopment_pa5;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

public class Boggle {
    private static final String[] boggleDice = {"AAEEGN", "ABBJOO", "ACHOPS", "AFFKPS",
            "AOOTTW", "CIMOTU", "DEILRX", "DELRVY", "DISTTY", "EEGHNW", "EEINSU", "EHRTVW",
            "EIOSST", "ELRTTY", "HIMNUQu", "HLNNRZ"};
    private BoggleDice[] dieArray = new BoggleDice[16];
    private HashMap<Integer, Integer> pointMap;
    private String[][] diceGrid = new String[4][4];
    private ArrayList<String> correctWords;
    private ArrayList<String> usedWords = new ArrayList<>();
    private StringBuilder curString = new StringBuilder();
    private int totalPoints;
    private Context context;

    public Boggle(Context context) {
        this.context = context;
    }

    //Setters
    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }
    public void setCurString(StringBuilder curString) {
        this.curString = curString;
    }
    //Getters
    public int getTotalPoints() {
        return totalPoints;
    }
    public StringBuilder getCurString() {
        return curString;
    }
    public Integer getPointMap(int key) {
        return pointMap.get(key);
    }
    public ArrayList<String> getUsedWords() {
        return usedWords;
    }

    /**
     * Method sets fields to be used in the game
     * @param activity
     */
    public void setGame(MainActivity activity) {
        //Load dictionary
        loadWordsFromFile();
        //set the Grid and pointMap
        shakeGrid(activity);
        setPointMap();
    }

    /**
     * Method resets fields for a new game and creates a new Boggle object
     * @param activity
     */
    public void resetGame(MainActivity activity) {
        activity.seconds = 0;
        activity.prevButton = null;
        activity.disableButtons();
        for(int i = 0; i < 4; ++i) {
            for(int j = 0; j < 4; ++j) {
                activity.buttonGrid[i][j].setText(null);
            }
        }
        activity.boggle = new Boggle(activity);
        activity.clearButton.setEnabled(false);
        activity.submitButton.setEnabled(false);
    }

    /**
     * Method checks that the button that was clicked is adjacent to the previous button
     * if there is no previous button then it sets the previous button to the current one
     * @param activity
     * @param buttonClicked
     * @return
     */
    public boolean checkAdjacent(MainActivity activity, Button buttonClicked) {
        //If prevButton has not been set then set it and do nothing
        if(activity.prevButton == null) {
            activity.prevButton = buttonClicked;
            return true;
        } else {
            //Find coordinates of previous button
            int prevButtonRow = 0;
            int prevButtonColumn = 0;
            boolean flag = false;
            for(int i = 0; i < 4; ++i) {
                for(int j = 0; j < 4; ++j) {
                    if(activity.buttonGrid[i][j] == activity.prevButton) {
                        prevButtonRow = i;
                        prevButtonColumn = j;
                        flag = true;
                        break;
                    }
                }
                if(flag) {
                    break;
                }
            }
            //Find coordinates of the current button
            int curButtonRow = 0;
            int curButtonColumn = 0;
            flag = false;
            for(int i = 0; i < 4; ++i) {
                for(int j = 0; j < 4; ++j) {
                    if(activity.buttonGrid[i][j] == buttonClicked) {
                        curButtonRow = i;
                        curButtonColumn = j;
                        flag = true;
                        break;
                    }
                }
                if(flag) {
                    break;
                }
            }
            //Update prevButton
            activity.prevButton = buttonClicked;
            //Check that button is adjacent using the coordinates
            if(prevButtonRow - curButtonRow == 1 || prevButtonRow - curButtonRow == -1 || prevButtonRow - curButtonRow == 0) {
                if(prevButtonColumn - curButtonColumn == 1 || prevButtonColumn - curButtonColumn == -1 || prevButtonColumn - curButtonColumn == 0) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Method checks if the curString has already been played
     * if it has been used it returns false
     * if it has not been used it adds the string to usedWords and returns true
     * @return
     */
    public boolean checkUsedWords() {
        for(int i = 0; i < usedWords.size(); ++i) {
            if(curString.toString().equals(usedWords.get(i))) { //If word has already been used
                return false;
            }
        }
        usedWords.add(curString.toString());
        return true;
    }

    /**
     * Method checks if the submitted word is a real word
     * @return
     */
    public boolean checkDictionary() {
        for(int i = 0; i < correctWords.size() - 1; ++i) {
            if(curString.toString().equals(correctWords.get(i).toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method adds the buttons text to curString
     * @param button
     */
    public void appendCurWord(Button button) {
        curString.append(button.getText());
    }

    /**
     * Method fills the HashMap pointMap
     */
    public void setPointMap() {
        pointMap = new HashMap<>();
        pointMap.put(3, 1);
        pointMap.put(4, 1);
        pointMap.put(5, 2);
        pointMap.put(6, 3);
        pointMap.put(7, 5);
        pointMap.put(8, 11);
    }

    /**
     * Method instantiates 16 dice, creating a sideUp for each dice in their constructor
     * then fills the 2D array diceGrid with these values
     */
    public void setDiceGrid() {
        //Shuffle String[] boggleDice
        Collections.shuffle(Arrays.asList(boggleDice));
        //Fill dieArray with dice
        for(int i = 0; i < 16; ++i) {
            dieArray[i] = new BoggleDice(boggleDice[i]);
        }
        //Fill diceGrid with the sideUp of each elem in dieArray
        int idx = 0;
        for(int i = 0; i < 4; ++i) {
            for(int j = 0; j < 4; ++j) {
                diceGrid[i][j] = dieArray[idx].toString();
                idx++;
            }
        }
    }

    /**
     * Method calls set Grid and fills the activity.buttonGrid with the boggleDices sideUp
     * @param activity
     */
    private void shakeGrid(MainActivity activity) {
        //fill the dice Grid to be used
        setDiceGrid();
        //setText of all buttons
        for(int i = 0; i < 4; ++i) {
            for(int j = 0; j < 4; ++j) {
                activity.buttonGrid[i][j].setText(diceGrid[i][j]);
            }
        }
    }

    /**
     * Method fills correctWords with words from words_alpha.txt
     */
    public void loadWordsFromFile() {
        correctWords = new ArrayList<>();
        try {
            InputStream in = context.getResources().openRawResource(R.raw.words_alpha);
            BufferedReader is = new BufferedReader(new InputStreamReader(in, "UTF8"));
            String line;
            do {
                line = is.readLine();
                correctWords.add(line);
            } while (line !=null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
