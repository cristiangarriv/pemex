package com.example.crudfirebase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.ListResult;

import java.util.ArrayList;
import java.util.List;

public class ConsultaAnexos extends AppCompatActivity {

    private ListView listViewPdfs;
    private List<String> pdfNames;
    private List<Uri> pdfUris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultar_anexos);

        listViewPdfs = findViewById(R.id.listViewPdfs);
        pdfNames = new ArrayList<>();
        pdfUris = new ArrayList<>();

        loadPdfFiles();

        listViewPdfs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri pdfUri = pdfUris.get(position);
                Intent intent = new Intent(ConsultaAnexos.this, VistaPreviaPdfs.class);
                intent.putExtra("PDF_URI", pdfUri.toString());
                startActivity(intent);
            }
        });
    }

    private void loadPdfFiles() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("pdfs/");

        storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getItems()) {
                    item.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                        @Override
                        public void onSuccess(StorageMetadata storageMetadata) {
                            String name = storageMetadata.getName();
                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    pdfNames.add(name);
                                    pdfUris.add(uri);
                                    updateListView();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(ConsultaAnexos.this, "Error al obtener metadata", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ConsultaAnexos.this, "Error al listar archivos PDF", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pdfNames);
        listViewPdfs.setAdapter(adapter);
    }
}