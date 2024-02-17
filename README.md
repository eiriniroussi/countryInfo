# countryInfo
A java FX app made to fetch data(name,currencies,population,continents) about countries based on some search criteria like country name, country native language or country language. There is also a functionality of displaying the information about all the countries.

INSTRUCTIONS TO INTERGRATE THE LIBRARY WITH THE JAVAFX APP (2 ways)
- Install maven to your local computer using a terminal

1) Download the jar file from GITHUB

- After you have installed maven run this line to install the jar file to your local maven repository: 
mvn install:install-file -Dfile=path-where-you-saved-the-jar-file -DgroupId=gr.unipi.library -DartifactId=countries-data-library -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar

If everything goes well you should be able to find the jar file at ../.m2/repository/gr/unipi/library

2) You can also download the folder "gr" from the project and paste it at ../.m2/repository/

