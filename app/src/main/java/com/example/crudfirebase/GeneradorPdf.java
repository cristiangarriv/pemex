package com.example.crudfirebase;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Clase encargada de generar documentos PDF con la información del formulario Anexo.
 * Usa una plantilla preexistente 'anexo.pdf' ubicada en res/raw/anexo.pdf.
 * Ahora incluye vista previa del PDF generado antes de ser almacenado.
 */
public class GeneradorPdf {

    private static final String TAG = "GeneradorPdf"; // Etiqueta para logs
    private Context context;

    /**
     * Constructor de la clase.
     *
     * @param context Contexto de la aplicación.
     */
    public GeneradorPdf(Context context) {
        this.context = context;
    }

    /**
     * Genera un archivo PDF con los datos del formulario.
     *
     * @param datosFormulario Objeto con los datos del formulario.
     * @return Archivo PDF generado, o null si ocurre un error.
     */
    public File generarPDF(DatosFormulario datosFormulario) {
        // Verificar si el directorio de documentos está disponible
        File directorio = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (directorio == null) {
            Log.e(TAG, "No se pudo acceder al directorio de documentos.");
            return null;
        }

        // Crear el archivo PDF
        File archivoPDF = new File(directorio, "Formulario_" + datosFormulario.getNumeroEmbarque() + ".pdf");
        PdfDocument documento = new PdfDocument();

        // Crear una página en el documento
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page pagina = documento.startPage(pageInfo);
        Canvas canvas = pagina.getCanvas();
        Paint paint = new Paint();
        paint.setTextSize(14);

        // Escribir los datos del formulario en el PDF
        int y = 100; // Posición vertical inicial
        canvas.drawText("Fecha: " + datosFormulario.getFecha(), 50, y, paint); y += 30;
        canvas.drawText("Número de Embarque: " + datosFormulario.getNumeroEmbarque(), 50, y, paint); y += 30;
        canvas.drawText("Clave del Cliente: " + datosFormulario.getClaveCliente(), 50, y, paint); y += 30;
        canvas.drawText("Permiso CRE: " + datosFormulario.getPermisoCre(), 50, y, paint); y += 30;
        canvas.drawText("Razón Social: " + datosFormulario.getRazonSocial(), 50, y, paint); y += 30;
        canvas.drawText("Dirección de Entrega: " + datosFormulario.getDireccionEntrega(), 50, y, paint); y += 30;
        canvas.drawText("Observaciones: " + datosFormulario.getObservaciones(), 50, y, paint);

        documento.finishPage(pagina);

        // Guardar el archivo PDF
        try (FileOutputStream fos = new FileOutputStream(archivoPDF)) {
            documento.writeTo(fos);
            documento.close();
            Log.i(TAG, "Vista previa del PDF: " + archivoPDF.getAbsolutePath());
            abrirPDF(archivoPDF); // Abrir el PDF generado
            return archivoPDF;
        } catch (IOException e) {
            Log.e(TAG, "Error al generar el PDF", e);
            return null;
        }
    }

    /**
     * Abre el archivo PDF generado en una aplicación externa.
     *
     * @param archivo Archivo PDF a abrir.
     */
    private void abrirPDF(File archivo) {
        Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", archivo);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }

    /**
     * Guarda un Bitmap en la caché de la aplicación y devuelve la ruta del archivo.
     *
     * @param bitmap  Bitmap a guardar.
     * @return Ruta del archivo guardado, o null si ocurre un error.
     */
    public String guardarBitmapEnCache(Bitmap bitmap) {
        String nombreArchivo = "temp_bitmap.png";
        File archivo = new File(context.getCacheDir(), nombreArchivo);
        try (FileOutputStream fos = new FileOutputStream(archivo)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            return archivo.getAbsolutePath();
        } catch (IOException e) {
            Log.e(TAG, "Error al guardar el Bitmap en la caché", e);
            return null;
        }
    }

    /**
     * Reduce el tamaño de un Bitmap para evitar problemas de memoria.
     *
     * @param bitmap     Bitmap original.
     * @param maxAncho   Ancho máximo permitido.
     * @param maxAlto    Alto máximo permitido.
     * @return Bitmap reducido.
     */
    public Bitmap reducirBitmap(Bitmap bitmap, int maxAncho, int maxAlto) {
        int ancho = bitmap.getWidth();
        int alto = bitmap.getHeight();

        float ratio = (float) ancho / (float) alto;
        if (ancho > maxAncho || alto > maxAlto) {
            if (ratio > 1) {
                ancho = maxAncho;
                alto = (int) (ancho / ratio);
            } else {
                alto = maxAlto;
                ancho = (int) (alto * ratio);
            }
        }

        return Bitmap.createScaledBitmap(bitmap, ancho, alto, true);
    }

    /**
     * Carga un Bitmap de manera optimizada para evitar problemas de memoria.
     *
     * @param rutaDelArchivo Ruta del archivo de la imagen.
     * @param anchoRequerido Ancho requerido para el Bitmap.
     * @param altoRequerido  Alto requerido para el Bitmap.
     * @return Bitmap cargado y optimizado.
     */
    public Bitmap cargarBitmapOptimizado(String rutaDelArchivo, int anchoRequerido, int altoRequerido) {
        // Decodificar el tamaño de la imagen sin cargar el Bitmap en memoria
        BitmapFactory.Options opciones = new BitmapFactory.Options();
        opciones.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(rutaDelArchivo, opciones);

        // Calcular el factor de escala
        opciones.inSampleSize = calcularFactorDeEscala(opciones, anchoRequerido, altoRequerido);

        // Decodificar el Bitmap con el factor de escala
        opciones.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(rutaDelArchivo, opciones);
    }

    /**
     * Calcula el factor de escala para reducir el tamaño del Bitmap.
     *
     * @param opciones       Opciones de decodificación del Bitmap.
     * @param anchoRequerido Ancho requerido para el Bitmap.
     * @param altoRequerido  Alto requerido para el Bitmap.
     * @return Factor de escala.
     */
    private int calcularFactorDeEscala(BitmapFactory.Options opciones, int anchoRequerido, int altoRequerido) {
        final int ancho = opciones.outWidth;
        final int alto = opciones.outHeight;
        int factorDeEscala = 1;

        if (alto > altoRequerido || ancho > anchoRequerido) {
            final int mitadAlto = alto / 2;
            final int mitadAncho = ancho / 2;

            while ((mitadAlto / factorDeEscala) >= altoRequerido && (mitadAncho / factorDeEscala) >= anchoRequerido) {
                factorDeEscala *= 2;
            }
        }

        return factorDeEscala;
    }
}