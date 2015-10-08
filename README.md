# Line Server

This is an implementation of the [Line Server Problem](https://salsify.github.io/line-server.html) in Clojure, using Ring and Compojure.

I chose Clojure because I feel that it is a very clean and simple high-level language, and that the requirements of the project were simple enough that I could take advantage of the language's best features. I don't code professionally in Clojure, so I rarely do web development with Ring or Compojure, but I took this opportunity to complete the project in a language I want to get more experience in.

## What's Clojure?

[Clojure](http://clojure.org/) is a dialect of Lisp that runs on the JVM. It can be interpreted in a REPL, or compiled as a .jar. It has interoperability with all core Java classes and packages, and there are many Clojure bindings for Java libraries (e.g. Processing and JDBC). Clojure combines the expressiveness and simplicity of Lisp with the power and knowledge-base of Java and the JVM.

## How to Build and Run

Clojure projects generally use [Leiningen](http://leiningen.org/) as a build tool, dependency manager, and development environment. Thus, building and running this application requires Leiningen to be installed. After installation, the application can be built and run simply by invoking the requisite `lein` commands. `build.sh` and `run.sh` are provided, but assume that Leiningen is already installed.

To build dependencies: `lein deps`.

To open REPL: `lein repl`.

To run application: `lein ring server-headless`.

To build: `lein ring uberjar`. (You can run the uberjar with `java -jar target/line-server-0.1.0-SNAPSHOT-standalone.jar`.)

## FAQs

### How does your system work?

Ring abstracts basic HTTP functionality away in a manner similar to Rack in Ruby, and Compojure is a routing library built on top of it. The basic program route is `/:file-name/lines/:line-number`, where `:file-name` is the name of the file to load (e.g. "50.txt" for a 50-line file) and `:line-number` is the line number to retrieve. These occur in constant time due to file lines being loaded into memory as the server is started.

The basic program flow is:

1. Start server.
1. Load all lines of all specified files into memory, stored in vectors (arrays).
1. Receive GET request.
1. Retrieve specified file and line number from route of requested endpoint.
1. Look up line via file and line number, in memory banks.
1. If line number is too large, or line number is 0, return 413.
1. If line exists, return 200 and the specified line.

### How will your system perform with a 1 GB file? a 10 GB file? a 100 GB file?

Currently, the system loads each file into memory and stores them in vectors (Clojure data structures analogous to arrays). This is so that after initial startup, lookups for any valid line in any valid file are in constant time O(1), as arrays (and thereby vectors) have constant-time access. For simple use cases and smaller files of about 100-1000 lines, this is acceptable, but it's quickly apparent that this does not scale as file size becomes larger. For larger files like those, it would be better to retrieve the lines from a database. Clojure has good support for RDBMS approaches via various SQL handling libraries and native compatibility with JDBC.

### How will your system perform with 100 users? 10000 users? 1000000 users?

Ideally, the system would handle incoming requests in parallel. In reality, Ring by default queues requests, so this application currently does everything sequentially, which doesn't scale well at all. Extensions like `ring-async` allow for concurrency and parallel processing, so with the current application, that is an option. Handling large amounts of users and requests could be improved even further by caching requests so that specific lines need only be loaded once, ever. (Or at least until the server shuts down.)

### What documentation, websites, papers, etc did you consult in doing this assignment?

* [Grimoire](conj.io) is absolutely indispensible when hacking at the REPL.
* [The Clojure Toolbox](www.clojure-toolbox.com) is a great compendium of Clojure tools, libraries, and frameworks.
* [Web Development With Clojure](https://pragprog.com/book/dswdcloj/web-development-with-clojure) and [Functional Programming Patterns in Scala and Clojure](https://pragprog.com/book/mbfpp/functional-programming-patterns-in-scala-and-clojure) are excellent books that details best practices for development in Clojure, both in general and web-development-specific.
* [Postman](https://www.getpostman.com/) is very useful for hand-testing REST APIs and endpoints.
* Clojure supports looking up the documentation and source code for any core function in the REPL, via `doc`. It's very convenient.

### What third-party libraries or other tools does the system use? How did you choose each library or framework you used?

* [Leiningen](http://leiningen.org/) is the de facto unofficial Clojure build tool. Almost every Clojure project starts here.
* [Ring](https://github.com/ring-clojure/ring) and [Compojure](https://github.com/weavejester/compojure) are two of the most common and well-supported HTTP libraries available for Clojure.

Clojure requires relatively little third party software, thanks to its powerful core functionality, and an emphasis on simplicyt and bottom-up development. However, it encourages building software from smaller libraries and there is a wide selection available. And, of course, Java interop means you can use any Java library in a Clojure program.

### How long did you spend on this exercise? If you had unlimited more time to spend on this, how would you spend it and how would you prioritize each item?

I spent about 2 hours on the core functionality, out of concern for submitting a completed project in a timely manner. I spent about 30-45 minutes on README.md.

There is a *lot* left out that I'd like to add:

1. Proper benchmarking of file line access running times (e.g. for proving constant time lookup).
2. Comprehensive automated testing for API endpoints and aforementioned benchmarking.
3. Asynchronous, concurrent request handling via a library built specifically for scalability, such as [http-kit](http://www.http-kit.org/600k-concurrent-connection-http-kit.html)
4. Explicit specification and diagramming of program flow.
5. Caching for line requests.
6. Handling of large files via an RDBMS, or my favorite fact-based database, Datomic.

### If you were to critique your code, what would you have to say about it?

"It's a start. But, it has promise, and it has the ability to grow and improve without becoming unwieldy."
