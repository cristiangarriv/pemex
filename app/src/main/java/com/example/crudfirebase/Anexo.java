package com.example.crudfirebase;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Actividad principal que captura los datos del formulario y genera el PDF.
 */
public class Anexo extends AppCompatActivity {

    private EditText fecha, numeroEmbarque, claveCliente, razonSocial, direccionEntrega, nombreEs, permisoCre, permisoCreConfirm, noAt, volumenNetoInicial, volumenNetoFinal, volumenTotalDescargado, nombreConductor, noAtConflict, observaciones;
    private RadioGroup tipoProducto, colorBocatoma, presionInicial, presionFinal, presionFinalConflict, revisionS, oatMuestreoB, oatEntregaB, oatEntregaBConflict;
    private CheckBox extintor, tierraFisica, calzas, biombos;
    private EditText horaInicio, horaFinal;
    private Button btnGenerar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anexo);

        // Inicializar vistas
        fecha = findViewById(R.id.fecha);
        numeroEmbarque = findViewById(R.id.numero_embarque);
        claveCliente = findViewById(R.id.clave_cliente);
        razonSocial = findViewById(R.id.razon_social);
        direccionEntrega = findViewById(R.id.direccion_entrega);
        nombreEs = findViewById(R.id.nombre_es);
        permisoCre = findViewById(R.id.permiso_cre);
        permisoCreConfirm = findViewById(R.id.permiso_cre_confirm);
        noAt = findViewById(R.id.no_at);
        volumenNetoInicial = findViewById(R.id.volumen_neto_inicial);
        volumenNetoFinal = findViewById(R.id.volumen_neto_final);
        volumenTotalDescargado = findViewById(R.id.volumen_total_descargado);
        nombreConductor = findViewById(R.id.nombre_conductor);
        noAtConflict = findViewById(R.id.no_at_conflict);
        observaciones = findViewById(R.id.observaciones);

        tipoProducto = findViewById(R.id.tipo_producto);
        colorBocatoma = findViewById(R.id.color_bocatoma);
        presionInicial = findViewById(R.id.presion_inicial);
        presionFinal = findViewById(R.id.presion_final);
        presionFinalConflict = findViewById(R.id.presion_final_conflict);
        revisionS = findViewById(R.id.revision_s);
        oatMuestreoB = findViewById(R.id.oat_muestreo_b);
        oatEntregaB = findViewById(R.id.oat_entrega_b);
        oatEntregaBConflict = findViewById(R.id.oat_entrega_b_conflict);

        extintor = findViewById(R.id.extintor);
        tierraFisica = findViewById(R.id.tierra_fisica);
        calzas = findViewById(R.id.calzas);
        biombos = findViewById(R.id.biombos);
        horaInicio = findViewById(R.id.hora_inicio);
        horaFinal = findViewById(R.id.hora_final);

        btnGenerar = findViewById(R.id.btn_generar);

        // Establecer la fecha actual
        fecha.setText(Utils.obtenerFechaActual());

        // Generar PDF
        btnGenerar.setOnClickListener(v -> {
            DatosFormulario datos = new DatosFormulario();
            datos.setFecha(fecha.getText().toString());
            datos.setNumeroEmbarque(numeroEmbarque.getText().toString());
            datos.setClaveCliente(claveCliente.getText().toString());
            datos.setPermisoCre(permisoCre.getText().toString());
            datos.setRazonSocial(razonSocial.getText().toString());
            datos.setDireccionEntrega(direccionEntrega.getText().toString());
            datos.setExtintor(extintor.isChecked() ? "Sí" : "No");
            datos.setTierraFisica(tierraFisica.isChecked() ? "Sí" : "No");
            datos.setCalzas(calzas.isChecked() ? "Sí" : "No");
            datos.setBiombos(biombos.isChecked() ? "Sí" : "No");
            datos.setHoraInicio(horaInicio.getText().toString());
            datos.setTipoProducto(obtenerTextoRadioButton(tipoProducto));
            datos.setVolumenNetoInicial(volumenNetoInicial.getText().toString());
            datos.setVolumenNetoFinal(volumenNetoFinal.getText().toString());
            datos.setVolumenTotalDescargado(volumenTotalDescargado.getText().toString());
            datos.setColorBocatoma(obtenerTextoRadioButton(colorBocatoma));
            datos.setPresionInicial(obtenerTextoRadioButton(presionInicial));
            datos.setPresionFinal(obtenerTextoRadioButton(presionFinal));
            datos.setHoraFinal(horaFinal.getText().toString());
            datos.setComprobacionDescarga(obtenerTextoRadioButton(presionFinalConflict));
            datos.setNombreConductor(nombreConductor.getText().toString());
            datos.setNoAt(noAt.getText().toString());
            datos.setObservaciones(observaciones.getText().toString());
            datos.setRevisionSellos(obtenerTextoRadioButton(revisionS));
            datos.setMuestreoOAT(obtenerTextoRadioButton(oatMuestreoB));
            datos.setEntregaOAT(obtenerTextoRadioButton(oatEntregaB));
            datos.setSatisfaccionCliente(obtenerTextoRadioButton(oatEntregaBConflict));

            new GeneradorPDF(Anexo.this).generarPDF(datos, "anexo_" + System.currentTimeMillis() + ".pdf");
        });
    }

    private String obtenerTextoRadioButton(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton radioButton = findViewById(selectedId);
            return radioButton.getText().toString();
        }
        return "";
    }
}