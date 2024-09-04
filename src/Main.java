import types.SimilarTransaction;
import types.Transactions;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Main {
    static int DAY_GRACE_PERIOD = 3;
    static double SIMILARITY_PERCENTAGE_CAP = 0.7;

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

//        Map<SimilarTransaction, List<Transactions>> grouped = groupByIdentical(transactions);

    }

    // Map of date to
    private static Map<String, List<Transactions>> groupByIdentical(List<Transactions> transactions) {




        Map<String, List<Transactions>> groupedTransactions = new HashMap<>();
//        // group by transaction
//        for (Transactions transaction : transactions) {
//            if (!groupedTransactions.containsKey(transaction.getDescription()) {
//                groupedTransactions.put(transaction, new ArrayList<>(List.of(transaction)));
//            }
//            SimilarTransaction similarTransaction = transactionToSimilarTransaction(transaction);
//            if(isTransactionSimilar(transaction, similarTransaction)) {
//                if (!groupedTransactions.containsKey(similarTransaction)) {
//
//                }
//                groupedTransactions.get(similarTransaction).add(transaction);
//            }
//        }
        return groupedTransactions;
    }

    private static boolean isTransactionSimilar(Transactions newTransaction, Transactions oldTransaction) {
        if(newTransaction == oldTransaction) return true;
        if(isSimilarDescription(newTransaction, oldTransaction) &&
                Objects.equals(oldTransaction.getAmount(), newTransaction.getAmount()) &&
                (isMonthly(newTransaction, oldTransaction) || isWeekly(newTransaction, oldTransaction))
        ) return true;
        return false;
    }

    private static boolean isMonthly(Transactions newTransaction, Transactions oldTransaction) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate newDate = LocalDate.parse(newTransaction.getDate(), formatter);
        LocalDate oldDate = LocalDate.parse(oldTransaction.getDate(), formatter);

        long monthsBetween = ChronoUnit.MONTHS.between(newDate, oldDate);
        long daysBetween = ChronoUnit.DAYS.between(newDate, oldDate);

        boolean isOneMonth = monthsBetween == 1 || monthsBetween==-1;
        boolean isGracePeriod = Math.abs(daysBetween) <= 31 + 3;

        return isOneMonth && isGracePeriod;
    }
    private static boolean isWeekly(Transactions newTransaction, Transactions oldTransaction) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate newDate = LocalDate.parse(newTransaction.getDate(), formatter);
        LocalDate oldDate = LocalDate.parse(oldTransaction.getDate(), formatter);

        long monthsBetween = ChronoUnit.WEEKS.between(newDate, oldDate);
        long daysBetween = ChronoUnit.DAYS.between(newDate, oldDate);

        boolean isOneMonth = monthsBetween == 1 || monthsBetween==-1;
        boolean isGracePeriod = Math.abs(daysBetween) <= 7 + 3;

        return isOneMonth && isGracePeriod;
    }

    private static boolean isSimilarDescription(Transactions newTransaction, Transactions oldTransaction) {
        String newDescription = newTransaction.getDescription();
        String oldDescription = oldTransaction.getDescription();
        int distance = levenshteinDistance(newDescription, oldDescription);
        double similarityScore = 1.0 - (double) distance / Math.max(newDescription.length(), oldDescription.length());;
        return similarityScore > SIMILARITY_PERCENTAGE_CAP;
    }

    private static int levenshteinDistance(String s1, String s2) {
        int lenS1 = s1.length();
        int lenS2 = s2.length();
        int[][] dp = new int[lenS1 + 1][lenS2 + 1];

        for (int i = 0; i <= lenS1; i++) {
            for (int j = 0; j <= lenS2; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                    dp[i][j] = Math.min(
                            Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                            dp[i - 1][j - 1] + cost
                    );
                }
            }
        }

        return dp[lenS1][lenS2];
    }
}