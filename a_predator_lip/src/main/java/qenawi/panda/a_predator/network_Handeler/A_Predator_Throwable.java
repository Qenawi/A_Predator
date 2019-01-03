package qenawi.panda.a_predator.network_Handeler;

public class A_Predator_Throwable extends Exception
{
    public A_Predator_Throwable(Throwable throwable) {
        super(throwable);
    }

    public A_Predator_Throwable(String msg) {
        super(msg);
    }

    public A_Predator_Throwable(String msg, Throwable t) {
        super(msg, t);
    }

    public String getWrapedMesg() {
        return WrapedMesg;
    }

    public void setWrapedMesg(String wrapedMesg) {
        WrapedMesg = wrapedMesg;
    }

    private String WrapedMesg = null;
    private int ErrorCode = 200;

    public int getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(int errorCode) {
        ErrorCode = errorCode;
    }
}
