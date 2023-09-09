package com.example.futurefastfood;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class FoodListAdapter extends ArrayAdapter<FoodItem> {
    private Context mContext;

    public FoodListAdapter(@NonNull Context context, ArrayList<FoodItem> foodList) {
        super(context, 0, foodList);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.food_list_item, parent, false);
        }

        FoodItem item = getItem(position);

        ImageView foodImageView = convertView.findViewById(R.id.food_image);
        TextView foodNameTextView = convertView.findViewById(R.id.food_name);
        TextView foodPriceTextView = convertView.findViewById(R.id.food_price);

        if (item != null) {
            foodImageView.setImageResource(item.getImage());
            foodNameTextView.setText(item.getName());

            String priceString = String.format("%.2f", item.getPrice());
            String priceLabelText = mContext.getString(R.string.price_label, priceString);
            foodPriceTextView.setText(priceLabelText);
        }

        return convertView;
    }
}

