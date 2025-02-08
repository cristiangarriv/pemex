package com.example.crudfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    private Button buttonActualizarDatos, buttonEliminarRegistros, buttonVerEmpleados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        buttonActualizarDatos = findViewById(R.id.buttonActualizarDatos);
        buttonEliminarRegistros = findViewById(R.id.buttonEliminarRegistros);
        buttonVerEmpleados = findViewById(R.id.buttonVerEmpleados);

        buttonActualizarDatos.setOnClickListener(view -> {
            // Redirigir a la actividad que muestra los empleados registrados
            Intent intent = new Intent(AdminActivity.this, ConsultaActivity.class);
            startActivity(intent);
        });

        buttonEliminarRegistros.setOnClickListener(view -> {
            // Redirigir a la actividad para borrar registros
            Intent intent = new Intent(AdminActivity.this, BorrarActivity.class);
            startActivity(intent);
        });

        buttonVerEmpleados.setOnClickListener(view -> {
            // Redirigir a la actividad de consulta
            Intent intent = new Intent(AdminActivity.this, ConsultaActivity.class);
            startActivity(intent);
        });
    }
}