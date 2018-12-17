package qenawi.panda.a_predator.network_Handeler;

import com.google.gson.JsonElement;
import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

import java.util.HashMap;

public interface Retrofit_Api
{
    @GET
    Single<Response<JsonElement>> CserviceGet(@HeaderMap HashMap<String, String> headers, @Url String url, @QueryMap HashMap<String, Object> Data);
}
