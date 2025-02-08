package com.example.crudfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private EditText txtFicha, txtContrasena;
    private Button btnLogin, btnRegistrar, btnVerEmpleados;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar los componentes
        txtFicha = findViewById(R.id.txtFicha);
        txtContrasena = findViewById(R.id.txtContrasena);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnVerEmpleados = findViewById(R.id.btnVerEmpleados);

        // Inicializar Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("empleados");

        // Configurar el botón de inicio de sesión
        btnLogin.setOnClickListener(v -> {
            String ficha = txtFicha.getText().toString().trim();
            String contrasena = txtContrasena.getText().toString().trim();

            if (ficha.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(Login.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                if (ficha.equals("0000") && contrasena.equals("admin")) {
                    // Inicio de sesión como admin
                    Toast.makeText(Login.this, "Inicio de sesión como admin", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, AdminActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Inicio de sesión como usuario normal
                    iniciarSesion(ficha, contrasena);
                }
            }
        });

        // Configurar el botón de registro
        btnRegistrar.setOnClickListener(v -> {
            // Redirigir a la actividad MainActivity para el formulario de registro de empleado
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        });

        // Configurar el botón para ver empleados registrados
        btnVerEmpleados.setOnClickListener(v -> {
            // Redirigir a la actividad Ver
            Intent intent = new Intent(Login.this, Ver.class);
            startActivity(intent);
        });

        // Ocultar el botón de ver empleados para usuarios normales
        btnVerEmpleados.setVisibility(View.GONE);
    }

    private void iniciarSesion(String ficha, String contrasena) {
        Log.d("LoginDebug", "Iniciando sesión con ficha: " + ficha + " y contraseña: " + contrasena);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean found = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Empleados.Empleado empleado = snapshot.getValue(Empleados.Empleado.class);
                    if (empleado != null) {
                        Log.d("LoginDebug", "Ficha en la base de datos: " + empleado.getFicha());
                        Log.d("LoginDebug", "Contraseña en la base de datos: " + empleado.getContrasena());
                        if (String.valueOf(empleado.getFicha()).equals(ficha)) {
                            found = true;
                            if (empleado.getContrasena().equals(contrasena)) {
                                Log.d("LoginDebug", "Inicio de sesión exitoso para ficha: " + ficha);
                                Toast.makeText(Login.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                                // Obtener nombres y apellidos del empleado
                                String nombres = empleado.getNombres();
                                String apellidos = empleado.getApellidoPaterno() + " " + empleado.getApellidoMaterno();
                                String nombreCompleto = nombres + " " + apellidos;

                                // Redirigir a UserActivity con el nombre del conductor
                                Intent intent = new Intent(Login.this, UserActivity.class);
                                intent.putExtra("NOMBRE_CONDUCTOR", nombreCompleto);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.d("LoginDebug", "Contraseña incorrecta para ficha: " + ficha);
                                Toast.makeText(Login.this, "Ficha o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                    }
                }
                if (!found) {
                    Log.d("LoginDebug", "Empleado no encontrado para ficha: " + ficha);
                    Toast.makeText(Login.this, "Empleado no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("LoginDebug", "Error en la base de datos: " + databaseError.getMessage());
                Toast.makeText(Login.this, "Error en la base de datos: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}