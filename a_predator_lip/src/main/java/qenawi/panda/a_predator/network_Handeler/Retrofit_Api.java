package qenawi.panda.a_predator.network_Handeler;

import com.google.gson.JsonElement;
import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.*;

import java.util.HashMap;

public interface Retrofit_Api
{
    @GET
    Single<Response<JsonElement>> A_PredatorGet(@HeaderMap HashMap<String, String> headers, @Url String url, @QueryMap HashMap<String, Object> Data);
    @POST
    Single<Response<JsonElement>> A_PredatorPost(@HeaderMap HashMap<String, String> headers, @Url String url, @Body HashMap<String, Object> Data);



}
