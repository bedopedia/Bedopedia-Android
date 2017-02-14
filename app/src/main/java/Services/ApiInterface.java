package Services;

/**
 *
 */


import com.google.gson.*;
import retrofit2.*;
import retrofit2.http.*;
import java.util.*;

public interface ApiInterface {

    @GET
    Call<JsonObject> getServise(@Url String url,@QueryMap Map<String,String> params);

    @GET
    Call<ArrayList<JsonObject> > getServiseArr(@Url String url,@QueryMap Map<String,String> params);

    @FormUrlEncoded
    @POST
    Call<JsonObject> postServise(@Url String url,@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST
    Call<ArrayList<JsonObject>> postServiseArr(@Url String url,@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @PUT
    Call<JsonObject> putServise(@Url String url,@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @PUT
    Call<ArrayList<JsonObject>> putServiseArr(@Url String url,@FieldMap Map<String,String> params);





}