# Akka
My initial attempt at java 8 and Akka toolkit.

Java - Backend - Assignment #1
Akka ( http://akka.io/ ), it’s a powerful asynchronous, actor based, message processing framework.
This is a small Java application that processes log files, using a few different actors,demostrating use of message parsing and the framework basics.
<ol>
<li>On application startup (main), you’d create your ActorSystem and eventual actors you need</li>
<li>The application (main), sends a scan message to a FileScanner actor which will check if there is any file in predefined directory</li>
<li>The FileScanner actor then sends a parse message to a FileParser actor in order to initiate the parsing</li>
<li>The FileParser actor sends different events (“start-of-file”, “line”, “end-of-file”) to an Aggregator actor, depending on the parser state</li>
<li>The Aggregator actor split words in the lines by the space “ “ character based on the “line” event</li>
<li>The Aggregator actor counts the number of words in a file</li>
<li>The Aggregator actor prints the number of words in a file in the console when it receives the “end-of-file” event</li>
</ol>
