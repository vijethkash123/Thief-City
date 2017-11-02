package com.dot.thievescity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Admin on 11/1/2017.
 */

 public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {


    private ArrayList<Integer> imageIds = new ArrayList<>();

    public MainAdapter(ArrayList<Integer> imageIds)
    {
        this.imageIds = imageIds;
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MainAdapter.ViewHolder holder, int position) {
        holder.imageView.setImageResource(imageIds.get(position));
    }

    @Override
    public int getItemCount() {
        return imageIds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_id);
        }
    }
}
