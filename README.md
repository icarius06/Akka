# Akka
My initial attempt at java 8 and Akka toolkit.
<p>
Akka ( http://akka.io/ ), it’s a powerful asynchronous, actor based, message processing framework.
This is a small Java application that processes log files, using a few different actors,demostrating use of message parsing and the framework basics.
</p>
<h5>Requirements</h5>
<ol>
<li>On application startup (main), you’d create your <i>ActorSystem</i> and eventual actors you need</li>
<li>The application (main), sends a scan message to a <i>FileScanner</i> actor which will check if there is any file in predefined directory</li>
<li>The <i>FileScanner</i> actor then sends a parse message to a <i>FileParser</i> actor in order to initiate the parsing</li>
<li>The <i>FileParser</i> actor sends different events (“start-of-file”, “line”, “end-of-file”) to an <i>Aggregator</i> actor, depending on the parser state</li>
<li>The <i>Aggregator</i> actor split words in the lines by the space “ “ character based on the “line” event</li>
<li>The <i>Aggregator</i> actor counts the number of words in a file</li>
<li>The <i>Aggregator</i> actor prints the number of words in a file in the console when it receives the “end-of-file” event</li>
</ol>

# Usage
<p>Edit config.properties found in resources folder for the log folders</p>
<p>You can build the jar file by running this command in the root dir.</p>
<pre><code>mvn clean package
</code></pre>
