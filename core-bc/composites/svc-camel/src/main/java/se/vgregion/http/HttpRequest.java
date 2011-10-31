package se.vgregion.http;


import javax.xml.bind.DatatypeConverter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Patrik Bergström
 */
public class HttpRequest {

    private String queryString;
    private String body;
    private Map<String,String> headers = new HashMap<String, String>();

    public void setQueryByString(String queryString) {
        this.queryString = queryString;
    }

    public void setQueryByMap(Map<String, String> queryMap) {
        StringBuilder sb = mapToQuery(queryMap);
        this.queryString = sb.toString();
    }

    public String getQueryString() {
        return queryString;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void addBasicAuthentication(String userName, String password) {
        String base64Part = DatatypeConverter.printBase64Binary((userName + ":" + password).getBytes());
        headers.put("Authorization", "Basic " + base64Part);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public static StringBuilder mapToQuery(Map<String, String> parameters) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (Map.Entry<String, String> key : parameters.entrySet()) {
            sb.append(key.getKey() + "=" + key.getValue());
            count++;
            if (count < parameters.size()) {
                sb.append("&"); //we will have at least one more parameter to add to our query
            }
        }
        return sb;
    }
}
