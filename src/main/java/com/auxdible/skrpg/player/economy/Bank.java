package com.auxdible.skrpg.player.economy;

public class Bank {
    private int credits;
    private BankLevel level;
    public Bank(int credits, BankLevel level) {
        this.credits = credits;
        this.level = level;
    }
    public int getCredits() { return credits; }
    public BankLevel getLevel() { return level; }
    public void upgrade(BankLevel bankLevel) { this.level = bankLevel; }
    public void deposit(int credits) { this.credits = this.credits + credits; }
    public void withdraw(int credits) { this.credits = this.credits - credits; }
}
