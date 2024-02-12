# countryInfo
A java FX app made to fetch data(name,currencies,population,continents) about countries


INSTRUCTIONS TO INTERGRATE THE LIBRARY WITH THE JAVAFX APP
- Install maven to your local computer using a terminal

-Download the jar file from GITHUB

- After you have installed maven run this line to install the jar file to your local maven repository: 
mvn install:install-file -Dfile=path-where-you-saved-the-jar-file -DgroupId=gr.unipi.library -DartifactId=countries-data-library -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar

- Ensure that the library is included as a dependancy in the pom.xml file as follows (If not please add the following lines in your pom.xml file):
<dependency>
    <groupId>gr.unipi.library</groupId>
    <artifactId>countries-data-library</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>

- In the pom.xml file please replace the path inside the <countries-data-library-sysrtemPath></countries-data-library-systemPath> with the path of the jar file installed in your local maven repository

