package com.example.crudfirebase;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
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

public class ConsultaActivity extends AppCompatActivity {

    private RecyclerView recyclerViewEmpleados;
    private EmpleadosAdapterConsulta adapter;
    private List<Empleados.Empleado> empleadoList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        recyclerViewEmpleados = findViewById(R.id.recyclerViewEmpleados);
        recyclerViewEmpleados.setLayoutManager(new LinearLayoutManager(this));

        empleadoList = new ArrayList<>();
        adapter = new EmpleadosAdapterConsulta(empleadoList, empleado -> {
            // Redirigir a la actividad de actualizar registro con los datos del empleado seleccionado
            Intent intent = new Intent(ConsultaActivity.this, ActualizarRegistro.class);
            intent.putExtra("ficha", String.valueOf(empleado.getFicha()));
            startActivity(intent);
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
}