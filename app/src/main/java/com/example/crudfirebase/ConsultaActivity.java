package com.example.crudfirebase;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Clase encargada de la consulta de datos en Firebase.
 * Recupera la información del formulario Anexo para la generación del PDF.
 */
public class ConsultaActivity extends AppCompatActivity {

    private static final String TAG = "ConsultaActivity"; // Etiqueta para logs
    private DatabaseReference databaseReference; // Referencia a la base de datos de Firebase
    private TextView textViewDatos; // TextView para mostrar los datos
    private ProgressBar progressBar; // ProgressBar para indicar carga

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        // Inicialización de vistas
        textViewDatos = findViewById(R.id.textViewDatos);
        progressBar = findViewById(R.id.progressBar);

        // Verificar si las vistas están correctamente inicializadas
        if (textViewDatos == null || progressBar == null) {
            Log.e(TAG, "Error: Las vistas no están correctamente definidas en el layout.");
            return;
        }

        // Inicializar la referencia a Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("formularios");

        // Obtener el número de embarque del intent
        String fichaFormulario = getIntent().getStringExtra(getString(R.string.no_embarque));

        // Verificar si el número de embarque es válido
        if (fichaFormulario != null && !fichaFormulario.isEmpty()) {
            Log.i(TAG, getString(R.string.consultar_anexos) + fichaFormulario);
            progressBar.setVisibility(View.VISIBLE); // Mostrar ProgressBar
            obtenerDatosFormulario(fichaFormulario); // Consultar datos en Firebase
        } else {
            Log.e(TAG, getString(R.string.no));
            textViewDatos.setText(getString(R.string.no_at)); // Mostrar mensaje de error
        }
    }

    /**
     * Consulta los datos del formulario en Firebase.
     *
     * @param fichaFormulario Número de embarque del formulario a consultar.
     */
    private void obtenerDatosFormulario(String fichaFormulario) {
        databaseReference.child(fichaFormulario).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE); // Ocultar ProgressBar

                if (dataSnapshot.exists()) {
                    // Obtener los datos del formulario
                    DatosFormulario datosFormulario = dataSnapshot.getValue(DatosFormulario.class);

                    if (datosFormulario != null) {
                        mostrarDatos(datosFormulario); // Mostrar los datos en la interfaz
                    } else {
                        // Error al convertir los datos
                        textViewDatos.setText(getString(R.string.error_conversion_datos));
                    }
                } else {
                    // No se encontraron datos
                    textViewDatos.setText(getString(R.string.datos_no_encontrados));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE); // Ocultar ProgressBar
                // Error en la consulta
                textViewDatos.setText(getString(R.string.error_consulta_firebase));
                Log.e(TAG, "Error en la consulta a Firebase: " + databaseError.getMessage());
            }
        });
    }

    /**
     * Muestra los datos del formulario en el TextView.
     *
     * @param datosFormulario Objeto con los datos del formulario.
     */
    private void mostrarDatos(DatosFormulario datosFormulario) {
        // Formatear los datos para mostrarlos en el TextView
        String datos = getString(R.string.fecha) + ": " + datosFormulario.getFecha() + "\n"
                + getString(R.string.no_embarque) + ": " + datosFormulario.getNumeroEmbarque() + "\n"
                + getString(R.string.clave_cliente) + ": " + datosFormulario.getClaveCliente() + "\n"
                + getString(R.string.razon_social) + ": " + datosFormulario.getRazonSocial();
        textViewDatos.setText(datos); // Mostrar los datos
    }
}