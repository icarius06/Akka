# Akka
My initial attempt at java 8 and Akka toolkit.

Java - Backend - Assignment #1
Akka ( http://akka.io/ ), it’s a powerful asynchronous, actor based, message processing framework.
This is a small Java application that processes log files, using a few different actors,demostrating use of message parsing and the framework basics.
1.On application startup (main), you’d create your ActorSystem and eventual actors you need
2.The application (main), sends a scan message to a FileScanner actor which will check if there is any file in predefined directory
3.The FileScanner actor then sends a parse message to a FileParser actor in order to initiate the parsing
4.The FileParser actor sends different events (“start-of-file”, “line”, “end-of-file”) to an Aggregator actor, depending on the parser state
5.The Aggregator actor split words in the lines by the space “ “ character based on the “line” event
6.The Aggregator actor counts the number of words in a file
7.The Aggregator actor prints the number of words in a file in the console when it receives the “end-of-file” event
