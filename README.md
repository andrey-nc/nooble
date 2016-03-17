<h1>nooble - Simple Google clone project</h1>

Functionality:
 - Full-text indexing of a target page
 - Search for required word/fraze    

Frameworks 
 - Spring Boot
 - Spring MVC
 - Spring IoC
 - Lucene
 - Gradle
 - FreeMarker
 - Jsoup
 - lsf4j
 - java concurrency
 - java 8 (lambda, stream)
  
Web-server
 - Apache Tomcat 

IDE
 - InteliJ Idea 15
 
To run app: 
    <code>java -jar build/libs/nooble.jar</code>
 
Default search index directories specified in "lucene.properties":

    <code>WIN: C:\temp\lucene\index</code>
    <code>UNIX: /var/tmp/lucene/index</code>

Log 'nooble.log' is stored in project root
