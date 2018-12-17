package qenawi.panda.a_predator.Demo;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import qenawi.panda.a_predator.R;
import qenawi.panda.a_predator.network_Handeler.A_Predator_NetWorkManger;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
     {
    A_Predator_NetWorkManger predator_netWorkManger;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        predator_netWorkManger = new A_Predator_NetWorkManger(this);
    }

    }
