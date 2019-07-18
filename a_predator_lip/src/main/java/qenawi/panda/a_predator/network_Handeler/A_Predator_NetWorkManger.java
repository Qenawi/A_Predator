package qenawi.panda.a_predator.network_Handeler;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.RelativeLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import qenawi.panda.a_predator.R;
import retrofit2.HttpException;
import timber.log.Timber;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static qenawi.panda.a_predator.network_Handeler.A_Predator_EE.Handel_HttpExeption;

/**
 * * main target create new instance to each view (Activity/Fragment)
 * * get parameter and return json response
 * * //**
 * * what i want to do is to check for return
 * * json if it was valid to be used 1
 * * or it contain type of error 2
 * * if  1-> return json
 * * else 2->
 * * error has 2 types
 * * 1-?
 * * don t  have impact on app flow
 * * ex -> missing parameter or data don t exist
 * * 2-?
 * * have direct impact on app flow
 * * ex -> login session expired and need to log again  , no network connection
 * * 3-?
 * * //
 */

public class A_Predator_NetWorkManger implements LifecycleObserver {
    public static final int UnAuthCode = 401;
    private static final String TAG=A_Predator_NetWorkManger.class.getSimpleName();
    /**
     * second ->1000 ms
     */
    private Context context;
    private CompositeDisposable disposable;
    private RelativeLayout RootView;
    private A_Predator_NWM.BaseActionHandeler baseActionHandeler;
    private static final int TimeOutConnection = 15;

    public A_Predator_NetWorkManger(Context C, @NotNull A_Predator_NWM.BaseActionHandeler handeler) {
        this.context = C;
        disposable = new CompositeDisposable();
        if (C instanceof LifecycleOwner) {
            ((LifecycleOwner) C).getLifecycle().addObserver(this);
            Timber.tag(TAG).v("Life Cycle Yea ATTACHED");
        }
        baseActionHandeler = handeler;

    }

    final public <T extends CService_DBase> void FetchData(final T Obj, final HashMap<String, String> Header, final String Url, final HashMap<String, Object> requistBody, final A_Predator_NWM.RequistResuiltCallBack f) {
        disposable.add(SingleToneRetrofit.<Gson>get_RetrofitCs().A_PredatorGet(Header, Url, requistBody).subscribeOn(Schedulers.io())
                .timeout(TimeOutConnection, TimeUnit.SECONDS, Single.error(new SocketTimeoutException(context.getResources().getString(R.string.TimeOutExeption))))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s ->
                        {
                            Log.v("CserviceSucess", s.code() + "..");
                            // Status Code 200 / 300
                            if (s.isSuccessful() && A_Predator_EE.IsJasonValid(s.body(), Obj)) {
                                f.Sucess(new Gson().fromJson(s.body(), Obj.getClass()));
                            } else {
                                Throwable t = new HttpException(s);
                                // code range 400 -500
                                A_Predator_Throwable res = RetrofitThr(t);
                                f.Faild(res);
                            }
                        }
                        ,
                        e ->
                        {
                            A_Predator_Throwable cService_throwable_callBack = RetrofitThr(e);
                            f.Faild(cService_throwable_callBack);
                        }

                )
        );
    }

    final public <T extends CService_DBase> void SendData(final T Obj, HashMap<String, String> Header, String Url, HashMap<String, Object> requistBody, A_Predator_NWM.RequistResuiltCallBack f) {
        disposable.add(SingleToneRetrofit.<Gson>get_RetrofitCs().A_PredatorPost(Header, Url, requistBody).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .timeout(TimeOutConnection, TimeUnit.SECONDS, Single.error(new SocketTimeoutException(context.getResources().getString(R.string.TimeOutExeption))))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s ->
                        {
                            Log.v("CserviceSucess", s.code() + "..");
                            // Status Code 200 / 300
                            if (s.isSuccessful() && A_Predator_EE.IsJasonValid(s.body(), Obj)) {
                                f.Sucess(new Gson().fromJson(s.body(), Obj.getClass()));
                            } else {
                                Throwable t = new HttpException(s);
                                // code range 400 -500
                                A_Predator_Throwable res = RetrofitThr(t);
                                f.Faild(res);
                            }
                        }
                        ,
                        e ->
                        {
                            A_Predator_Throwable cService_throwable_callBack = RetrofitThr(e);
                            f.Faild(cService_throwable_callBack);
                        }
                )
        );
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onAttach()

    {
        Timber.tag(TAG).v("ATTACHED");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onDetech() {
        Timber.tag(TAG).v("Detached");
        /*
        view is not ready to handel api calls so cancel them
         */
        baseActionHandeler.DetacHHandelers();
        disposable.clear();
    }

    private static boolean connectedToTheNetwork(Context context)
    {
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = null;
            if (connectivityManager != null)
            {
                activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            }
            return activeNetworkInfo != null;
        } catch (Exception e)
        {
            return false;
        }
    }

    private void ShowSnack(String Msg) {
        baseActionHandeler.ShowSnackBar(Msg);
    }

    private void HandelUnAuthAction() {
        baseActionHandeler.HandelUnAuthAction();
    }

    private A_Predator_Throwable RetrofitThr(Throwable e)
    {
        //HarD( ! )Core Error  - > !@
        A_Predator_Throwable res;
        // flat map Throw Data Here
        Timber.tag(TAG).v(e);
        if (e instanceof IOException) {
            res = new A_Predator_Throwable(e);
            if (e instanceof SocketTimeoutException)
            {
                res.setACtion(context.getResources().getString(R.string.TimeOutExeption));
            } else {
                res.setACtion(context.getResources().getString(R.string.PleaseCheckYourConnection));
            }
            //res default do action false
            ShowSnack(res.getACtion());
            //Do Action  & Set View To Stop Progress bar ( Done )
        }// IO EX
        else if (e instanceof HttpException)
        {
            res = Handel_HttpExeption((HttpException) e, context);
            switch (res.getErrorCode()) {
                case UnAuthCode:
                    res.setDoAction(false);
                    HandelUnAuthAction();
                    break;
            }// Do Action & Set View To Call Stop Loading

        } // Http ExP
        else {
            res = new A_Predator_Throwable(e);
            res.setACtion(context.getResources().getString(R.string.EmptyBodyHttpException));
            res.setDoAction(true);// Show Toast With Default Msg
        } // New Error Type
        return res;
    }

}
