package Services;

import org.json.JSONObject;

/**
 * Created by mohamed on 2/9/17.
 */

public class BackEnd {

    private static BackEnd instance = new BackEnd();

    private BackEnd() {}

    public static BackEnd getInstance() {
        return instance;
    }
    public String get(String url, String params) {
        APICalling apiCalling = new APICalling();
        return apiCalling.execute("GET", url, params).toString();
    }

    public String post(String url, String params) {
        APICalling apiCalling = new APICalling();
        return apiCalling.execute("POST", url, params).toString();
    }
}
