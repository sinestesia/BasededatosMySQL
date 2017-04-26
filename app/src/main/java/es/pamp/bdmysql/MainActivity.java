package es.pamp.bdmysql;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import static android.provider.ContactsContract.CommonDataKinds.Identity.NAMESPACE;
import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static java.lang.annotation.ElementType.METHOD;

public class MainActivity extends AppCompatActivity {


    static final String SOAPACTION = "http://Hospital/AltaDoctor";
    private static final String METHOD = "altaDoctor";
    private static final String NAMESPACE = "http://Hospital/";
    private static final String URL = "http://10.1.2.108:8084/WSDoctor/AltaDoctor?wsdl";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText hospital_codET = (EditText) findViewById(R.id.hospital_codET);
        final EditText nombreET = (EditText) findViewById(R.id.nombreET);
        final EditText direccionET = (EditText) findViewById(R.id.direccionET);
        final EditText telefonoET = (EditText) findViewById(R.id.telefonoET);
        final EditText num_camaET = (EditText) findViewById(R.id.num_camaET);

        Button guardarBoton = (Button) findViewById(R.id.guardarBoton);
        guardarBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                altaHospital(hospital_codET.getText().toString(),nombreET.getText().toString(), direccionET.getText().toString(),telefonoET.getText().toString(),num_camaET.getText().toString());
            }
        });


    }

    public void altaHospital(String hospital_cod, String nombre, String direccion, String telefono, String num_cama){

        new PeticionAsincrona().execute(hospital_cod, nombre, direccion, telefono, num_cama);

    }

    private class PeticionAsincrona extends AsyncTask<String, Void, String > {

        @Override
        protected String doInBackground(String... params) {
            String resultadoFinal = "KO";

            try {
                /*Lo recomendado es crear esa tarea en un subproceso o hilo secundario,
                no obstante, si necesitáis hacerlo a la fuerza, se puede establecer un cambio en las políticas de restricciones
                de Android para nuestra clase (repito, no es recomendable). Lo único que habría que hacer es insertar estas dos líneas
                 de código en el onCreate() de nuestra clase principal, y Android se tragará cualquier acceso a red que hagamos en el Main Thread, sin rechistar */

                // StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
                //StrictMode.setThreadPolicy(policy);




                //Creacion de la Solicitud
                SoapObject request = new SoapObject(NAMESPACE, METHOD);
                // Creacion del Envelope
                SoapSerializationEnvelope sobre = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                sobre.setOutputSoapObject(request);
                //Creacion del transporte
                HttpTransportSE transporte = new HttpTransportSE(URL);

                // Paso de parámetro
                PropertyInfo hospital_cod = new PropertyInfo();
                hospital_cod.setName("hospital_cod");
                hospital_cod.setValue(params[0]);
                hospital_cod.setType(Integer.class);
                request.addProperty(hospital_cod);

                // Paso de parámetro
                PropertyInfo nombre = new PropertyInfo();
                nombre.setName("nombre");
                nombre.setValue(params[1]);
                nombre.setType(String.class);
                request.addProperty(nombre);

                // Paso de parámetro
                PropertyInfo direccion = new PropertyInfo();
                direccion.setName("direccion");
                direccion.setValue(params[2]);
                direccion.setType(String.class);
                request.addProperty(direccion);



                // Paso de parámetro
                PropertyInfo telefono = new PropertyInfo();
                telefono.setName("telefono");
                telefono.setValue(params[3]);
                telefono.setType(String.class);
                request.addProperty(telefono);

                // Paso de parámetro
                PropertyInfo num_cama = new PropertyInfo();
                num_cama.setName("num_cama");
                num_cama.setValue(params[4]);
                num_cama.setType(Integer.class);
                request.addProperty(num_cama);

                //Llamada
                transporte.call(SOAPACTION, sobre);

                //Resultado
                SoapPrimitive resultado = (SoapPrimitive) sobre.getResponse();
                resultadoFinal = "OK";
            }
            catch (Exception e) {
                resultadoFinal = "KO";
            }

            return resultadoFinal;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            if (s.equals("OK")){
                Toast toastBien = Toast.makeText(getApplicationContext(),"Guardado correctamente", Toast.LENGTH_SHORT);
                toastBien.show();
            }else{
                Toast toastMal = Toast.makeText(getApplicationContext(),"Error al guardar", Toast.LENGTH_SHORT);
                toastMal.show();
            }

        }
    }

}
