"""
Your system should act as a network server that serves individual lines of an immutable text file over the network to clients using the following simple REST API:

* `GET /lines/<line index>`
  * Returns an HTTP status of 200 and the text of the requested line or an HTTP 413 status if the requested line is beyond the end of the file.
"""

Given I have a file 50 lines long,

  When I send a GET request to `/lines/:index`
  And `:index` is between 1 and 50 inclusive,
  Then I should receive an HTTP 200
  And the text of that line;

  When I send a GET request to `/lines/:index`
  And `:index` is greater than 50,
  Then I should receive an HTTP 413;
  
  When I send a GET request to `/lines/:index`
  And `:index` is 0,
  Then I should receive an HTTP 413.

Notes:

* Assume 1-index for line numbers. e.g. `/lines/1` is the first line of the text file.

Questions to investigate:

* How to handle 0, in this case? Empty line, or 413?

---

"Your server should support multiple simultaneous clients."

Given I have a file 50 lines long,

  When I send a GET request to `/lines/1`
  And I send a GET request to `/lines/2`
  And I send a GET request to `/lines/3`,
  Then for each GET request I should receive an HTTP 200
  And the text of that line;

Notes:

* If several requests are fired at the same time, then the time it takes to receive all results should not be longer than the time it takes to receive the result of the longest-running query.
* Basically, have everything fire at the same time, in parallel. This implies support for asynchronous procressing.

Questions to investigate:

* How do you handle multiple requests at a time? Is the ability to handle that baked into HTTP servers already? Or do you need to implement it yourself?

---

"The system should perform well for small and large files."

Given I have a file 1,000,000 lines long,

  When I send a GET request to `/lines/50`,
  Then I should receive an HTTP 200
  And the text of that line;

  When I send a GET request to `/lines/500000`,
  Then I should receive an HTTP 200
  And the text of that line;

Notes:

* The intent of this specification is to emphasize that line access should be in constant time O(1). The most obvious way to achieve this is to use an array to hold the lines, as the lookup time in an array is constant time O(1).
* Part of the tests should be recording the amount of time it took to perform each request, and comparing them. They should be within a certain delta of each other.

Questions to investigate:

* What should the acceptability criteria be for the time delta in the second note?

---

"The system should perform well as the number of GET requests per unit time increases."

* The server should be able to handle 2 GET requests at the same time, and 1000 GET requests at the same time, gracefully and with a marginal increase in server load.
* Basically, the system should be able to handle a whole bunch of concurrent requests. This may be dependent on async being implemented.
