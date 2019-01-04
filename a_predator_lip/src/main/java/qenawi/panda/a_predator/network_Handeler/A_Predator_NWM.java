package qenawi.panda.a_predator.network_Handeler;

public class A_Predator_NWM {

    public  interface RequistResuiltCallBack

    {
        <T extends CService_DBase> void Sucess(T Resposne);

        void Faild(Throwable error);
    }

}
