package com.example.crudfirebase;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Clase GeneradorPdf para la generación de documentos PDF.
 */
public class GeneradorPdf {

    private Context context;
    private SignaturePad signaturePad;

    /**
     * Constructor de la clase GeneradorPdf.
     *
     * @param context Contexto de la aplicación
     * @param signaturePad SignaturePad para capturar firmas
     */
    public GeneradorPdf(Context context, SignaturePad signaturePad) {
        this.context = context;
        this.signaturePad = signaturePad;
    }

    /**
     * Método para iniciar la generación del PDF.
     */
    public void generarPDF() {
        new PdfTask().execute();
    }

    /**
     * Clase interna PdfTask para realizar la tarea de generación de PDF en segundo plano.
     */
    private class PdfTask extends AsyncTask<Void, Void, Boolean> {

        private File pdfFile;

        @Override
        protected Boolean doInBackground(Void... voids) {
            PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();

            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(40);

            canvas.drawText("Este es un documento PDF generado", 80, 50, paint);

            // Añadir firma al PDF si existe
            if (!signaturePad.isEmpty()) {
                canvas.drawBitmap(signaturePad.getSignatureBitmap(), 80, 100, paint);
            }

            document.finishPage(page);

            try {
                File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDFs");
                if (!directory.exists()) {
                    boolean dirCreated = directory.mkdirs();
                    if (!dirCreated) {
                        return false;
                    }
                }
                pdfFile = new File(directory, "anexo.pdf");
                try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
                    document.writeTo(fos);
                }
                document.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                Toast.makeText(context, "PDF generado: " + pdfFile.getPath(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Error al generar PDF", Toast.LENGTH_SHORT).show();
            }
        }
    }
}