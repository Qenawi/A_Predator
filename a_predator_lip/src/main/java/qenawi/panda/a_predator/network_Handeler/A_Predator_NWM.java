package qenawi.panda.a_predator.network_Handeler;

public class A_Predator_NWM {

    public interface RequistResuiltCallBack

           {
        <T> void Sucess(T Resposne);

         void Faild(Throwable error);
            }
    public abstract class CService_DBase
    {
        public boolean Is_Data_Good() {
            return false;
        }

        ;
    }

}
