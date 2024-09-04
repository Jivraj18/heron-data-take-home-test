package types;

import java.util.Objects;

public class Transactions {
    private String description;
    private Double amount ;
    private String date;

    // Constructor
    public Transactions(String description, Double amount, String date) {
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "description='" + description + '\'' +
                ", amount=" + amount +
                ", date='" + date + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transactions that = (Transactions) o;
        return Double.compare(that.amount, amount) == 0 && Objects.equals(description, that.description) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, amount, date);
    }
}


/*

    public static List<List<Transactions>> groupRecurringDescriptions(List<Transactions> transactions) {
        List<List<Transactions>> result = new ArrayList<>();
        Map<String, List<Transactions>> descriptionMap = new HashMap<>();

        // Group transactions by description
        for (Transactions transaction : transactions) {
            descriptionMap.computeIfAbsent(transaction.getDescription(), k -> new ArrayList<>()).add(transaction);
        }

        // For each group, check if the transactions are recurring
        for (List<Transactions> group : descriptionMap.values()) {
            List<Transactions> recurringGroup = findRecurringTransactions(group);
            if (!recurringGroup.isEmpty()) {
                result.add(recurringGroup);
            }
        }

        return result;
    }

    public static List<Transactions> findRecurringTransactions(List<Transactions> transactions) {
        List<Transactions> recurringTransactions = new ArrayList<>();
        Set<Transactions> visited = new HashSet<>();

        for (int i = 0; i < transactions.size(); i++) {
            Transactions currentTransaction = transactions.get(i);

            if (!visited.contains(currentTransaction)) {
                List<Transactions> group = new ArrayList<>();
                group.add(currentTransaction);
                visited.add(currentTransaction);

                for (int j = i + 1; j < transactions.size(); j++) {
                    Transactions otherTransaction = transactions.get(j);

                    if (!visited.contains(otherTransaction) &&
                            areDatesRecurring(currentTransaction.getDate(), otherTransaction.getDate())) {

                        group.add(otherTransaction);
                        visited.add(otherTransaction);
                    }
                }

                // Add to result if it has more than one transaction
                if (group.size() > 1) {
                    recurringTransactions.addAll(group);
                }
            }
        }

        return recurringTransactions;
    }

    public static boolean areDatesRecurring(String date1, String date2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate1 = LocalDate.parse(date1, formatter);
        LocalDate localDate2 = LocalDate.parse(date2, formatter);

        long daysBetween = ChronoUnit.DAYS.between(localDate1, localDate2);

        // Grace period allows +/- 2 days
        int gracePeriod = 2;

        // Check if the transactions are about a month or a week apart, with grace period
        return (isWithinRange(daysBetween, 28, 31, gracePeriod)) ||
                (isWithinRange(daysBetween, 7, 7, gracePeriod));
    }

    private static boolean isWithinRange(long daysBetween, int min, int max, int gracePeriod) {
        return (daysBetween >= (min - gracePeriod) && daysBetween <= (max + gracePeriod));
    }
 */