package com.deide.appdevelopment_pa5;

public class Board {
    private static final String[] boggleDice = {"AAEEGN", "ABBJOO", "ACHOPS", "AFFKPS",
            "AOOTTW", "CIMOTU", "DEILRX", "DELRVY", "DISTTY", "EEGHNW", "EEINSU", "EHRTVW",
            "EIOSST", "ELRTTY", "HIMNUQu", "HLNNRZ"};
    protected BoggleDice[] dieArray = new BoggleDice[16];

    /**
     * Method instantiates 16 dice, creating a sideUp for each dice in their constructor
     */
    public void shakeGrid() {
        for(int i = 0; i < 16; ++i) {
            dieArray[i] = new BoggleDice(boggleDice[i]);
        }
    }
}
