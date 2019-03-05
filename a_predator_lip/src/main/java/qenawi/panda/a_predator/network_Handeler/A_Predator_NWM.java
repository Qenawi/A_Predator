package qenawi.panda.a_predator.network_Handeler;

public class A_Predator_NWM
{
    /*
    Interfaces to Access NW and Customize its Actions
     */

    public  interface RequistResuiltCallBack

    {
        <T extends CService_DBase> void Sucess(T Resposne);

        void Faild(A_Predator_Throwable error);
    }
    public interface BaseActionHandeler
    {
        void ShowSnackBar(String t);
        void HandelUnAuthAction();
        void DetacHHandelers();
    }

}
