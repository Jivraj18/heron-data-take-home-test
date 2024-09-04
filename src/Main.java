import types.Transaction;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    static int DAY_GRACE_PERIOD = 3;
    static int GRACE_AMOUNT = 1000;
    static double SIMILARITY_PERCENTAGE_CAP = 0.7;

    public static void main(String[] args) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        // Hard-code the transactions into the ArrayList
        transactions.add(new Transaction("Spotify", -14.99, "2021-01-29"));
        transactions.add(new Transaction("Spotify", -14.99, "2020-12-29"));
        transactions.add(new Transaction("Spotify", -14.99, "2020-11-29"));
        transactions.add(new Transaction("Spotify", -14.99, "2020-10-29"));
        transactions.add(new Transaction("Netflix", -20.00, "2020-02-15"));
        transactions.add(new Transaction("Netflix", -20.00, "2020-03-14"));
        transactions.add(new Transaction("Netflix", -20.00, "2020-04-16"));
        transactions.add(new Transaction("Netflix", -20.00, "2020-05-15"));
        transactions.add(new Transaction("Aug2020 Acme Corp Salary", 4000.00, "2020-08-01"));
        transactions.add(new Transaction("Sep2020 Acme Corp Salary", 4000.00, "2020-09-01"));
        transactions.add(new Transaction("Oct2020 Acme Corp Salary", 4000.00, "2020-10-01"));
        transactions.add(new Transaction("Nov2020 Acme Corp Salary", 4000.00, "2020-11-01"));
        transactions.add(new Transaction("Dec2020 Acme Corp Salary", 4000.00, "2020-12-01"));
        transactions.add(new Transaction("Jan2021 Acme Corp Salary", 5000.00, "2021-01-01"));
        transactions.add(new Transaction("Feb2021 Acme Corp Salary", 5000.00, "2021-02-01"));
        transactions.add(new Transaction("Mar2021 Acme Corp Salary", 5000.00, "2021-03-01"));
        transactions.add(new Transaction("Apr2021 Acme Corp Salary", 5000.00, "2021-04-01"));
        transactions.add(new Transaction("company lunch: burritos", 5.00, "2021-01-05"));
        transactions.add(new Transaction("company lunch: korean", 5.00, "2021-01-12"));
        transactions.add(new Transaction("company lunch: barbeque", 5.00, "2021-01-19"));
        transactions.add(new Transaction("company lunch: hot pot", 5.00, "2021-01-26"));
        transactions.add(new Transaction("company lunch: fondue", 5.00, "2021-02-02"));
        transactions.add(new Transaction("company lunch: burgers", 5.00, "2021-02-09"));
        transactions.add(new Transaction("one-off meal", -100.00, "2021-02-14"));
        transactions.add(new Transaction("special meal", -50.00, "2021-01-05"));
        transactions.add(new Transaction("special holiday", -5000.00, "2020-12-10"));
        transactions.add(new Transaction("treating myself", -3200.00, "2020-11-05"));

        Map<String, List<Transaction>> grouped = initGroups(transactions)
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().size() > 1) // Filtering groups that have more than 1 transaction
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        printTransactionsMap(grouped);

    }

    private static Map<String, List<Transaction>> initGroups(List<Transaction> transactions) {
        Map<String, List<Transaction>> groupedTransactions = new HashMap<>();

        for (int i = 0; i < transactions.size(); i++) {
            Transaction currentTransaction = transactions.get(i);
            // Avoid adding similar group names
            boolean canAdd = true;
            for(Map.Entry<String, List<Transaction>> transaction : groupedTransactions.entrySet()) {
                if(isSimilarString(currentTransaction.getDescription(), transaction.getKey())) canAdd = false;
            }
            if(canAdd) {
                List<Transaction> currentGroup = new ArrayList<>();

                currentGroup.add(currentTransaction);

                for (int j = i + 1; j < transactions.size(); j++) {
                    if(isTransactionSimilar(currentTransaction, transactions.get(j))) {
                        currentGroup.add(transactions.get(j));
                    }
                }
                groupedTransactions.put(currentTransaction.getDescription(), currentGroup);
            }
        }
        return groupedTransactions;
    }

    private static boolean isTransactionSimilar(Transaction newTransaction, Transaction oldTransaction) {
        return isSimilarDescription(newTransaction, oldTransaction) &&
                isWithinRange(newTransaction.getAmount(), oldTransaction.getAmount() - GRACE_AMOUNT, oldTransaction.getAmount() + GRACE_AMOUNT) &&
                (isMonthly(newTransaction, oldTransaction) || isWeekly(newTransaction, oldTransaction));
    }

    private static boolean isMonthly(Transaction newTransaction, Transaction oldTransaction) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate newDate = LocalDate.parse(newTransaction.getDate(), formatter);
        LocalDate oldDate = LocalDate.parse(oldTransaction.getDate(), formatter);

        long monthsBetween = ChronoUnit.MONTHS.between(newDate, oldDate);
        long daysBetween = ChronoUnit.DAYS.between(newDate, oldDate);

        boolean isOneMonth = monthsBetween == 1 || monthsBetween==-1;
        boolean isGracePeriod = Math.abs(daysBetween) <= 31 + DAY_GRACE_PERIOD;

        return isOneMonth && isGracePeriod;
    }
    private static boolean isWeekly(Transaction newTransaction, Transaction oldTransaction) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate newDate = LocalDate.parse(newTransaction.getDate(), formatter);
        LocalDate oldDate = LocalDate.parse(oldTransaction.getDate(), formatter);

        long weeksBetween = ChronoUnit.WEEKS.between(newDate, oldDate);
        long daysBetween = ChronoUnit.DAYS.between(newDate, oldDate);

        boolean isOneWeek = weeksBetween == 1 || weeksBetween==-1;
        boolean isGracePeriod = Math.abs(daysBetween) <= 7 + DAY_GRACE_PERIOD;

        return isOneWeek && isGracePeriod;
    }

    private static boolean isWithinRange(double number, double lowerBound, double upperBound) {
        return number >= lowerBound && number <= upperBound;
    }

    private static boolean isSimilarDescription(Transaction newTransaction, Transaction oldTransaction) {
        String newDescription = newTransaction.getDescription();
        String oldDescription = oldTransaction.getDescription();
        return isSimilarString(newDescription, oldDescription);
    }

    private static boolean isSimilarString(String s1, String s2) {
        if(s1.equals(s2)) return true;
        int distance = levenshteinDistance(s1, s2);
        double similarityScore = 1.0 - (double) distance / Math.max(s1.length(), s2.length());
        return similarityScore >= SIMILARITY_PERCENTAGE_CAP;
    }

    // Got from external sources
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

    public static void printTransactionsMap(Map<String, List<Transaction>> transactionsMap) {
        for (Map.Entry<String, List<Transaction>> entry : transactionsMap.entrySet()) {
            String key = entry.getKey();
            List<Transaction> transactions = entry.getValue();

            // Print the key (category or identifier)
            System.out.println("Category/Identifier: " + key);

            // Print each transaction in the list
            for (Transaction transaction : transactions) {
                System.out.println(transaction);
            }

            // Print a separator line for clarity
            System.out.println("--------------------------------------------------");
        }
    }
}