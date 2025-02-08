package com.example.crudfirebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EmpleadosAdapterBorrar extends RecyclerView.Adapter<EmpleadosAdapterBorrar.ViewHolder> {

    private List<Empleados.Empleado> empleadoList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Empleados.Empleado empleado);
    }

    public EmpleadosAdapterBorrar(List<Empleados.Empleado> empleadoList, OnItemClickListener listener) {
        this.empleadoList = empleadoList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empleado, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Empleados.Empleado empleado = empleadoList.get(position);
        holder.bind(empleado, listener);
    }

    @Override
    public int getItemCount() {
        return empleadoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewFicha;
        public TextView textViewNombres;
        public TextView textViewApellidoPaterno;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewFicha = itemView.findViewById(R.id.textViewFicha);
            textViewNombres = itemView.findViewById(R.id.textViewNombres);
            textViewApellidoPaterno = itemView.findViewById(R.id.textViewApellidoPaterno);
        }

        public void bind(final Empleados.Empleado empleado, final OnItemClickListener listener) {
            textViewFicha.setText("Ficha: " + empleado.getFicha());
            textViewNombres.setText("Nombres: " + empleado.getNombres());
            textViewApellidoPaterno.setText("Primer apellido: " + empleado.getApellidoPaterno());
            itemView.setOnClickListener(v -> listener.onItemClick(empleado));
        }
    }
}