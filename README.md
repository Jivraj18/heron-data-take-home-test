# My Approach
I first investigated the transactions in the example payload to identify patterns and edge-cases that should be accounted for in the implementation. Spotify showcases a case where transactions can be grouped by amount, description and day of transaction in a month, but also presents an edge case where some transactions could be taken on days after the 28th where some months like February would throw off a naive pattern recognition implementation.

Netflix showcases a case where transactions can be grouped by amount and description. However, I would have to allow for a grace period to account for any variance in the day of transaction, since the repeated transaction does not occur on the same date every month.

The salary group of transactions showcases a case where the day of transaction is the same each month but the description and amount could vary. Each description contains a substring that could be used to more confidently identify the group of transactions. The meal related transactions reinforce the need for a comparison of the similarity of description as well as presenting some edge cases that could prove problematic. For example, using the "meal" substring to associate a group, may prove to throw off the grouping algorithm if not correctly used in conjunction with the amount and day of transaction comparison. Additionally, I would only group transactions with different descriptions if the recurring substring in the description is above a percentage of the total characters in the description. This would help ensure "one-off meal" and “special meal” are not grouped together. I would also normalise each description to ensure that minor difference like punctuation and case are ignored when comparing the similarity of the descriptions.

So my approach is to first check for transactions where the description, amount and day of month/week of the transaction are the same. Then I do a check to allow a grace period of 3 days to ensure I capture any recurring transactions that may not happen exactly every month or exactly every week but are recurring. I also then do a check for transactions where the descriptions are not exactly the same but have at least a 70% match. This is done by using a similarity algorithm, Levenshtein distance.

This was done under a time limit and I would some things to improve my solution. Firstly, I would make it more efficient. Currently I am using a double nested for loop to iterate through the data which gives a time complexity of O(n^2). This is inefficient and can be improved by using a more sophisticated approach. This is specially important when analysing large data sets since customer’s expect fast responses. Furthermore, I would write unit tests that cover edge cases which may not be present in the example payload.


# Discussion

   ```
   1. How would you measure the accuracy of your approach?
```

 While working on my machine learning model for my dissertation, I used the Receiver  Operating Characteristics (ROC) curve to analyse my classification accuracy. This is a  common approach to analyse classification accuracy and I think it would be useful to use  here to provide an objective accuracy score. Alternatively, to ensure the approach  exceeds customer expectations, an empirical study can be conducted. I can give the  example payload to an end user to identify recurring transactions and then compare the  results with the results from my solution.

    2. How would you know whether solving this problem made a material impact on customers?

 I can gain feedback from the end users via surveys, interviews, feedback forms, reviews  and more. These users can inform me on whether the problem was solved and if there is  anything in the solution that can be improved. For example, If the end users believe the  response time isn’t fast enough then changes would have to be made to improve the  performance of my solution.

    3. How would you deploy your solution?

 Build a docker image using a docker file. Once the docker file is created it can be used in  conjunction with deployment environments such as Kubernetes to deploy the docker  image to production. This facilitates scalability of the application.

    4. What other approaches would you investigate if you had more time?
 As mentioned previously, I would make my solution more efficient. Currently I am using a  double nested for loop to iterate through the data which gives a time complexity of  O(n^2). This is inefficient and can be improved by using a more sophisticated approach.  For example, by using an unsupervised machine learning approach like clustering to  group transactions into clusters based on similarity.

