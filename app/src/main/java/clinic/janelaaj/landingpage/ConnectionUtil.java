package clinic.janelaaj.landingpage;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ConnectionUtil {

    /**
     * Return a list of {@link JSONObject} objects passing
     * @param url of given ENDPOINT
     * @param js parameters that are required to get response from ENDPOINT
     * Method: POST
     */
    public static JSONObject postMethod(String url, JSONObject js) throws IOException, JSONException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = null;
        try {
            entity = new StringEntity(js.toString(), HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Setting the content type is very important
        assert entity != null;
        //entity.setContentEncoding(HTTP.UTF_8);
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        //Execute and get the response.
        HttpResponse response = httpClient.execute(httpPost);
        String jsonString = EntityUtils.toString(response.getEntity());
        return new JSONObject(jsonString);
    }
}
