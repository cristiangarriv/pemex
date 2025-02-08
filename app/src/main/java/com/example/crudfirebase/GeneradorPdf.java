package com.example.crudfirebase;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Clase que genera un PDF basado en la plantilla ANEXO.pdf.
 * Los datos del formulario se proyectan en la plantilla.
 */
public class GeneradorPDF {

    private final WeakReference<Context> contextRef;

    /**
     * Constructor de la clase GeneradorPDF.
     *
     * @param context Contexto de la aplicación.
     */
    public GeneradorPDF(Context context) {
        this.contextRef = new WeakReference<>(context);
    }

    /**
     * Genera un PDF basado en la plantilla ANEXO.pdf y los datos del formulario.
     *
     * @param datosFormulario Datos del formulario.
     * @param nombreArchivo   Nombre del archivo PDF generado.
     */
    public void generarPDF(DatosFormulario datosFormulario, String nombreArchivo) {
        new PdfTask().execute(datosFormulario, nombreArchivo);
    }

    private class PdfTask extends AsyncTask<Object, Void, File> {

        @Override
        protected File doInBackground(Object... params) {
            DatosFormulario datos = (DatosFormulario) params[0];
            String nombreArchivo = (String) params[1];

            Context context = contextRef.get();
            if (context == null) return null;

            try {
                // 1. Copiar plantilla desde res/raw
                InputStream inputStream = context.getResources().openRawResource(R.raw.anexo);
                File pdfFile = new File(context.getExternalFilesDir(null), nombreArchivo);
                PdfReader reader = new PdfReader(inputStream);
                PdfWriter writer = new PdfWriter(new FileOutputStream(pdfFile));
                PdfDocument pdfDoc = new PdfDocument(reader, writer);

                // 2. Rellenar campos del formulario PDF
                PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
                Map<String, PdfFormField> fields = form.getFormFields();

                // Mapeo de campos (ajustar según la plantilla)
                fields.get("Fecha").setValue(datos.getFecha());
                fields.get("NoEmbarque").setValue(datos.getNumeroEmbarque());
                fields.get("ClaveCliente").setValue(datos.getClaveCliente());
                fields.get("PermisoCRE").setValue(datos.getPermisoCre());
                fields.get("RazonSocial").setValue(datos.getRazonSocial());
                fields.get("DireccionEntrega").setValue(datos.getDireccionEntrega());
                fields.get("Extintor").setValue(datos.getExtintor());
                fields.get("TierraFisica").setValue(datos.getTierraFisica());
                fields.get("Calzas").setValue(datos.getCalzas());
                fields.get("Biombos").setValue(datos.getBiombos());
                fields.get("HoraInicio").setValue(datos.getHoraInicio());
                fields.get("TipoProducto").setValue(datos.getTipoProducto());
                fields.get("VolumenNetoInicial").setValue(datos.getVolumenNetoInicial());
                fields.get("VolumenNetoFinal").setValue(datos.getVolumenNetoFinal());
                fields.get("VolumenTotalDescargado").setValue(datos.getVolumenTotalDescargado());
                fields.get("ColorBocatoma").setValue(datos.getColorBocatoma());
                fields.get("PresionInicial").setValue(datos.getPresionInicial());
                fields.get("PresionFinal").setValue(datos.getPresionFinal());
                fields.get("HoraFinal").setValue(datos.getHoraFinal());
                fields.get("ComprobacionDescarga").setValue(datos.getComprobacionDescarga());
                fields.get("NombreConductor").setValue(datos.getNombreConductor());
                fields.get("NoAT").setValue(datos.getNoAt());
                fields.get("Observaciones").setValue(datos.getObservaciones());
                fields.get("RevisionSellos").setValue(datos.getRevisionSellos());
                fields.get("MuestreoOAT").setValue(datos.getMuestreoOAT());
                fields.get("EntregaOAT").setValue(datos.getEntregaOAT());
                fields.get("SatisfaccionCliente").setValue(datos.getSatisfaccionCliente());

                // 3. Cerrar recursos
                pdfDoc.close();
                return pdfFile;

            } catch (Exception e) {
                Log.e("PDF_ERROR", "Error generando PDF: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(File pdfFile) {
            Context context = contextRef.get();
            if (context == null) return;

            if (pdfFile != null) {
                Toast.makeText(context, "PDF generado: " + pdfFile.getPath(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Error al generar PDF", Toast.LENGTH_SHORT).show();
            }
        }
    }
}