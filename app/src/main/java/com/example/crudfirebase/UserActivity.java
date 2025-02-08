package com.example.crudfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity {

    private Button btnGenerarAnexo, btnConsultarAnexos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Inicializar los botones
        btnGenerarAnexo = findViewById(R.id.buttonGenerarAnexo);
        btnConsultarAnexos = findViewById(R.id.buttonConsultarAnexos);

        // Configurar el botón para generar anexo
        btnGenerarAnexo.setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, Anexo.class);
            String nombreConductor = getIntent().getStringExtra("NOMBRE_CONDUCTOR");
            if (nombreConductor != null) {
                intent.putExtra("NOMBRE_CONDUCTOR", nombreConductor);
            }
            startActivity(intent);
        });

        // Configurar el botón para consultar anexos
        btnConsultarAnexos.setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, ConsultaAnexos.class);
            startActivity(intent);
        });
    }
}