package com.example.crudfirebase;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActualizarRegistro extends AppCompatActivity {

    private EditText editTextClaveDepartamento, editTextPlazaHistorica, editTextFicha, editTextNombres, editTextApellidoPaterno, editTextApellidoMaterno, editTextRFC, editTextCategoria, editTextNivel, editTextContrasena;
    private ImageView imageViewGuardarCambios;
    private DatabaseReference databaseReference;
    private String ficha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actualizar_registro);

        editTextClaveDepartamento = findViewById(R.id.txtClaveDepartamento);
        editTextPlazaHistorica = findViewById(R.id.txtPlazaHistorica);
        editTextFicha = findViewById(R.id.txtFicha);
        editTextNombres = findViewById(R.id.txtNombres);
        editTextApellidoPaterno = findViewById(R.id.txtApellidoPaterno);
        editTextApellidoMaterno = findViewById(R.id.txtApellidoMaterno);
        editTextRFC = findViewById(R.id.txtRFC);
        editTextCategoria = findViewById(R.id.txtCategoria);
        editTextNivel = findViewById(R.id.txtNivel);
        editTextContrasena = findViewById(R.id.txtContrasena);
        imageViewGuardarCambios = findViewById(R.id.btnGuardarCambios);

        // Obtener la ficha del empleado desde el intent
        ficha = getIntent().getStringExtra("ficha");

        databaseReference = FirebaseDatabase.getInstance().getReference("empleados");

        // Cargar datos del empleado
        cargarDatosEmpleado();

        imageViewGuardarCambios.setOnClickListener(view -> actualizarRegistro());
    }

    private void cargarDatosEmpleado() {
        databaseReference.orderByChild("ficha").equalTo(Integer.parseInt(ficha)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Empleados.Empleado empleado = snapshot.getValue(Empleados.Empleado.class);
                    if (empleado != null) {
                        editTextClaveDepartamento.setText(String.valueOf(empleado.getClaveDepartamento()));
                        editTextPlazaHistorica.setText(String.valueOf(empleado.getPlazaHistorica()));
                        editTextFicha.setText(String.valueOf(empleado.getFicha()));
                        editTextNombres.setText(empleado.getNombres());
                        editTextApellidoPaterno.setText(empleado.getApellidoPaterno());
                        editTextApellidoMaterno.setText(empleado.getApellidoMaterno());
                        editTextRFC.setText(empleado.getRfc());
                        editTextCategoria.setText(empleado.getCategoria());
                        editTextNivel.setText(String.valueOf(empleado.getNivel()));
                        editTextContrasena.setText(empleado.getContrasena());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar error
            }
        });
    }

    private void actualizarRegistro() {
        String claveDepartamento = editTextClaveDepartamento.getText().toString();
        String plazaHistorica = editTextPlazaHistorica.getText().toString();
        String ficha = editTextFicha.getText().toString();
        String nombres = editTextNombres.getText().toString();
        String apellidoPaterno = editTextApellidoPaterno.getText().toString();
        String apellidoMaterno = editTextApellidoMaterno.getText().toString();
        String rfc = editTextRFC.getText().toString();
        String categoria = editTextCategoria.getText().toString();
        String nivel = editTextNivel.getText().toString();
        String contrasena = editTextContrasena.getText().toString();

        if (!ficha.isEmpty()) {
            Empleados.Empleado empleado = new Empleados.Empleado(Integer.parseInt(claveDepartamento), Long.parseLong(plazaHistorica), Integer.parseInt(ficha), nombres, apellidoPaterno, apellidoMaterno, rfc, categoria, Integer.parseInt(nivel), contrasena);

            databaseReference.orderByChild("ficha").equalTo(Integer.parseInt(ficha)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshot.getRef().setValue(empleado)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(ActualizarRegistro.this, "DATOS ACTUALIZADOS CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(ActualizarRegistro.this, "ERROR AL ACTUALIZAR LOS DATOS", Toast.LENGTH_SHORT).show();
                                });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(ActualizarRegistro.this, "ERROR: FICHA DE EMPLEADO NO VÁLIDA", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "ERROR: FICHA DE EMPLEADO NO VÁLIDA", Toast.LENGTH_SHORT).show();
        }
    }
}