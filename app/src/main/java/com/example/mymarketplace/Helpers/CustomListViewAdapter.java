package com.example.mymarketplace.Helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mymarketplace.Entities.Items;
import com.example.mymarketplace.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CustomListViewAdapter extends ArrayAdapter {

    int[] images;
    ArrayList<String> itemName;
    ArrayList<String> sellerName;
    ArrayList<String> averageRating;

    // Constructor
    public CustomListViewAdapter(Context context, int[] images, ArrayList<String> itemName, ArrayList<String> sellerName, ArrayList<String> averageRating) {
        super(context, R.layout.individual_list_item, R.id.itemName, itemName);
        this.images = images;
        this.itemName = itemName;
        this.sellerName = sellerName;
        this.averageRating = averageRating;
    }

    // Generate View Method
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View singleItem = layoutInflater.inflate(R.layout.individual_list_item, parent, false);

        ImageView itemImgIV = singleItem.findViewById(R.id.itemImage);
        TextView itemNameTV = singleItem.findViewById(R.id.itemName);
        TextView sellerNameTV = singleItem.findViewById(R.id.sellerName);
        TextView averageRatingTV = singleItem.findViewById(R.id.averageRating);

        itemImgIV.setImageResource(images[position]);
        itemNameTV.setText(itemName.get(position));
        sellerNameTV.setText(sellerName.get(position));
        averageRatingTV.setText(averageRating.get(position));

        return singleItem;
    }
}
