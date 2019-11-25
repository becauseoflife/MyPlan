package com.example.myplan.data;

import android.content.Context;

import com.example.myplan.data.model.Plan;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileDataSource {
    private Context context;
    private ArrayList<Plan> plans = new ArrayList<>();

    public FileDataSource(Context context) {
        this.context = context;
    }

    public ArrayList<Plan> getPlans() {
        return plans;
    }

    public void save()
    {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    context.openFileOutput("Serializable.txt", Context.MODE_PRIVATE)
            );
            outputStream.writeObject(plans);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<Plan> load()
    {
        try{
            ObjectInputStream inputStream = new ObjectInputStream(
                    context.openFileInput("Serializable.txt")
            );
            plans = (ArrayList<Plan>) inputStream.readObject();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plans;
    }
}
