package com.example.mymarketplace.Helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mymarketplace.Entities.Items;
import com.example.mymarketplace.R;

import java.util.ArrayList;

public class CustomListViewAdapter extends ArrayAdapter<Items.Item> {

    Context context;

    // View lookup cache
    private static class ViewHolder {
        TextView itemName;
        TextView sellerName;
        TextView itemRating;
        ImageView itemImg;
    }

    public CustomListViewAdapter(ArrayList<Items.Item> data, Context mcontext) {
        super(mcontext, R.layout.individual_list_item, data);
        this.context=mcontext;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Items.Item item = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.individual_list_item, parent, false);
            viewHolder.itemName = convertView.findViewById(R.id.itemName);
            viewHolder.sellerName = convertView.findViewById(R.id.sellerName);
            viewHolder.itemRating = convertView.findViewById(R.id.averageRating);
            viewHolder.itemImg = convertView.findViewById(R.id.itemImage);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.itemName.setText(item.productName);
        viewHolder.sellerName.setText(item.sellerName);
        viewHolder.itemRating.setText(item.averageRatingAsText);

        int itemPhotoDir = context.getResources().getIdentifier(item.photoDirectory,"drawable", context.getPackageName());
        viewHolder.itemImg.setImageResource(itemPhotoDir);

        // Render the view
        return convertView;
    }
}
