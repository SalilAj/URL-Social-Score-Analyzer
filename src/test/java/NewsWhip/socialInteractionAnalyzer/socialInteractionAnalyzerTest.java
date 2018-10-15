package NewsWhip.socialInteractionAnalyzer;

import static org.junit.Assert.*;

import org.junit.Test;

public class socialInteractionAnalyzerTest {

	@Test
	public void isValidURLTest() {
		String url = "http://test.com/test123";
		boolean result = QueryService.isValidURL(url);
		assertEquals(true, result);
	}

	@Test
	public void isValidScoreTest() {
		String score = "20";
		boolean result = QueryService.isValidScore(score);
		assertEquals(true, result);
	}

	@Test
	public void domainNameRetrieverTest() {
		String url = "http://test.com/test123";
		String domainName = QueryService.domainNameRetriever(url);
		assertEquals("test.com", domainName);
	}

	@Test
	public void addURLTest() {
		MongoConnection mongoConn = new MongoConnection();
		String url = "http://test.com/test123";
		String score = "20";
		boolean result = QueryService.addURL(mongoConn, url, score);
		assertEquals(true, result);
	}

	@Test
	public void removeURLTest() {
		MongoConnection mongoConn = new MongoConnection();
		String url = "http://test.com/test123";
		boolean result = QueryService.removeURL(mongoConn, url);
		assertEquals(true, result);
	}

	@Test
	public void getURLStatsTest() {
		MongoConnection mongoConn = new MongoConnection();
		QueryService.getURLStats(mongoConn);
	}

}
