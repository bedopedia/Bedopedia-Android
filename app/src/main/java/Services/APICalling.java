package Services;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ali on 09/02/17.
 */

public class APICalling extends AsyncTask<String, Void, String> {
    private final String LOG_TAG = APICalling.class.getSimpleName();
    @Override
    protected String doInBackground(String... params) {
        // If there's no zip code, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }
        JSONObject parameters = null;
        try {
            if (params[2]!= null)
            parameters = new JSONObject(params[2]) ;
            Log.v("Params2",parameters.toString() );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String responseJsonStr = null;


        try {
            final String API_BASE_URL = params[1];
            Uri builtUri;
            if (params[0].equals("POST")){
                builtUri = Uri.parse(API_BASE_URL).buildUpon()
                        .appendQueryParameter("email", parameters.getString("email"))
                        .appendQueryParameter("password", parameters.getString("password"))
                        .build();
            } else {
                builtUri = Uri.parse(API_BASE_URL).buildUpon()
                        .build();
            }



            URL url = new URL(builtUri.toString());
            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            // Create the request to backend API, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(params[0]);
            urlConnection.connect();


            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            responseJsonStr = buffer.toString();
            JSONObject temp = new JSONObject(responseJsonStr);
            Log.v("token", urlConnection.getHeaderFields().toString());
            Log.v(LOG_TAG,"Json String is" + responseJsonStr);
            Log.v(LOG_TAG,"Response" + urlConnection.getInputStream());
            return responseJsonStr;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);

                }
            }
        }
        return responseJsonStr;
    }
}
