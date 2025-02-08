package com.example.crudfirebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VistaEmpleado extends RecyclerView.Adapter<VistaEmpleado.ViewHolder> {

    private Context context;
    private List<Empleados.Empleado> empleadosList;

    public VistaEmpleado(Context context, List<Empleados.Empleado> empleadosList) {
        this.context = context;
        this.empleadosList = empleadosList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lista, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Empleados.Empleado empleado = empleadosList.get(position);
        holder.textViewClaveDepto.setText("Clave del Departamento: " + empleado.getClaveDepartamento());
        holder.textViewPlazaHis.setText("Plaza Histórica: " + empleado.getPlazaHistorica());
        holder.textViewFicha.setText("Ficha: " + empleado.getFicha());
        holder.textViewNombres.setText("Nombres: " + empleado.getNombres());
        holder.textViewApPaterno.setText("Apellido Paterno: " + empleado.getApellidoPaterno());
        holder.textViewApMaterno.setText("Apellido Materno: " + empleado.getApellidoMaterno());
        holder.textViewRfc.setText("RFC: " + empleado.getRfc());
        holder.textViewCategoria.setText("Categoría: " + empleado.getCategoria());
        holder.textViewNivel.setText("Nivel: " + empleado.getNivel());
    }

    @Override
    public int getItemCount() {
        return empleadosList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewClaveDepto, textViewPlazaHis, textViewFicha, textViewNombres,
                textViewApPaterno, textViewApMaterno, textViewRfc, textViewCategoria, textViewNivel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewClaveDepto = itemView.findViewById(R.id.textViewClaveDepto);
            textViewPlazaHis = itemView.findViewById(R.id.textViewPlazaHis);
            textViewFicha = itemView.findViewById(R.id.textViewFicha);
            textViewNombres = itemView.findViewById(R.id.textViewNombres);
            textViewApPaterno = itemView.findViewById(R.id.textViewApPaterno);
            textViewApMaterno = itemView.findViewById(R.id.textViewApMaterno);
            textViewRfc = itemView.findViewById(R.id.textViewRfc);
            textViewCategoria = itemView.findViewById(R.id.textViewCategoria);
            textViewNivel = itemView.findViewById(R.id.textViewNivel);
        }
    }
}