package com.example.myprogect.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myprogect.Interface.ItemClickListner;
import com.example.myprogect.R;


public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView
             txtProductNameseller
            , txtProductDescriptionseller
            , txtProductPriceseller
            , textproductstatusseller
            ,   txtNameseller2
    ;
    public ImageView imageView;
    public ItemClickListner listner;



    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);


        imageView =   itemView.findViewById(R.id.product_seller_image);

        txtProductNameseller =   itemView.findViewById(R.id.product_seller_name);
        txtNameseller2 =   itemView.findViewById(R.id.seller_name_item_sall222);
        txtProductDescriptionseller =   itemView.findViewById(R.id.product_seller_description);
        txtProductPriceseller =  itemView.findViewById(R.id.product_seller_price);
        textproductstatusseller =  itemView.findViewById(R.id.product_seller_state);



    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }
}