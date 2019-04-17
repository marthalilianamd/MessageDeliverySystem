package org.mlmunozd.app.MessageDeliverySystem.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mlmunozd.app.MessageDeliverySystem.Logic.Mensaje;
import org.mlmunozd.app.MessageDeliverySystem.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MensajesFragment extends Fragment implements OnItemClickListener {

    View viewFragMensajes;
    RecyclerView recyclerview;
    private MensajeAdapter mensajeAdapter;
    public static final Mensaje sMensaje= new Mensaje();

    public MensajesFragment() {
        // Required empty public constructors
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (viewFragMensajes != null) {
            ViewGroup parent = (ViewGroup) viewFragMensajes.getParent();
            if (parent != null){
                parent.removeView(viewFragMensajes);
            }
        }
        viewFragMensajes= inflater.inflate(R.layout.fragment_mensajes, container, false);
        return viewFragMensajes;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerview = view.findViewById(R.id.recycleview);

        configAdpater();
        configRecyclerView();

        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=37.3914105,-5.9591776&radius=1500&type=Mensaje&key=AIzaSyBOTR1kAzmAQ9FV7M9n7qvy7_irmi_DBCs";
        CargarJsonTask tarea = new CargarJsonTask();
        tarea.execute(url);
    }

    private void configAdpater() {
        mensajeAdapter = new MensajeAdapter(new ArrayList<Mensaje>(),this);
    }

    //podemos configurarlo como un listado o un grid, en este caso un linearLayout
    private void configRecyclerView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(mensajeAdapter);
    }


    private class CargarJsonTask extends AsyncTask<String, Integer, Boolean> {
        JSONObject jsonResponse;
        protected Boolean doInBackground(String... params) {
            boolean resul = false;
            String StringResponse;
            try {
                StringResponse = get(params[0]);
                jsonResponse = new JSONObject(StringResponse);
                resul = true;
            } catch (JSONException e) {
                Log.d("JSON ", e.getLocalizedMessage());
            }
            return resul;
        }
        private String get(String url) {
            InputStream inputStream = null;
            String result = "";
            try {
                // create HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                // make GET request to the given URL
                HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
                //HttpResponse httpResponse = httpclient.execute(new HttpGet("http://services.groupkt.com/state/get/ESP/all");
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = httpResponse.getEntity();
// receive response as inputStream
                    if (entity != null) {
                        inputStream = httpResponse.getEntity().getContent();
// convert inputstream to string
                        if (inputStream != null)
                            result = convertInputStreamToString(inputStream);
                        else
                            result = "Fallo al leer iostream!";
                    } else
                        result = "Fallo al leer entity";
                } else
                    result = "Fallo: Error " +
                            httpResponse.getStatusLine().getStatusCode();
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return result;
        }
        private String convertInputStreamToString(InputStream inputStream) throws
                IOException {
            BufferedReader bufferedReader = new BufferedReader(new
                    InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null)
                result += line;
            inputStream.close();
            return result;
        }
        protected void onPostExecute(Boolean resul) {
            try {
                JSONArray jsonAResultadoArray = jsonResponse.getJSONArray("results");
                for (int i = 0; i < jsonAResultadoArray.length(); i++) {
                    Mensaje mensajeActual = new Mensaje();
                    JSONObject MensajeJsonObj = new JSONObject(jsonAResultadoArray.get(i).toString());

                    if(MensajeJsonObj.has("titulo"))
                        mensajeActual.setTitulo(MensajeJsonObj.getString("titulo"));
                    if(MensajeJsonObj.has("icon"))
                        mensajeActual.setIcon(MensajeJsonObj.getString("icon"));
                    mensajeAdapter.addMensaje(mensajeActual);
                }

            } catch (Exception e) {
                Toast.makeText(getContext(), "Hubo un error, perdone las molestias", Toast.LENGTH_SHORT).show();
                System.out.println("Excepcion aqui: " + e.toString());
            }
        }
    }

    /*
     *Metodos implementados por la interface OnItemClickListener
     */
    @Override
    public void OnItemClick(Mensaje mensaje) {
        sMensaje.setTitulo(mensaje.getTitulo());
        sMensaje.setIcon(mensaje.getIcon());

        MensajeDetailsFragment mensajeDetailsFragment = new MensajeDetailsFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_layout, mensajeDetailsFragment, "fragmentDetailsMensaje")
                .setTransition(transaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack("fragmentDetailsMensaje");
        transaction.commit();
    }

    @Override
    public void onLongItemClick(Mensaje mensaje) {

    }
}
