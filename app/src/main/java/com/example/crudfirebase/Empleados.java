package com.example.crudfirebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class Empleados {
    private DatabaseReference databaseReference;
    private List<Empleado> empleados;

    public Empleados() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("empleados");
        empleados = new ArrayList<>();
        loadEmpleadosFromFirebase();
    }

    private void loadEmpleadosFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                empleados.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Empleado empleado = postSnapshot.getValue(Empleado.class);
                    empleados.add(empleado);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar posibles errores
            }
        });
    }

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public void addEmpleado(Empleado empleado) {
        databaseReference.push().setValue(empleado);
    }

    public void updateEmpleado(Empleado empleado) {
        // Implementa la lógica de actualización aquí si es necesario
    }

    public void deleteEmpleado(Empleado empleado) {
        // Implementa la lógica de eliminación aquí si es necesario
    }

    public static class Empleado {
        private String id;
        private int claveDepartamento;
        private long plazaHistorica;
        private int ficha;
        private String nombres;
        private String apellidoPaterno;
        private String apellidoMaterno;
        private String rfc;
        private String categoria;
        private int nivel;
        private String contrasena;

        public Empleado() {
            // Constructor vacío requerido por Firebase
        }

        public Empleado(int claveDepartamento, long plazaHistorica, int ficha, String nombres, String apellidoPaterno, String apellidoMaterno, String rfc, String categoria, int nivel, String contrasena) {
            this.claveDepartamento = claveDepartamento;
            this.plazaHistorica = plazaHistorica;
            this.ficha = ficha;
            this.nombres = nombres;
            this.apellidoPaterno = apellidoPaterno;
            this.apellidoMaterno = apellidoMaterno;
            this.rfc = rfc;
            this.categoria = categoria;
            this.nivel = nivel;
            this.contrasena = contrasena;
        }

        // Métodos getter y setter

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getClaveDepartamento() {
            return claveDepartamento;
        }

        public void setClaveDepartamento(int claveDepartamento) {
            this.claveDepartamento = claveDepartamento;
        }

        public long getPlazaHistorica() {
            return plazaHistorica;
        }

        public void setPlazaHistorica(long plazaHistorica) {
            this.plazaHistorica = plazaHistorica;
        }

        public int getFicha() {
            return ficha;
        }

        public void setFicha(int ficha) {
            this.ficha = ficha;
        }

        public String getNombres() {
            return nombres;
        }

        public void setNombres(String nombres) {
            this.nombres = nombres;
        }

        public String getApellidoPaterno() {
            return apellidoPaterno;
        }

        public void setApellidoPaterno(String apellidoPaterno) {
            this.apellidoPaterno = apellidoPaterno;
        }

        public String getApellidoMaterno() {
            return apellidoMaterno;
        }

        public void setApellidoMaterno(String apellidoMaterno) {
            this.apellidoMaterno = apellidoMaterno;
        }

        public String getRfc() {
            return rfc;
        }

        public void setRfc(String rfc) {
            this.rfc = rfc;
        }

        public String getCategoria() {
            return categoria;
        }

        public void setCategoria(String categoria) {
            this.categoria = categoria;
        }

        public int getNivel() {
            return nivel;
        }

        public void setNivel(int nivel) {
            this.nivel = nivel;
        }

        public String getContrasena() {
            return contrasena;
        }

        public void setContrasena(String contrasena) {
            this.contrasena = contrasena;
        }
    }
}