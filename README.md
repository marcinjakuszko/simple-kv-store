# Simple KV store
This a sample recruitment tasks that needed to be completed within 60 minutes.
Main goals:
1. Provide basic data commands (all in O(logN) time):
   1. GET(key)
   2. SET(key, value)
   3. COUNT(value)
   4. DELETE(key)
2. Provide support for basic transactions commands:
   1. BEGIN - open a transaction block
   2. ROLLBACK - rollback all of commands from active transaction. Should throw exception when there is no active transaction.
   3. COMMIT - commit **all of transactions that are opened**

Note:
Transactions should not double amount of memory needed to store data. It shouldn't rely on the number of stored elements.