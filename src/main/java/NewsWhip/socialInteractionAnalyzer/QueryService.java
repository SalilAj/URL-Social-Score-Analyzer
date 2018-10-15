package NewsWhip.socialInteractionAnalyzer;

import static com.mongodb.client.model.Filters.eq;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;

public class QueryService {

	public static void printStat(String domain, int count, Double score) {
		System.out.println(domain + ";" + count + ";" + Math.round(score));
	};

	public static boolean isValidURL(String url) {
		try {
			URI uri = new URI(url);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isValidScore(String score) {
		try {
			double d = Double.parseDouble(score);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static String domainNameRetriever(String input) {
		String result = "";
		try {
			URI uri = new URI(input);
			String domain = uri.getHost();
			result = domain.startsWith("www.") ? domain.substring(4) : domain;
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return result;

	}

	public static boolean addURL(MongoConnection mongoConn, String url, String score) {

		boolean result = false;

		if (isValidURL(url) && isValidScore(score)) {
			String domainName = domainNameRetriever(url);

			UpdateOptions updateOp = new UpdateOptions();
			mongoConn.domainCollection.updateOne(eq("domainName", domainName),
					new Document("$addToSet", new Document("urls", url)), updateOp.upsert(true));

			mongoConn.urlCollection.updateOne(eq("url", url), new Document("$set", new Document("score", score)),
					updateOp.upsert(true));

			result = true;
		}
		return result;
	}

	public static boolean removeURL(MongoConnection mongoConn, String url) {

		boolean result = false;

		if (isValidURL(url)) {
			String domainName = domainNameRetriever(url);

			mongoConn.domainCollection.updateOne(eq("domainName", domainName),
					new Document("$pull", new Document("urls", url)));

			mongoConn.urlCollection.deleteOne(eq("url", url));
			result = true;

		}
		return result;

	}

	public static void getURLStats(MongoConnection mongoConn) {
		System.out.println("\nDisplaying URL stats:");
		String domain = "";
		boolean statsPresent = false;
		int count;
		double score;

		MongoCursor<Document> cursor = mongoConn.domainCollection.find().iterator();
		try {
			while (cursor.hasNext()) {
				Document doc = cursor.next();
				boolean urlExists = false;
				count = 0;
				score = 0;

				domain = doc.getString("domainName");

				ArrayList<String> urlList = (ArrayList<String>) doc.get("urls");

				if (urlList.size() > 0) {
					urlExists = true;
					for (String url : urlList) {
						count++;
						MongoCursor<Document> urlDocs = mongoConn.urlCollection.find(new Document("url", url))
								.iterator();
						Document urlDoc = urlDocs.next();
						score = score + Double.parseDouble(urlDoc.getString("score"));
					}
				}

				if (urlExists) {
					statsPresent = true;
					printStat(domain, count, score);
				}
			}
			if(!statsPresent)
			{
				System.out.println("No URL Stats present");
			}

		} finally {
			cursor.close();
		}

	}

}
