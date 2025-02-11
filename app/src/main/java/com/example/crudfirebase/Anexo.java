package com.example.crudfirebase;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Clase para la actividad de generación de anexos.
 * Maneja permisos y la firma para los formularios generados.
 */
public class Anexo extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1; // Código de solicitud de permisos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anexo);

        // Verificar y solicitar permisos al iniciar la actividad
        if (!checkPermission()) {
            requestPermission();
        }
    }

    /**
     * Verifica si el permiso de escritura en almacenamiento externo está concedido.
     *
     * @return true si el permiso está concedido, false en caso contrario.
     */
    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Solicita el permiso de escritura en almacenamiento externo.
     * Si el usuario ha denegado el permiso anteriormente, se muestra una explicación.
     */
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Mostrar explicación al usuario
            Toast.makeText(this, getString(R.string.permission_rationale), Toast.LENGTH_LONG).show();
        }
        // Solicitar el permiso
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido
                Toast.makeText(this, getString(R.string.permission_granted), Toast.LENGTH_LONG).show();
            } else {
                // Permiso denegado
                Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_LONG).show();

                // Redirigir al usuario a la configuración de la aplicación si deniega el permiso repetidamente
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    openAppSettings();
                }
            }
        }
    }

    /**
     * Abre la configuración de la aplicación para que el usuario pueda otorgar permisos manualmente.
     */
    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
}