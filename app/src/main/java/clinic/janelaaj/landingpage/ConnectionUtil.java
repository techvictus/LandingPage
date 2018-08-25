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

/**
 *  Methods of {@link ConnectionUtil} is used to connect {@link Endpoints}
 *
 *  @author Sambit Mallick (sambit-m)
 *  Created by Sambit Mallick on 20.08.2018
 */

public class ConnectionUtil {

    /**
     * Return a list of {@link JSONObject} objects passing
     * @param url of given ENDPOINT
     * @param jsString parameters that are required to get response from ENDPOINT
     * Method: POST
     */
    public static JSONObject postMethod(String url, String jsString) throws IOException, JSONException {

        JSONObject js = null;
        if(jsString!=null)
            js = new JSONObject(jsString);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = null;
        try {
            if(js!=null)
                entity = new StringEntity(js.toString(), HTTP.UTF_8);
            else
                entity = new StringEntity(HTTP.UTF_8);
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
