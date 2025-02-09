package com.example.crudfirebase;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.gcacace.signaturepad.views.SignaturePad;

/**
 * Clase Anexo para la actividad de generación y manejo de anexos.
 */
public class Anexo extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;

    private Button btnGenerar;
    private Button btnClear;
    private SignaturePad signaturePad;
    private EditText fecha, numeroEmbarque, claveCliente, razonSocial, direccionEntrega, nombreEs, permisoCre, permisoCreConfirm, noAt, volumenNetoInicial, volumenNetoFinal, volumenTotalDescargado, nombreConductor, noAtConflict, observaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anexo);

        btnGenerar = findViewById(R.id.btn_export_pdf);
        btnClear = findViewById(R.id.btn_clear);
        signaturePad = findViewById(R.id.signature_pad_conflict);

        fecha = findViewById(R.id.fecha);
        numeroEmbarque = findViewById(R.id.numero_embarque);
        claveCliente = findViewById(R.id.clave_cliente);
        razonSocial = findViewById(R.id.razon_social);
        direccionEntrega = findViewById(R.id.direccion_entrega);
        nombreEs = findViewById(R.id.nombre_es);
        permisoCre = findViewById(R.id.permiso_cre);
        permisoCreConfirm = findViewById(R.id.permiso_cre_confirm);
        noAt = findViewById(R.id.no_at);
        volumenNetoInicial = findViewById(R.id.volumen_neto_inicial);
        volumenNetoFinal = findViewById(R.id.volumen_neto_final);
        volumenTotalDescargado = findViewById(R.id.volumen_total_descargado);
        nombreConductor = findViewById(R.id.nombre_conductor);
        noAtConflict = findViewById(R.id.no_at_conflict);
        observaciones = findViewById(R.id.observaciones);

        btnGenerar.setOnClickListener(v -> {
            if (checkPermission()) {
                GeneradorPdf generadorPdf = new GeneradorPdf(Anexo.this, signaturePad);
                generadorPdf.generarPDF();
            } else {
                requestPermission();
            }
        });

        btnClear.setOnClickListener(v -> signaturePad.clear());
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Write External Storage permission allows us to create PDF. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted, Now you can create PDF.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Permission Denied, You cannot create PDF.", Toast.LENGTH_LONG).show();
            }
        }
    }
}