package com.kuaizhan.integration;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import spark.utils.IOUtils;


public class SurveyIntegrationTest {
	
    private static int PORT = 8080;

    private static UrlResponse doMethod(String requestMethod, String path, String body) {
        UrlResponse response = new UrlResponse();

        try {
            getResponse(requestMethod, path, response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    private static void getResponse(String requestMethod, String path, UrlResponse response)
            throws MalformedURLException, IOException, ProtocolException {
        URL url = new URL("http://localhost:" + PORT + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod);
        connection.connect();
        String res = IOUtils.toString(connection.getInputStream());
        response.body = res;
        response.status = connection.getResponseCode();
        response.headers = connection.getHeaderFields();
    }

    private static class UrlResponse {
        public Map<String, List<String>> headers;
        private String body;
        private int status;
    }
	
}
