How to run the tests (unit and integration):<br>
mvn clean verify -U

How to use the application:<br>
You need to put the csv file name as a runtime argument, and only this one. Otherwise, an error message will appear, telling this.<br>
mvn exec:java -Dexec.mainClass=big.company.Main -Dexec.args=example.csv

The csv format should be the same is it was provided by the example:<br>
Id,firstName,lastName,salary,managerId<br>
123,Joe,Doe,60000,<br>
124,Martin,Chekov,45000,123<br>
125,Bob,Ronstad,47000,123<br>
300,Alice,Hasacat,50000,124<br>
305,Brett,Hardleaf,34000,300

Assumptions, when I wasn't 100% sure in something:
* When calculating the earning related data, I'll assume the CEO is a Manager as well
