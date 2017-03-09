package Services;

/**
 * momen
 */

import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {


    public static String BASE_URL = "";

    private static Retrofit retrofit = null;


    public static Retrofit getClient(SharedPreferences sharedPreferences) {

        final String accessToken = sharedPreferences.getString("header_access-token", "");
        final String tokenType = sharedPreferences.getString("header_token-type", "");
        final String clientCode = sharedPreferences.getString("header_client", "");
        final String uid = sharedPreferences.getString("header_uid", "");

        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request()
                        .newBuilder().addHeader("access-token", accessToken)
                                .addHeader("uid", uid)
                                 .addHeader("client", clientCode)
                                .addHeader("token-type", tokenType).build();
                return chain.proceed(newRequest);
            }
        };

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(interceptor);
        OkHttpClient client = builder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }

}
