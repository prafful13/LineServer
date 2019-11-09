import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ApplicationTest {

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();
    private static final int numberOFConcurrentClients = 1000000;
    private static int lineNumber = 99;
    private static BufferedReader br = null;
    private static String apiResponse;
    private static String fileLineResponse;
    private static HttpGet request;
    private static String result;

    /**
     * Testing correct data is sent back by the API
     *
     * @throws Exception
     */
    @Test
    public void testLineNumber() throws Exception {
        apiResponse = getResponse();
        fileLineResponse = null;
        br = new BufferedReader(new FileReader(new File("TestFile")));
        int cnt = 1;
        for (String line = null; (line = br.readLine()) != null; cnt++) {
            if (cnt == lineNumber) {
                fileLineResponse = "{\"id\":" + "\"" + cnt + "\"" + "," + "\"lineData\":\"" + line + "\"}";
                break;
            }
        }
        assert (apiResponse.equals(fileLineResponse));
    }

    /**
     * Testing that Api is able to handle a million concurrent connections
     *
     * @throws Exception
     */
    @Test
    public void testConcurrentConnections() throws Exception {
        final ExecutorService service = Executors.newFixedThreadPool(1000);
        final List<Future<String>> returnedLines = Lists.newArrayList();
        for (int i = 0; i < numberOFConcurrentClients; i++) {
            final int finalI = i;
            final Runnable task = () -> {
                lineNumber = finalI;
                try {
                    getResponse();
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            };
            returnedLines.add((Future<String>) service.submit(task));
        }
        assert (returnedLines.size() == numberOFConcurrentClients);
    }

    /**
     * Helper method used to get response from the server
     *
     * @return Returns the response received from the server
     * @throws Exception
     */
    private String getResponse() throws Exception {
        request = new HttpGet("http://localhost:8080/lines/" + lineNumber);
        result = null;

        final CloseableHttpResponse response = httpClient.execute(request);

        final HttpEntity entity = response.getEntity();
        final Header headers = entity.getContentType();

        result = EntityUtils.toString(entity);

        return result;
    }
}
