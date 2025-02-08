package com.example.crudfirebase;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class BorrarActivity extends AppCompatActivity {

    private RecyclerView recyclerViewEmpleados;
    private EmpleadosAdapterBorrar adapter;
    private List<Empleados.Empleado> empleadoList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrar);

        recyclerViewEmpleados = findViewById(R.id.recyclerViewEmpleados);
        recyclerViewEmpleados.setLayoutManager(new LinearLayoutManager(this));

        empleadoList = new ArrayList<>();
        adapter = new EmpleadosAdapterBorrar(empleadoList, empleado -> {
            showDeleteConfirmationDialog(empleado);
        });
        recyclerViewEmpleados.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("empleados");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                empleadoList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Empleados.Empleado empleado = snapshot.getValue(Empleados.Empleado.class);
                    empleadoList.add(empleado);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar error
            }
        });
    }

    private void showDeleteConfirmationDialog(Empleados.Empleado empleado) {
        new AlertDialog.Builder(this)
                .setTitle("ELIMINAR REGISTRO")
                .setMessage("¿DESEAS ELIMINAR EL REGISTRO DE: " + empleado.getNombres() + "?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        showFinalConfirmationDialog(empleado);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showFinalConfirmationDialog(Empleados.Empleado empleado) {
        new AlertDialog.Builder(this)
                .setTitle("CONFIRMACIÓN")
                .setMessage("¿ESTÁS SEGURO?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteEmpleado(empleado);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteEmpleado(Empleados.Empleado empleado) {
        databaseReference.child(String.valueOf(empleado.getFicha())).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(BorrarActivity.this, "Registro eliminado correctamente", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(BorrarActivity.this, "Error al eliminar el registro", Toast.LENGTH_SHORT).show();
                });
    }
}