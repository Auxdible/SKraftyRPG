package com.auxdible.skrpg.player.economy;

public class Bank {
    private double credits;
    private BankLevel level;
    public Bank(double credits, BankLevel level) {
        this.credits = credits;
        this.level = level;
    }
    public double getCredits() { return credits; }
    public BankLevel getLevel() { return level; }
    public void upgrade(BankLevel bankLevel) { this.level = bankLevel; }
    public void deposit(double credits) { this.credits = this.credits + credits; }
    public void withdraw(double credits) { this.credits = this.credits - credits; }
}
