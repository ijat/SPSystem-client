package my.ijat.spsystem;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.parser.ParseException;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Caller {

    private static String addurl = "https://127.0.0.1:6789/user/add";
    private static String logurl = "https://127.0.0.1:6789/user/login";
    private static String geturl = "https://127.0.0.1:6789/user/get";

    public Caller() {
        try {
            TrustSelfSignedStrategy a = new TrustSelfSignedStrategy();
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(null, a).build();

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .build();
            Unirest.setHttpClient(httpclient);
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public String update(String uid, String uh) throws Exception {
        com.mashape.unirest.http.HttpResponse<JsonNode> jsonResponse = Unirest.get(geturl)
                .header("uid", uid)
                .header("api_key", uh)
                .asJson();

        //System.out.println(jsonResponse.getBody());
        //System.out.println(jsonResponse.getBody().toString());

        org.json.simple.parser.JSONParser jp = new org.json.simple.parser.JSONParser();
        org.json.simple.JSONObject json = (org.json.simple.JSONObject) jp.parse(jsonResponse.getBody().toString());

        Long counter = (Long) json.get("counter");

        /*Map<String, String> m1 = new HashMap<String, String>();
        m1.put("counter",counter.toString());*/

        if (counter.toString().length() <= 0) throw new Exception();
        return counter.toString();
    }

    public void newReg(String un, String pw, String fn, String ir) throws Exception {
        try {
            com.mashape.unirest.http.HttpResponse<JsonNode> jsonResponse = Unirest.post(addurl)
                    .header("username", un)
                    .header("password", pw)
                    .header("fullname", fn)
                    .header("ir_id", ir)
                    .asJson();
            //jsonResponse.getBody().toString();
            //System.out.println(jsonResponse.getBody().toString());

            //jsonResponse.getBody()
            org.json.simple.parser.JSONParser jp = new org.json.simple.parser.JSONParser();
            org.json.simple.JSONObject json = (org.json.simple.JSONObject) jp.parse(jsonResponse.getBody().toString());
            System.out.println(json.get("status"));
            if (json.get("status").equals("FAIL")) throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("POST failed");
        }
    }

    public Map<String, String> login(String un, String pw) throws Exception {
        try {
            com.mashape.unirest.http.HttpResponse<JsonNode> jsonResponse = Unirest.get(logurl)
                    .header("username", un)
                    .header("password", pw)
                    .asJson();

            //System.out.println(jsonResponse.getBody());
            //System.out.println(jsonResponse.getBody().toString());

            org.json.simple.parser.JSONParser jp = new org.json.simple.parser.JSONParser();
            org.json.simple.JSONObject json = (org.json.simple.JSONObject) jp.parse(jsonResponse.getBody().toString());

            Long uid, irid, counter;
            uid = (Long) json.get("uid");
            irid = (Long) json.get("ir_id");
            counter = (Long) json.get("counter");

            Map<String, String> m1 = new HashMap<String, String>();
            m1.put("uid",uid.toString());
            m1.put("fullname",(String) json.get("fullname"));
            m1.put("uhash",(String) json.get("uhash"));
            m1.put("ir_id",irid.toString());
            m1.put("counter",counter.toString());

            if (m1.get("uid").length() <= 0) throw new Exception();
            return m1;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
