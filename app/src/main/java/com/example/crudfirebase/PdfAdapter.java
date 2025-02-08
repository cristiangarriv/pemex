package com.example.crudfirebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.PdfViewHolder> {

    private List<PdfItem> pdfList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String url);
    }

    public PdfAdapter(List<PdfItem> pdfList, OnItemClickListener listener) {
        this.pdfList = pdfList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf_item, parent, false);
        return new PdfViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PdfViewHolder holder, int position) {
        PdfItem pdfItem = pdfList.get(position);
        holder.bind(pdfItem, listener);
    }

    @Override
    public int getItemCount() {
        return pdfList.size();
    }

    static class PdfViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public PdfViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.pdfName);
        }

        public void bind(final PdfItem pdfItem, final OnItemClickListener listener) {
            textView.setText(pdfItem.getUrl());
            itemView.setOnClickListener(v -> listener.onItemClick(pdfItem.getUrl()));
        }
    }
}