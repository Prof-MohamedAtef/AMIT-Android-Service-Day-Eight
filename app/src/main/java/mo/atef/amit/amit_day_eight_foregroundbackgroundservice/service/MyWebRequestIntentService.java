package mo.atef.amit.amit_day_eight_foregroundbackgroundservice.service;

import static mo.atef.amit.amit_day_eight_foregroundbackgroundservice.activities.IntentServiceActivty.MyWebServiceReceiver.PROCESS_RESPONSE;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;

import androidx.annotation.Nullable;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MyWebRequestIntentService extends IntentService {

    public static final String REQUEST_STRING = "myRequest";
    public static final String RESPONSE_STRING = "myResponse";
    public static final String RESPONSE_MESSAGE = "myResponseMessage";
    String responseMessage = "";
    private String URL = null;

    private static final int REGISTRATION_TIMEOUT = 3 * 1000;
    private static final int WAIT_TIMEOUT = 30 * 1000;

    public MyWebRequestIntentService() {
        super("MyWebRequestService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String requestString= intent.getStringExtra(REQUEST_STRING);
        String responseString = requestString + " " + DateFormat.format("MM/dd/yy h:mmaa", System.currentTimeMillis());

        SystemClock.sleep(3000); // Wait 3 seconds
        Log.e("responseString", responseString);

        responseMessage=callWebService(requestString);

        Intent broadcastIntent=new Intent();
        broadcastIntent.setAction(PROCESS_RESPONSE);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(RESPONSE_STRING, responseString);
        broadcastIntent.putExtra(RESPONSE_MESSAGE, responseMessage);

        sendBroadcast(broadcastIntent);
    }

    private String callWebService(String requestString) {

        try {

            URL = requestString;
            HttpClient httpclient = new DefaultHttpClient();
            HttpParams params = httpclient.getParams();

            HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, WAIT_TIMEOUT);
            ConnManagerParams.setTimeout(params, WAIT_TIMEOUT);

            HttpGet httpGet = new HttpGet(URL);
            HttpResponse response = httpclient.execute(httpGet);

            StatusLine statusLine = response.getStatusLine();

            /*
            200 - > success
            400 - > failed
             */
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseMessage=out.toString();
                return responseMessage;
            }

            else{
                //Closes the connection.
                Log.w("HTTP1:",statusLine.getReasonPhrase());
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }

        } catch (ClientProtocolException e) {
            Log.w("HTTP2:",e );
            responseMessage = e.getMessage();
            return null;
        } catch (IOException e) {
            Log.w("HTTP3:",e );
            responseMessage = e.getMessage();
            return null;
        }catch (Exception e) {
            Log.w("HTTP4:",e );
            responseMessage = e.getMessage();
            return null;
        }
    }
}
