/**
 * This program plays a single player version of the game Boggle
 * No sources to cite
 *
 * @ConnorDeide
 * @Version v1.0 10/27/2021
 */

package com.deide.appdevelopment_pa5;

import androidx.annotation.NonNull;

import java.util.Random;

public class BoggleDice {
    private String sideUp;
    private String[] sidesArray = new String[6];

    //DVC
    public BoggleDice() { }
    //EVC
    public BoggleDice(String dieSides) {
        for(int i = 0; i < 6; ++i) {
            if(dieSides.charAt(i) == 'Q')
                sidesArray[i] = "QU";
            else {
                sidesArray[i] = String.valueOf(dieSides.charAt(i));
            }
        }
        roll();
    }
    //Getter
    public String getSideUp() {
        return this.sideUp;
    }

    /**
     * Rolls the die to select a sideUp
     */
    public void roll() {
        Random rand = new Random();
        this.sideUp = sidesArray[rand.nextInt(6)];
    }

    /**
     * Method overrides toString() to return sideUp
     * @return
     */
    @NonNull
    @Override
    public String toString() {
        return this.sideUp;
    }
}
