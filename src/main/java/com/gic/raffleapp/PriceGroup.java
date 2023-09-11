package com.gic.raffleapp;

public enum PriceGroup {
    TWO(2,10),THREE(3,15),FOUR(4,25),FIVE(5,50);

    PriceGroup(int winningNumbers, int rewardPercentage) {
        this.winningNumbers = winningNumbers;
        this.rewardPercentage = rewardPercentage;
    }

    public int getWinningNumbers() {
        return winningNumbers;
    }

    public int getRewardPercentage() {
        return rewardPercentage;
    }

    private final int winningNumbers;
    private final int rewardPercentage;
}
