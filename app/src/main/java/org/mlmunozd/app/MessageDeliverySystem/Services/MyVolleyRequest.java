package org.mlmunozd.app.MessageDeliverySystem.Services;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

public class MyVolleyRequest {

    private static MyVolleyRequest mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;
    public static final int MY_DEFAULT_TIMEOUT = 7000;

    private MyVolleyRequest(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized MyVolleyRequest getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyVolleyRequest(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(mCtx.getApplicationContext().getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.

            //mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        /*req.setRetryPolicy(new DefaultRetryPolicy(
                MY_DEFAULT_TIMEOUT, //tiempo de espera del socket en milésimas por cada intento de reintento.
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, //Número de intentos de repetición.
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)); //tiempo exponencial establecido en socket para cada intento de reintento*/
        getRequestQueue().add(req);
    }
}
