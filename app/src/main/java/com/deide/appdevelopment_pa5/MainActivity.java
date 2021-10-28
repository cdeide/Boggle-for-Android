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

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //Fields for the timer
    Handler handler = null;
    int seconds = 0;
    //Button fields
    Button startButton;
    Button clearButton;
    Button submitButton;
    Button[][] buttonGrid = new Button[4][4];
    //Used for checkAdjacent
    Button prevButton;
    //Class for app model
    Boggle boggle = new Boggle(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Method finds all of the button fields by id
        getButtonElements();
        //These buttons are initially disabled
        clearButton.setEnabled(false);
        submitButton.setEnabled(false);

        //Timer
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                updateSeconds(seconds + 1);
                handler.postDelayed(this, 1000);
                //Check time up
                if(seconds >= 90) {
                    //Stop timer, output endGameMessage and reset game
                    stopTimer(this);
                    TextView endGameMessage = findViewById(R.id.gameStateTextView);
                    endGameMessage.setText("GAME OVER: You found " + boggle.getUsedWords().size() + " word(s)");
                    boggle.resetGame(MainActivity.this);
                }
            }
        };

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start timer
                if(handler == null) {
                    handler = new Handler();
                    handler.postDelayed(runnable, 1000);
                    startButton.setEnabled(false);
                }
                clearButton.setEnabled(true);
                submitButton.setEnabled(true);
                //Change gameState text
                TextView currentWord = findViewById(R.id.gameStateTextView);
                currentWord.setText("Current Word:");
                //Start game
                boggle.setGame(MainActivity.this);
                //Enable buttons
                enableButtons();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableButtons();
                boggle.setCurString(new StringBuilder());
                prevButton = null;
                TextView currentWord = findViewById(R.id.gameStateTextView);
                currentWord.setText("Current Word: " + boggle.getCurString());
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView gameMessage = findViewById(R.id.gameStateTextView);
                //Check that word is long enough has not already been used and is a real word
                if(boggle.getCurString().length() < 3) {
                    gameMessage.setText("Word must be at least 3 letters");
                    boggle.setCurString(new StringBuilder());
                    prevButton = null;
                    enableButtons();
                    return;
                }
                if(!boggle.checkDictionary()) {
                    gameMessage.setText("Not a word");
                    boggle.setCurString(new StringBuilder());
                    prevButton = null;
                    enableButtons();
                    return;
                }
                if(!boggle.checkUsedWords()) {
                    gameMessage.setText("You have already used this word");
                    boggle.setCurString(new StringBuilder());
                    prevButton = null;
                    enableButtons();
                    return;
                }
                else {
                    //Update the points
                    int points = boggle.getPointMap(boggle.getCurString().length());
                    updatePoints(points);
                    boggle.setCurString(new StringBuilder());
                    gameMessage.setText("Current Word: " + boggle.getCurString());
                    prevButton = null;
                    enableButtons();
                }
            }
        });

        //Set onClickListeners for all buttons in buttonGrid
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                buttonGrid[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Check that user can click this tile
                        boolean valid = boggle.checkAdjacent(MainActivity.this, (Button) view);
                        if(!valid) {
                            TextView invalidButton = findViewById(R.id.gameStateTextView);
                            invalidButton.setText("Must select adjacent tiles");
                            //Reset current string
                            boggle.setCurString(new StringBuilder());
                            enableButtons();
                        } else {
                            //Disable button
                            view.setEnabled(false);
                            //Add string to the current word and update the gameStateTextView
                            boggle.appendCurWord((Button) view);
                            TextView currentWord = findViewById(R.id.gameStateTextView);
                            currentWord.setText("Current Word: " + boggle.getCurString());
                        }
                    }
                });
            }
        }
    }

    /**
     * Method updates the points seen in the countsPointsTextView
     * @param newPoints
     */
    private void updatePoints(int newPoints) {
        boggle.setTotalPoints(boggle.getTotalPoints() + newPoints);
        TextView pointsTextView = findViewById(R.id.countPointsTextView);
        pointsTextView.setText("" + boggle.getTotalPoints());
    }

    /**
     * Method updates the seconds seen in the timerTextView
     * @param newSeconds
     */
    private void updateSeconds(int newSeconds) {
        seconds = newSeconds;
        TextView timerTextView = findViewById(R.id.timerTextView);
        timerTextView.setText("" + seconds);
    }

    /**
     * Method stops the timer
     * @param runnable
     */
    private void stopTimer(Runnable runnable) {
        if (handler != null) { //If timer is running
            handler.removeCallbacks(runnable);
            handler = null;
            startButton.setEnabled(true);
        }
    }

    /**
     * Method enables all buttons
     */
    private void enableButtons() {
        for(int i = 0; i < 4; ++i) {
            for(int j = 0; j < 4; ++j) {
                buttonGrid[i][j].setEnabled(true);
            }
        }
    }

    /**
     * Method disables all buttons
     */
    public void disableButtons() {
        for(int i = 0; i < 4; ++i) {
            for(int j = 0; j < 4; ++j) {
                buttonGrid[i][j].setEnabled(false);
            }
        }
    }

    /**
     * Button finds all button elements by id
     */
    private void getButtonElements() {
        startButton = findViewById(R.id.buttonStart);
        clearButton = findViewById(R.id.buttonClear);
        submitButton = findViewById(R.id.buttonSubmit);

        buttonGrid = new Button[4][4];
        buttonGrid[0][0] = findViewById(R.id.button00);
        buttonGrid[0][1] = findViewById(R.id.button01);
        buttonGrid[0][2] = findViewById(R.id.button02);
        buttonGrid[0][3] = findViewById(R.id.button03);
        buttonGrid[1][0] = findViewById(R.id.button10);
        buttonGrid[1][1] = findViewById(R.id.button11);
        buttonGrid[1][2] = findViewById(R.id.button12);
        buttonGrid[1][3] = findViewById(R.id.button13);
        buttonGrid[2][0] = findViewById(R.id.button20);
        buttonGrid[2][1] = findViewById(R.id.button21);
        buttonGrid[2][2] = findViewById(R.id.button22);
        buttonGrid[2][3] = findViewById(R.id.button23);
        buttonGrid[3][0] = findViewById(R.id.button30);
        buttonGrid[3][1] = findViewById(R.id.button31);
        buttonGrid[3][2] = findViewById(R.id.button32);
        buttonGrid[3][3] = findViewById(R.id.button33);
    }
};


