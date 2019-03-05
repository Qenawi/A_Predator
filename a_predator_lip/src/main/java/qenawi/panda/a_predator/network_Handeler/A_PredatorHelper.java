package qenawi.panda.a_predator.network_Handeler;

import android.content.Context;
import qenawi.panda.a_predator.R;

class A_PredatorHelper {
    public static class ErrorCode
    {
        static final int CODE_Default = 1994;
        static final int CODE_400_BadArguments = 400;
        static final int CODE_401_Authentication = 401;
        static final int CODE_406_UNAcceptable = 406;
        static final int CODE_1995_TimeOutConnection = 1995;
        static final int CODE_1998_NO_NETWORK_AVAILIBLE = 1998;
        static final int CODE_500 = 500;
    }
    public static class ActionString
    {
        static final String Action_1998_NO_NETWORK_AVAILIBLE ="NO_NETWORK_AVAILABLE";
        static final String Action_Default = "DefaultAction";
        static final String Action_1995_TimeOutConnection = "TimeOutAction";
        static final String Action_401_Authentication = "Authentication";
        static final String Action_400_BadArguments = "BadArguments";
        static final String Action_406_UNAcceptable = "UNAcceptable";
    }
    public static class C_Convertor
    {
        static String ActionToMsg(final Context c, final String Action) {
            String Msg;
            switch (Action) {
                case ActionString.Action_1995_TimeOutConnection:
                    Msg = c.getResources().getString(R.string.TimeOutExeption);
                    break;
                default:
                    Msg = ActionString.Action_Default;

            }
            return Msg;
        }

        public static String CodeToMsg(final Context c, final int Code) {
            String Msg;
            switch (Code) {
                case ErrorCode.CODE_1995_TimeOutConnection:
                    Msg = c.getResources().getString(R.string.TimeOutExeption);
                    break;
                default:
                    Msg = ActionString.Action_Default;
            }
            return Msg;
        }
    }
    public static A_Predator_Throwable Throwable_Maper(Throwable throwable) {
        A_Predator_Throwable A_Predator_ThrowableResponse = new A_Predator_Throwable(throwable);
        if (throwable.getMessage().contains("401")) {
            A_Predator_ThrowableResponse.setErrorCode(ErrorCode.CODE_401_Authentication);
            A_Predator_ThrowableResponse.setACtion(ActionString.Action_401_Authentication);
        } else if (throwable.getMessage().contains("400")) {
            A_Predator_ThrowableResponse.setErrorCode(400);
            A_Predator_ThrowableResponse.setACtion(ActionString.Action_400_BadArguments);
        } else if (throwable.getMessage().contains("406")) {
            A_Predator_ThrowableResponse.setErrorCode(406);
            A_Predator_ThrowableResponse.setACtion(ActionString.Action_406_UNAcceptable);
        }
        return A_Predator_ThrowableResponse;
    }
}
