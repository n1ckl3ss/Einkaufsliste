package de.gbsschulen;

import java.util.Objects;

public class ArticleEntry {
    private String designation;
    private double price;
    private int amount;

    public ArticleEntry() {
    }

    public ArticleEntry(String designation, double price, int amount) {
        this.designation = designation;
        this.price = price;
        this.amount = amount;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleEntry that = (ArticleEntry) o;
        return Double.compare(that.price, price) == 0 &&
                amount == that.amount &&
                Objects.equals(designation, that.designation);
    }

    @Override
    public int hashCode() {

        return Objects.hash(designation, price, amount);
    }

    @Override
    public String toString() {
        return this.designation;
    }
}
