package qenawi.panda.a_predator.network_Handeler;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import io.reactivex.Scheduler;
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


}
