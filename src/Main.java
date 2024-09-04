import types.SimilarTransaction;
import types.Transactions;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        ArrayList<Transactions> transactions = new ArrayList<>();
        // Hard-code the transactions into the ArrayList
        transactions.add(new Transactions("Spotify", -14.99, "2021-01-29"));
        transactions.add(new Transactions("Spotify", -14.99, "2020-12-29"));
        transactions.add(new Transactions("Spotify", -14.99, "2020-11-29"));
        transactions.add(new Transactions("Spotify", -14.99, "2020-10-29"));
        transactions.add(new Transactions("Netflix", -20.00, "2020-02-15"));
        transactions.add(new Transactions("Netflix", -20.00, "2020-03-14"));
        transactions.add(new Transactions("Netflix", -20.00, "2020-04-16"));
        transactions.add(new Transactions("Netflix", -20.00, "2020-05-15"));
        transactions.add(new Transactions("Aug2020 Acme Corp Salary", 4000.00, "2020-08-01"));
        transactions.add(new Transactions("Sep2020 Acme Corp Salary", 4000.00, "2020-09-01"));
        transactions.add(new Transactions("Oct2020 Acme Corp Salary", 4000.00, "2020-10-01"));
        transactions.add(new Transactions("Nov2020 Acme Corp Salary", 4000.00, "2020-11-01"));
        transactions.add(new Transactions("Dec2020 Acme Corp Salary", 4000.00, "2020-12-01"));
        transactions.add(new Transactions("Jan2021 Acme Corp Salary", 5000.00, "2021-01-01"));
        transactions.add(new Transactions("Feb2021 Acme Corp Salary", 5000.00, "2021-02-01"));
        transactions.add(new Transactions("Mar2021 Acme Corp Salary", 5000.00, "2021-03-01"));
        transactions.add(new Transactions("Apr2021 Acme Corp Salary", 5000.00, "2021-04-01"));
        transactions.add(new Transactions("company lunch: burritos", 5.00, "2021-01-05"));
        transactions.add(new Transactions("company lunch: korean", 5.00, "2021-01-12"));
        transactions.add(new Transactions("company lunch: barbeque", 5.00, "2021-01-19"));
        transactions.add(new Transactions("company lunch: hot pot", 5.00, "2021-01-26"));
        transactions.add(new Transactions("company lunch: fondue", 5.00, "2021-02-02"));
        transactions.add(new Transactions("company lunch: burgers", 5.00, "2021-02-09"));
        transactions.add(new Transactions("one-off meal", -100.00, "2021-02-14"));
        transactions.add(new Transactions("special meal", -50.00, "2021-01-05"));
        transactions.add(new Transactions("special holiday", -5000.00, "2020-12-10"));
        transactions.add(new Transactions("treating myself", -3200.00, "2020-11-05"));

        Map<SimilarTransaction, List<Transactions>> grouped = groupByIdentical(transactions);

    }

    // Map of date to
    private static Map<SimilarTransaction, List<Transactions>> groupByIdentical(List<Transactions> transactions) {
        Map<SimilarTransaction, List<Transactions>> groupedTransactions = new HashMap<>();
        // group by transaction
        for (Transactions transaction : transactions) {
            SimilarTransaction similarTransaction = transactionToSimilarTransaction(transaction);
            if(isTransactionSimilar(transaction, similarTransaction)) {
                if (!groupedTransactions.containsKey(similarTransaction)) {
                    groupedTransactions.put(similarTransaction, new ArrayList<>(List.of(transaction)));
                }
                groupedTransactions.get(similarTransaction).add(transaction);
            }
        }
        return groupedTransactions;
    }

    private static boolean isTransactionSimilar(Transactions transaction, SimilarTransaction similarTransaction) {
        if(transactionToSimilarTransaction(transaction) == similarTransaction) return true;
        return false;
    }

    private static SimilarTransaction transactionToSimilarTransaction(Transactions transaction) {
        String description = transaction.getDescription();
        Double amount = transaction.getAmount();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(transaction.getDate(), formatter);
        int dayOfMonth = date.getDayOfMonth();
        return new SimilarTransaction(description, dayOfMonth, amount);
    }
}