# Akka
My initial attempt at java 8 and Akka toolkit.

Java - Backend - Assignment #1
One of the framework we use in our architecture is called Akka ( http://akka.io/ ), it’s a powerful asynchronous, actor based, message processing
framework.
Without going too much into the details, we’d like you to write a small Java application that processes log files, using a few different actors,
showing you learned how to use message parsing and the framework basics.
1.On application startup (main), you’d create your ActorSystem and eventual actors you need
2.The application (main), sends a scan message to a FileScanner actor which will check if there is any file in predefined directory
3.The FileScanner actor then sends a parse message to a FileParser actor in order to initiate the parsing
4.The FileParser actor sends different events (“start-of-file”, “line”, “end-of-file”) to an Aggregator actor, depending on the parser state
5.The Aggregator actor split words in the lines by the space “ “ character based on the “line” event
6.The Aggregator actor counts the number of words in a file
7.The Aggregator actor prints the number of words in a file in the console when it receives the “end-of-file” event
You are free to use any extra library you need and you feel comfortable with (ex. google guava, apache commons, etc). If you’re familiar with, I’d
recommend you to use something like Maven or Gradle to manage your dependencies, if you’re not, no big deal.
It would be nice to have an executable JAR file, though, I’m more interested in your sources to see the way you work.
Note that, you’ll be judged on the code quality , so, pay attention to details , just like you’d do if it was for a real-life application.
Let me know what you think of this exercise and when you think you’d have time to complete it.
