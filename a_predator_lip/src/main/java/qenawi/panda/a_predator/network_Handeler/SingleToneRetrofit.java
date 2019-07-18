package qenawi.panda.a_predator.network_Handeler;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class SingleToneRetrofit
{
    private static final String BaseUrl = "https://api.themoviedb.org/3/";
    private static Retrofit_Api RetroSingle_Tone =null;
    public static Retrofit_Api get_RetrofitCs()
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);
        if (RetroSingle_Tone!=null){return RetroSingle_Tone;}
        else
        {
            RetroSingle_Tone=new Retrofit.Builder().baseUrl(BaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(builder.build())
                    .build().create(Retrofit_Api.class);
            return RetroSingle_Tone;
        }
    }
}
