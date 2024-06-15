package com.light.backend.naver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class NaverOpenApiCaller {

    @Value("${naver.clientId}")
    private String clientId;

    @Value("${naver.clientSecret}")
    private String clientSecret;

    private static final String API_URL = "https://openapi.naver.com/v1/search/shop?query=";
    private static final String CLIENT_ID_HEADER = "X-Naver-Client-Id";
    private static final String CLIENT_SECRET_HEADER = "X-Naver-Client-Secret";

    public String call(String keyword)  {
        String text = null;
        try {
            text = URLEncoder.encode(keyword, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String apiURL = API_URL + text;    // JSON 결과

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put(CLIENT_ID_HEADER, clientId);
        requestHeaders.put(CLIENT_SECRET_HEADER, clientSecret);

        String responseBody = get(apiURL,requestHeaders);

        return responseBody;
    }

    private static String get(String apiUrl, Map<String, String> requestHeaders) {

        HttpURLConnection con = connect(apiUrl);

        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }


            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 오류 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }


    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }


    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }
}
