I first investigated the transactions in the example payload to identify patterns and edge-cases that should be accounted for in the implementation. Spotify showcases a case where transactions can be grouped by both amount, description and day of transaction in a month, but also presents an edge case where some transactions could be taken on days after the 28th where some months like February would throw off a naive pattern recognition implementation. Netflix showcases a case where transactions can be grouped by amount and description but that I would have to allow for a grace period to account for any variance in the day of transaction.



The salary group of transactions showcases a case where the day of transaction is the same each month but the description and amount could vary. Each description contains a substring that could be used to more confidently identify the group of transactions. The meal related transactions reinforce the need for a comparison of the similarity of description as well as presenting some edge cases that could prove problematic. For example, using the "meal" substring to associate a group, may prove to throw off the grouping algorithm if not correctly used in conjunction with the ensuring amount and day of transaction comparison. Additionally, I would only group transactions with different descriptions if the recurring substring in the description is above a percentage of the total characters in the description. This would help ensure "one-off meal" and special meal are not grouped together.




I would also normalise each description to ensure that minor difference like punctuation and case are ignored when comparing the similarity of the descriptions.



So my approach is to first check for transactions where the description, amount and day of month/week of the transaction are the same. Then I do a check to allow a grace period of 2 days to ensure I capture any recurring transactions that may not happen exactly every month or exactly every week but are recurring. I also then do a check for transactions where the descriptions are not exactly the same but have at least a 70% match. This is done by splitting the description into words and checking for similar words. If I had more time, I would’ve looked into implementing a similarity algorithm like Levenshtein distance or cosine similarity to measure the similarity between two descriptions.