package qenawi.panda.a_predator.network_Handeler;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class A_Predator_NetWorkManger implements LifecycleObserver {
    private Context context;
    private CompositeDisposable disposable;

    public A_Predator_NetWorkManger(Context context) {
        this.context = context;
        disposable = new CompositeDisposable();
        if (context instanceof LifecycleOwner) {
            ((LifecycleOwner) context).getLifecycle().addObserver(this);
            Timber.d("Life Cycle Attached");
        }
    }

    public <T extends A_Predator_NWM.CService_DBase> void FetchData(final T Obj, final HashMap<String, String> Header, final String Url, final HashMap<String, Object> requistBody, final A_Predator_NWM.RequistResuiltCallBack f) {
        disposable.add(SingleToneRetrofit.get_RetrofitCs().CserviceGet(Header, Url, requistBody).subscribeOn(Schedulers.io())
                .timeout(30, TimeUnit.MILLISECONDS, Single.error(new Throwable("Time Out Exception")))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe
                        (
                        sucess ->
                        {

                            f.Sucess(sucess.body());
                        }
                        , error ->
                        {
                            f.Faild(error);
                        }
                ));

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onAttach() {
        Timber.d("Attached");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onDetech() {
        Timber.d("Detached");
        //view is not ready to handel api calls so cancel them
        //AlertDialogHandeler.DismissDialog(null);
        disposable.clear();
    }




    /*
    package com.ebda3.jopiorders.qenawi_trash_hacks.Cservice;

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

import com.ebda3.jopiorders.R;
import com.ebda3.jopiorders.home.log_out_alert_dialog.unAutrized_dialog;
import com.ebda3.jopiorders.member.Sigin_In;
import com.ebda3.jopiorders.network.SingleToneRetrofit;
import com.ebda3.jopiorders.qenawi_trash_hacks.AlertDialogHandeler;
import com.ebda3.jopiorders.utils.Shared_prefs;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import carbon.widget.RelativeLayout;
import carbon.widget.Snackbar;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

/**
 * main target create new instance to each view (Activity/Fragment)
 * get parameter and return json response
 * //**
 * what i want to do is to check for return
 * json if it was valid to be used 1
 * or it contain type of error 2
 * if  1-> return json
 * else 2->
 * error has 2 types
 * 1-?
 * don t  have impact on app flow
 * ex -> missing parameter or data don t exist
 * 2-?
 * have direct impact on app flow
 * ex -> login session expired and need to log again  , no network connection
 * 3-?
 * //
    public class CService implements LifecycleObserver {
        /**
         * second ->1000 ms

        private Context context;
        private Context app_context;
        private Snackbar snackbar;
        private CompositeDisposable disposable;
        private RelativeLayout RootView;
        private static final int TimeOutConnection = 15;


        public CService(Context C) {
            this.context = C;
            app_context = C.getApplicationContext();
            snackbar = null;
            RootView = null;
            disposable = new CompositeDisposable();
            if (C instanceof LifecycleOwner) {
                ((LifecycleOwner) C).getLifecycle().addObserver(this);
                Log.v("Life Cycle Yea", "ATTACHED");
            }
            if (context instanceof AppCompatActivity)
                RootView = ((AppCompatActivity) context).getWindow().getDecorView().findViewById(R.id.container);

        }
        public <T extends CService_DBase> void FetchData(final T Obj, final HashMap<String, String> Header, final String Url, final HashMap<String, Object> requistBody, final CsCallBack f)
        {
            disposable.add(SingleToneRetrofit.<Gson>get_RetrofitCs().CserviceGet(Header, Url, requistBody).subscribeOn(Schedulers.io())
                    .timeout(TimeOutConnection, TimeUnit.SECONDS, Single.error(new SocketTimeoutException(context.getResources().getString(R.string.TimeOutExeption))))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s ->
                            {
                                Log.v("MRQ1.1", s.code() + "...Error");
                                // Status Code 200 / 300
                                if (s.isSuccessful() && CService_EE.IsJasonValid(s.body(), Obj)) {
                                    f.Sucess(new Gson().fromJson(s.body(), Obj.getClass()));
                                } else
                                {
                                    Throwable t = new HttpException(s);
                                    // code range 400 -500
                                    f.Faild(t);
                                }
                            }
                            ,
                            e ->
                            {
                                //HarD( ! )Core Error  - > !@
                                Throwable ret = e;
                                // flat map Throw Data Here
                                Log.v("MRQ1", e.getMessage() + "...Error");
                                if (e instanceof IOException)
                                {
                                    if (e instanceof SocketTimeoutException)
                                    {
                                        ret = new IOException(context.getResources().getString(R.string.TimeOutExeption));
                                    } else
                                    {
                                        ret = new IOException(context.getResources().getString(R.string.PleaseCheckYourConnection));
                                    }
                                    ShowSnack(ret.getMessage());
                                    //Do Action  & Set View To Stop Progress bar ( Done )
                                }
                                else if (e instanceof HttpException)
                                {
                                    // ret = new Throwable(CService_EE.CrackExeption(e));
                                    HttpException httpException = (HttpException) e;
                                    ret=new HttpException(httpException.response());
                                    int ecode = httpException.code();
                                    switch (ecode)
                                    {
                                        case 401:
                                            HandelUnAuthAction();
                                            break;
                                        case 400:
                                            break;
                                    }
                                    // Do Action & Set View To Call Stop Loading
                                }
                                else
                                {
                                    // Idont What The Fuck To Do
                                }
                                f.Faild(ret);
                            }

                    )
            );
        }
        public <T extends CService_DBase> void SendData(final T Obj, HashMap<String, String> Header, String Url, HashMap<String, Object> requistBody, CsCallBack f)
        {
            disposable.add(SingleToneRetrofit.<Gson>get_RetrofitCs().CservicePost(Header, Url, requistBody).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .timeout(TimeOutConnection, TimeUnit.SECONDS, Single.error(new SocketTimeoutException(context.getResources().getString(R.string.TimeOutExeption))))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s ->
                            {
                                Log.v("MRQ1.1", s.code() + "...Error");
                                // Status Code 200 / 300
                                if (s.isSuccessful() && CService_EE.IsJasonValid(s.body(), Obj)) {
                                    f.Sucess(new Gson().fromJson(s.body(), Obj.getClass()));
                                } else
                                {
                                    Throwable t = new HttpException(s);
                                    // code range 400 -500
                                    f.Faild(t);
                                }
                            }
                            ,
                            e ->
                            {
                                //HarD( ! )Core Error  - > !@
                                Throwable ret = e;
                                // flat map Throw Data Here
                                Log.v("MRQ1", e.getMessage() + "...Error");

                                if (e instanceof IOException)
                                {
                                    if (e instanceof SocketTimeoutException)
                                    {
                                        ret = new IOException(context.getResources().getString(R.string.TimeOutExeption));
                                    } else
                                    {
                                        ret = new IOException(context.getResources().getString(R.string.PleaseCheckYourConnection));
                                    }
                                    ShowSnack(ret.getMessage());
                                    //Do Action  & Set View To Stop Progress bar ( Done )
                                }




                                else if (e instanceof HttpException)
                                {
                                    // ret = new Throwable(CService_EE.CrackExeption(e));
                                    HttpException httpException = (HttpException) e;
                                    ret=new HttpException(httpException.response());
                                    int ecode = httpException.code();
                                    switch (ecode)
                                    {
                                        case 401:
                                            HandelUnAuthAction();
                                            break;
                                        case 400:
                                            break;
                                    }
                                    // Do Action & Set View To Call Stop Loading
                                }
                                else
                                {
                                    // Idont What The Fuck To Do
                                }
                                f.Faild(ret);
                            }
                    )
            );
        }
        public interface CsCallBack {
            <T> void Sucess(T Resposne);

            void Faild(Throwable throwable);
        }
        //--------------------------------------------------------
        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void onAttach()

        {
            Log.v("ATTACHED", "ATTACHED");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void onDetech() {
            Log.v("Detached", "ATTACHED");
        /*
        view is not ready to handel api calls so cancel them

            AlertDialogHandeler.DismissDialog(null);
            disposable.clear();
            Snackbar.clearQueue();
        }
        public static boolean isNetworkAvailable(Context context) {
            if (connectedToTheNetwork(context)) {
                try {
                    HttpURLConnection urlc = (HttpURLConnection)
                            (new URL("http://clients3.google.com/generate_204")
                                    .openConnection());
                    urlc.setRequestProperty("User-Agent", "Android");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(1500);
                    urlc.connect();
                    return (urlc.getResponseCode() == 204 &&
                            urlc.getContentLength() == 0);
                } catch (IOException e) {
                    Log.e(CService.class.getName(), "Error checking internet connection", e);
                }
            } else {
                Log.d(CService.class.getName(), "No network available!");
            }
            return false;
        }
        private static boolean connectedToTheNetwork(Context context) {
            try {
                ConnectivityManager connectivityManager
                        = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo != null;
            } catch (Exception e) {
                return false;
            }

        }
        private void ShowSnack(String Msg) {

            snackbar = new Snackbar(context, Msg, 1200);
            snackbar.setStyle(Snackbar.Style.Floating);
            snackbar.setTapOutsideToDismissEnabled(true);
            snackbar.setSwipeToDismissEnabled(true);
            snackbar.setGravity(Gravity.START | Gravity.BOTTOM);
            if (RootView != null)
                snackbar.show(RootView);
        }
        private void HandelUnAuthAction() {
            AlertDialogHandeler.ShowUnAuthDialog(context, new unAutrized_dialog.UnAuthrizedDialogCallBack() {
                @Override
                public void ReAuthrize() {
                    ((AppCompatActivity) context).finishAffinity();
                    ((AppCompatActivity) context).startActivity(new Intent(context, Sigin_In.class));
                    ((AppCompatActivity) context).finish();
                }

                @Override
                public void ExitAPP() {
                    Shared_prefs.save_Loged("out", context);
                    ((AppCompatActivity) context).finishAffinity();
                    ((AppCompatActivity) context).finish();
                }
            }, null);
        }
    }

        //-------------------------NEw BRead-----------------------------------------
    public static CService_Throwable Handel_HttpExeption(HttpException thr) {
        CService_Throwable cService_throwable = new CService_Throwable(thr);
        String ClientMsg = "";
        int code = 0;
        try {
            code = thr.code();
            ResponseBody responseBody = null;
            JsonElement jsonElement = null;
            if (thr.response().body() != null)
            {
                jsonElement = (JsonElement) ((HttpException) thr).response().body();
                Log.v("Tracking", "Normal  Respond Body->" + jsonElement.toString());
                if (jsonElement.getAsJsonObject().has("Message"))
                    ClientMsg = jsonElement.getAsJsonObject().get("Message").getAsString();
                if (jsonElement.getAsJsonObject().has("Code"))
                    code = jsonElement.getAsJsonObject().get("Code").getAsInt();

            } else if (thr.response().errorBody() != null)
            {
                responseBody = ((HttpException) thr).response().errorBody();
                JSONObject jObjError = new JSONObject(responseBody.string());
                 if (jObjError.has("Code"))
                 code = jObjError.getInt("Code");
                 Log.v("Tracking", "Error Respond Code->" + code + " ");
                if (jObjError.has("Errors"))
                {
                    JSONArray jsonArray = jObjError.getJSONArray("Errors");
                    Log.v("Tracking", "Error Respond Body->" + jsonArray.getJSONObject(0).getString("errorMsg"));
                    if (jsonArray.getJSONObject(0).has("errorMsg"))
                    ClientMsg = jsonArray.getJSONObject(0).getString("errorMsg");
                }
                else
                    {
                    Log.v("Tracking", "Error Respond Body->" + jObjError.getString("Message"));
                    ClientMsg = jObjError.getString("Message");
                    }
            }
             else
                    {
                Log.v("Tracking", "Faild To Capture Any Response");
                ClientMsg = "Empty Body Http Exception";
                    }
        } catch (Exception e) {
            e.printStackTrace();
            ClientMsg = "Try Catch Http Exception";
        } finally {
            // ShowToast(ClientMsg);
            cService_throwable.setACtion(ClientMsg);
            cService_throwable.setErrorCode(code);
        }
        return cService_throwable;
    }
/*
=
=
=
-
*/

}
