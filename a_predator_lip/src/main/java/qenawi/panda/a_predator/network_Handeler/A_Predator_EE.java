package qenawi.panda.a_predator.network_Handeler;

import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
}
