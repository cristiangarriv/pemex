package com.example.crudfirebase;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class VistaPreviaPdfs extends AppCompatActivity {

    private ImageView pdfImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.previa_pdf);

        pdfImageView = findViewById(R.id.pdfImageView);

        // Obtener la ruta del archivo PDF desde el Intent
        String pdfPath = getIntent().getStringExtra("PDF_PATH");
        if (pdfPath != null) {
            mostrarMiniatura(pdfPath);
        } else {
            Toast.makeText(this, "No se pudo cargar el PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarMiniatura(String pdfPath) {
        try {
            File pdfFile = new File(pdfPath);
            ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY);
            PdfRenderer pdfRenderer = new PdfRenderer(parcelFileDescriptor);
            PdfRenderer.Page currentPage = pdfRenderer.openPage(0);

            Bitmap bitmap = Bitmap.createBitmap(currentPage.getWidth(), currentPage.getHeight(), Bitmap.Config.ARGB_8888);
            currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

            pdfImageView.setImageBitmap(bitmap);

            currentPage.close();
            pdfRenderer.close();
            parcelFileDescriptor.close();
        } catch (Exception e) {
            Toast.makeText(this, "Error al mostrar el PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
