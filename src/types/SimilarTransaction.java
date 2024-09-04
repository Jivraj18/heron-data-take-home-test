package types;

public class SimilarTransaction {
    private String description;
    private Double amount;
    private int dayOfTransaction;

    public SimilarTransaction(String description, int dayOfTransaction, Double amount) {
        this.description = description;
        this.dayOfTransaction = dayOfTransaction;
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public int getDayOfTransaction() {
        return dayOfTransaction;
    }

    public void setDayOfTransaction(int dayOfTransaction) {
        this.dayOfTransaction = dayOfTransaction;
    }
}