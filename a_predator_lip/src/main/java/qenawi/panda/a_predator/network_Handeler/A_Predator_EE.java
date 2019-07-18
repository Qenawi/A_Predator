package qenawi.panda.a_predator.network_Handeler;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import qenawi.panda.a_predator.R;
import retrofit2.HttpException;
import timber.log.Timber;

import java.io.IOException;

/**
 * CService Error Extractor Class
 * Target
 * (1)
 * Check Json for its Validation for use
 * or it contain error
 * (2)
 * Get Error Type $2 types
 * 2 .1 view can handel it-> so pass Throwable or txt to view to show toast and handel error
 * 2 .2 error will impact main app flow (nO Connection or  authentication problem )
 */
class A_Predator_EE {
    static <T extends CService_DBase> Boolean IsJasonValid(JsonElement json, T Pattetn) {
        if (json != null && !json.isJsonNull()) {
            return CheckPAttern((T) Pattetn, json);
        }

        return false;
    }

    private static <T extends CService_DBase> Boolean CheckPAttern(@NotNull T Pattern, @NotNull JsonElement e)
    {
        T lol;
        try
        {
            lol = (T) (new Gson().fromJson(e, Pattern.getClass()));

        }catch (Exception ex)
        {
            lol = null;
        }
        Timber.tag("Pattern->").v(Pattern.getClass().getName() + " / " + lol.Is_Data_Good());
        return lol.Is_Data_Good();
    }
    protected static A_Predator_Throwable Handel_HttpExeption(HttpException thr, Context c)
    {

        A_Predator_Throwable Service_throwable = new A_Predator_Throwable(thr);
        String ClientMsg = "";
        int code = 0;
        try {
            code = thr.code();                 // secondary code
            ResponseBody responseBody = null; // retrofit Response Body
            JsonElement jsonElement = null;  // json response
            if (thr.response().body() != null)
            {
                //code  h200 - h300
                jsonElement = (JsonElement) ((HttpException) thr).response().body();
                Log.v("Tracking", "Normal  Respond Body->" + jsonElement.toString());
                if (jsonElement.getAsJsonObject().has("Message"))
                    ClientMsg = jsonElement.getAsJsonObject().get("Message").getAsString();
                if (jsonElement.getAsJsonObject().has("Code"))
                    code = jsonElement.getAsJsonObject().get("Code").getAsInt();
            } // Body Extraction
            else if (thr.response().errorBody() != null)
            {   // code h400-h500
                responseBody = ((HttpException) thr).response().errorBody();
                JsonParser jsonParser=new JsonParser();
                String extra=responseBody.string();
                String Raw=jsonParser.parse(extra).toString();
                Log.v("Tracking_Response",Raw+" ,Raw");
                JSONObject jObjError = new JSONObject(Raw);
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
            }//error Body Extraction
            else
            {
                Log.v("Tracking", "Faild To Capture Any Response"+" EmptyBodyHttpException");
                ClientMsg =c.getResources().getString(R.string.EmptyBodyHttpException);
            }//  EmptyBodyHttpException
        } catch (Exception e)
        {
            Log.v("Tracking", "Faild To Capture Any Response"+"");
            e.printStackTrace();
            ClientMsg =c.getResources().getString(R.string.ParsingBodyExeption);
        }// Exception
        finally
        {
            // ShowToast(ClientMsg);
            Service_throwable.setACtion(ClientMsg);
            Service_throwable.setErrorCode(code);
            Service_throwable.setDoAction(true);// Show Toast
        }
        Log.v("Tracking", "Show"+Service_throwable.GetDoAction()+" ");

        return Service_throwable;
    }
}
