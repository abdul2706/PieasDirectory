package com.example.abdul.pieasdirectory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private static final String TAG = "CustomAdapter";
    private ArrayList<Person> personArrayList;
    private MainActivity mainActivity;

    CustomAdapter(MainActivity mainActivity, ArrayList<Person> people) {
        this.mainActivity = mainActivity;
        this.personArrayList = new ArrayList<>();
        this.personArrayList = people;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameTextView.setText(personArrayList.get(position).getPersonData("personName").toUpperCase());
        holder.departmentTextView.setText(personArrayList.get(position).getPersonData("department").toUpperCase());
        holder.postTextView.setText(personArrayList.get(position).getPersonData("post"));

        final int index = position;
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, ShowPersonActivity.class);
                intent.putExtra("index", index);
                mainActivity.startActivityForResult(intent, ShowPersonActivity.SHOW_PERSON_ACTIVITY);
            }
        });

        holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(mainActivity)
                        .setIcon(android.R.drawable.stat_sys_warning)
                        .setTitle("Warning")
                        .setMessage("Do you want to delete selected item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseHandler.deletePerson(mainActivity, index);
                                personArrayList.remove(index);
                                mainActivity.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return personArrayList.size();
    }

    public void loadPersonsData(ArrayList<Person> people) {
        this.personArrayList = people;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout;
        TextView nameTextView;
        TextView departmentTextView;
        TextView postTextView;

        ViewHolder(View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.itemRelativeLayout);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            departmentTextView = itemView.findViewById(R.id.departmentTextView);
            postTextView = itemView.findViewById(R.id.postTextView);
        }
    }

}
