# URL-Socia-Score-Analyzer
A simple Java program that adds, removes and exports the cumulative social scores of a web domain based on its URL link scores.
The system should support:<br />
● Adding a URL with an associated social score<br />
● Removing a URL from the system<br />
● Exporting statistics about the URLs stored in the system. <br />
The export should consist of the aggregated social score for the domains in the system.

For Example:
A run of the system might look as follows:<br />
● ADD https://www.rte.ie/news/politics/2018/1004/1001034-cso/ 20<br />
● ADD https://www.rte.ie/news/ulster/2018/1004/1000952-moanghan-mine/ 30<br />
● ADD http://www.bbc.com/news/world-europe-45746837 10<br />
● EXPORT<br />
○ Produces a report displaying the domains, number of urls for each domain and the sum of social score for each domain, for example:<br />
domain;urls;social_score<br />
rte.ie;2;50<br />
bbc.com;1,10<br />

● REMOVE https://www.rte.ie/news/ulster/2018/1004/1000952-moanghan-mine/<br />
● EXPORT<br />
○ Produces a report of:<br />
domain;urls;social_score<br />
rte.ie;1;20<br />
bbc.com;1,10<br />

## Architecture
This is a simple CLI application written in Java which stores the URL information in MongoDb

The architecture of the Web application is as follows:
- MongoDB (Database)
- Java Applicaiton

### Data Modelling (MongoDB)
A record of the Domain names and their corresponding URLs needs to be maintained in such a way that addition and removal of the data leds to a clean removal of records from the database.This daya is stored in a single database in mongodb named *'newsWhipURLs'*. 

Within this database are two collections namely:
1) domain
2) url

The data format of documents within those collections are as follows:
1) domain:
```javascript
{
	"_id" : ObjectId("5bc468b8021c14c3fdb04aca"),
	"domainName" : "rte.ie",
	"urls" : [
		"https://www.rte.ie/news/politics/2018/1004/1001034-cso/",
		"https://www.rte.ie/news/ulster/2018/1004/1000952-moanghan-mine/"
	]
}
```
*_id* = MongoDB generated Unique Id for the Document<br />
*domainName* = Unique domain name <br />
*urls* = A list of all the Urls of that name who have been scored<br />

2) url
```javascript
{ 
  "_id" : ObjectId("5bc4ab15021c14c3fdb04f8f"), 
  "url" : "https://www.rte.ie/news/politics/2018/1004/1001034-cso/", 
  "score" : "20" 
}
```
*_id* = Indicates to which member the list belongs to.<br />
*url* = URL<br />
*score* = Social interaction score of the URL<br />

### Service functionality
The Operations are simple. The user is prompted to input the desired operation and the relevant details.

For adding a URL and its social score, we strip the domain name and insert a document into the domain collection. It contains the domain name and the URL list. (If the domain already exists in the system then we just add the url into the URL list of the mongo document). Similarly we add the URL into the url collection with its score.

For removing a URL from the system, we strip the domain name and pull the URL out of its 'url' list from the domain collection document. Similarly we delete the URL from the url collection.

To export the domain statistics, we query all the documents from the domain collection. Each document is unique to a domain name and the relevant URLs are present in the 'url' list in the document. Using the 'url' list we quesry the url information from the url collection. for all the url documents retrieved we calculate the number of URL's and the total social score of the domain.

### Testing
Implemented unit tests for the data query service using Junit. <br />
(*refer https://github.com/SalilAj/URL-Socia-Score-Analyzer/blob/master/src/test/java/NewsWhip/socialInteractionAnalyzer/socialInteractionAnalyzerTest.java*)
### How to run
Ubuntu Linux:<br />
1) Install mongodb:<br />
run "sudo apt install -y mongodb-org"<br />
*make sure mongod is running in the background*<br />
*no need to configure anything in Mongo, database and collections will be create automatically when mongod gets a request*

2) Run the program:<br />
java -jar NewsWhip.jar <br />
*Remaining is self explanatory*
