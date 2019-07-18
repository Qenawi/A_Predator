package qenawi.panda.a_predator.network_Handeler;

import android.text.TextUtils;

public class A_Predator_Throwable extends Exception
{
    /*
    Custom Throwable  To Add More Options To nw Calls And Exception Handling
     */
    public A_Predator_Throwable(Throwable throwable)
    {
        super(throwable);
    }
    public A_Predator_Throwable(String msg)
    {
        super(msg);
    }
    public A_Predator_Throwable(String msg,Throwable t) {
        super(msg,t);
    }
    private String ACtion =A_PredatorHelper.ActionString.Action_Default ;
    private int ErrorCode=A_PredatorHelper.ErrorCode.CODE_Default;
    private boolean DoAction=false;

    public boolean GetDoAction()
    {
        return DoAction;
    }

    public void setDoAction(boolean doAction) {
        DoAction = doAction;
    }

    public void setACtion(String ACtion)
    {
        if (ACtion!=null&&!TextUtils.isEmpty(ACtion))
            this.ACtion = ACtion;
    }
    public String getACtion(){ return ACtion; }

    public int getErrorCode()
    {
        return ErrorCode;
    }
    public void setErrorCode(int errorCode)
    {
        ErrorCode = errorCode;
    }
}
