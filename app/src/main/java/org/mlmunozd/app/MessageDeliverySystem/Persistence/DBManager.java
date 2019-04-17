package org.mlmunozd.app.MessageDeliverySystem.Persistence;

import android.app.Application;
import com.raizlabs.android.dbflow.config.FlowManager;

/*
 * Class inicialize the libray DFlow in the class Application
 */
public class DBManager extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
    }
}
