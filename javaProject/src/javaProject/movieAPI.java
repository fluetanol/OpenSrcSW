package javaProject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class movieAPI {

	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
		 String clientId = "ibA3KiAT9XwcCjJo5qAJ"; //애플리케이션 클라이언트 아이디값"
	        String clientSecret = "JqGv8COPI0"; //애플리케이션 클라이언트 시크릿값"

	        String text = null;
	        try {
	            text = URLEncoder.encode("그린팩토리", "UTF-8");
	        } catch (UnsupportedEncodingException e) {
	            throw new RuntimeException("검색어 인코딩 실패",e);
	        }

	        
	        String apiURL = "https://openapi.naver.com/v1/search/movie.json?query=%EC%A3%BC%EC%8B%9D&display=10&start=1&genre=1";    // json 결과
	        //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과

	        Map<String, String> requestHeaders = new HashMap<>();
	        requestHeaders.put("X-Naver-Client-Id", clientId);
	        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
	        String responseBody = get(apiURL,requestHeaders);

	
	        JSONParser jsonParser = new JSONParser();
	        JSONObject jsonObject = (JSONObject) jsonParser.parse(responseBody);
	        JSONArray infoArray = (JSONArray) jsonObject.get("items");  //items라는 key안의 value를 가져옴
	        
	        for(int i=0; i<infoArray.size(); i++)
	        {
	        	JSONObject tempObject = (JSONObject) infoArray.get(i);
	        	System.out.println("=item"+i+"========================");
	        	System.out.println("title:      \t"+tempObject.get("title"));
	        	System.out.println("subtitle:   \t"+tempObject.get("subtitle"));
	        	System.out.println("director:   \t"+tempObject.get("director"));
	        	System.out.println("actor:      \t"+tempObject.get("actor"));
	        	System.out.println("userRating: \t"+tempObject.get("userRating")+"\n");
	        }
	    }

	    private static String get(String apiUrl, Map<String, String> requestHeaders){
	        HttpURLConnection con = connect(apiUrl);
	        try {
	            con.setRequestMethod("GET");
	            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
	                con.setRequestProperty(header.getKey(), header.getValue());
	            }

	            int responseCode = con.getResponseCode();
	            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
	                return readBody(con.getInputStream());
	            } else { // 에러 발생
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
	                responseBody.append(line+"\n");
	            }

	            return responseBody.toString();
	        } catch (IOException e) {
	            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
	        }
	}

}
