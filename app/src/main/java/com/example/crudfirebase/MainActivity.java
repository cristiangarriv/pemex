package com.example.crudfirebase;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText editTextClaveDepartamento, editTextPlazaHistorica, editTextFicha, editTextNombres, editTextApellidoPaterno, editTextApellidoMaterno, editTextRFC, editTextCategoria, editTextNivel, editTextContrasena;
    private ImageView imageViewRegistrar;
    private TextView tvFormTitle;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        imageViewRegistrar = findViewById(R.id.btn_registro);
        tvFormTitle = findViewById(R.id.tvFormTitle);

        databaseReference = FirebaseDatabase.getInstance().getReference("empleados");

        imageViewRegistrar.setOnClickListener(view -> validarRegistroExistente());
    }

    private void validarRegistroExistente() {
        String fichaStr = editTextFicha.getText().toString();
        String plazaHistoricaStr = editTextPlazaHistorica.getText().toString();

        databaseReference.orderByChild("ficha").equalTo(Integer.parseInt(fichaStr)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(MainActivity.this, "ESTE EMPLEADO YA SE ENCUENTRA REGISTRADO", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.orderByChild("plazaHistorica").equalTo(Long.parseLong(plazaHistoricaStr)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Toast.makeText(MainActivity.this, "ESTE EMPLEADO YA SE ENCUENTRA REGISTRADO", Toast.LENGTH_SHORT).show();
                            } else {
                                registrarEmpleado();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(MainActivity.this, "ERROR AL VALIDAR EL EMPLEADO", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "ERROR AL VALIDAR EL EMPLEADO", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registrarEmpleado() {
        String claveDepartamentoStr = editTextClaveDepartamento.getText().toString();
        String plazaHistoricaStr = editTextPlazaHistorica.getText().toString();
        String fichaStr = editTextFicha.getText().toString();
        String nombres = editTextNombres.getText().toString();
        String apellidoPaterno = editTextApellidoPaterno.getText().toString();
        String apellidoMaterno = editTextApellidoMaterno.getText().toString();
        String rfc = editTextRFC.getText().toString();
        String categoria = editTextCategoria.getText().toString();
        String nivelStr = editTextNivel.getText().toString();
        String contrasena = editTextContrasena.getText().toString();

        if (!claveDepartamentoStr.isEmpty() && !fichaStr.isEmpty()) {
            int claveDepartamento = Integer.parseInt(claveDepartamentoStr);
            long plazaHistorica = Long.parseLong(plazaHistoricaStr);
            int ficha = Integer.parseInt(fichaStr);
            int nivel = Integer.parseInt(nivelStr);

            Empleados.Empleado empleado = new Empleados.Empleado(claveDepartamento, plazaHistorica, ficha, nombres, apellidoPaterno, apellidoMaterno, rfc, categoria, nivel, contrasena);
            databaseReference.child(String.valueOf(ficha)).setValue(empleado)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(MainActivity.this, "EMPLEADO REGISTRADO CORRECTAMENTE EN LA BASE DE DATOS", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(MainActivity.this, "ERROR AL REGISTRAR EMPLEADO", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "LOS CAMPOS CLAVE DEL DEPARTAMENTO Y FICHA SON OBLIGATORIOS", Toast.LENGTH_SHORT).show();
        }
    }
}