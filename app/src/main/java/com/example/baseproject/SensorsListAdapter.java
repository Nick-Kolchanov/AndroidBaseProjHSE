package com.example.baseproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SensorsListAdapter extends RecyclerView.Adapter<SensorsListAdapter.SensorsViewHolder> {
    public class SensorsViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public SensorsViewHolder(View view) {
            super(view);

            textView = (TextView) view.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    private String[] arraySensors;

    public SensorsListAdapter(String[] arrayCourses) {
        arraySensors = arrayCourses;
    }

    @NonNull
    @Override
    public SensorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_row_item, parent, false);
        return new SensorsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SensorsViewHolder holder, int position) {
        holder.getTextView().setText(arraySensors[position]);
    }

    @Override
    public int getItemCount() {
        return arraySensors.length;
    }
}
