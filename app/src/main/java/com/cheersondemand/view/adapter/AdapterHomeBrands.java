package com.cheersondemand.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cheersondemand.R;

import java.util.List;

/**
 * Created by GAURAV on 6/7/2018.
 */

public class AdapterHomeBrands extends RecyclerView.Adapter<AdapterHomeBrands.MyViewHolder> {

private List<String> horizontalList;
    Context context;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView tvBrandName;
    public ImageView ivProductImage;
    public MyViewHolder(View view) {
        super(view);
        tvBrandName = (TextView) view.findViewById(R.id.tvBrandName);
        ivProductImage = (ImageView) view.findViewById(R.id.ivProductImage);

    }
}


    public AdapterHomeBrands(List<String> horizontalList,Context context) {
        this.horizontalList = horizontalList;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_round_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tvBrandName.setText(horizontalList.get(position));

        holder.tvBrandName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,holder.tvBrandName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return horizontalList.size();
    }

}
