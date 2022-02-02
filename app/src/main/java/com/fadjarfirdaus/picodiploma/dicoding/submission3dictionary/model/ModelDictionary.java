package com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelDictionary implements Parcelable {
    int id;
    String words,description;

    public ModelDictionary(String words, String description) {
        this.words = words;
        this.description = description;
    }

    public ModelDictionary() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.words);
        dest.writeString(this.description);
    }

    protected ModelDictionary(Parcel in) {
        this.id = in.readInt();
        this.words = in.readString();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<ModelDictionary> CREATOR = new Parcelable.Creator<ModelDictionary>() {
        @Override
        public ModelDictionary createFromParcel(Parcel source) {
            return new ModelDictionary(source);
        }

        @Override
        public ModelDictionary[] newArray(int size) {
            return new ModelDictionary[size];
        }
    };
}
