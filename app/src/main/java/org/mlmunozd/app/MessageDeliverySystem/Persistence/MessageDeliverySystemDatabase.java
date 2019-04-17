package org.mlmunozd.app.MessageDeliverySystem.Persistence;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = MessageDeliverySystemDatabase.NAME, version = MessageDeliverySystemDatabase.VERSION)
public class MessageDeliverySystemDatabase {
    public static final String NAME = "MessageDeliverySystemDatabase";
    public static final int VERSION = 1;
}
