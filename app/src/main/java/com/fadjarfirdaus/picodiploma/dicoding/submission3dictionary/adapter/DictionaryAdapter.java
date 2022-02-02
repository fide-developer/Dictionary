package com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.R;
import com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.model.ModelDictionary;

import java.util.ArrayList;

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.DictionaryHolder> {
    private ArrayList<ModelDictionary> listWords = new ArrayList<>();

    public DictionaryAdapter() {
    }

    public void setData(ArrayList<ModelDictionary> listMahasiswa) {
        if (listMahasiswa.size() > 0) {
            this.listWords.clear();
        }
        this.listWords.addAll(listMahasiswa);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DictionaryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_dictionary_row,viewGroup,false);
        return new DictionaryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DictionaryHolder holder, int position) {
        holder.textWords.setText(listWords.get(position).getWords());
    }

    @Override
    public long getItemId(int position) {
        return listWords.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return listWords.size();
    }

    public class DictionaryHolder extends RecyclerView.ViewHolder {
        private TextView textWords;

        public DictionaryHolder(@NonNull View itemView) {
            super(itemView);

            textWords = itemView.findViewById(R.id.text_words);
        }
    }
}
