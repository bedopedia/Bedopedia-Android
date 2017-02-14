package Services;

/**
 *
 */


import com.google.gson.*;
import retrofit2.*;
import retrofit2.http.*;
import java.util.*;

public interface ApiInterface {

    @FormUrlEncoded
    @POST
    Call<JsonObject> postServise(@Url String url,@FieldMap Map<String,String> params);

    @GET
    Call<JsonObject> getServise(@Url String url,@QueryMap Map<String,String> params);

}