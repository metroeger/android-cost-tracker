package com.metroeger.costtrackerapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.metroeger.costtrackerapp.R;
import com.metroeger.costtrackerapp.model.CategoryItem;

import java.util.List;

public class CategoryItemAdapter extends ArrayAdapter {

    private int resource;

    public CategoryItemAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.resource=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CategoryItem ci = (CategoryItem) getItem(position);

        if(convertView==null){
            convertView=LayoutInflater.from(getContext()).inflate(resource,null);
        }

        TextView tvCategoryName = convertView.findViewById(R.id.tvCategoryName);
        TextView tvCategoryAmount = convertView.findViewById(R.id.tvCategoryAmount);

        tvCategoryName.setText(ci.getName());
        tvCategoryAmount.setText(ci.getAmount()+"");

        return convertView;
    }
}
