package com.example.myprogect.Sellers.SellersFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.myprogect.Admin.AdminNewOrdersActivity;
import com.example.myprogect.Admin.AdminUserProductsActivity;
import com.example.myprogect.Buyers.MainActivity;
import com.example.myprogect.R;
import com.example.myprogect.Seller.SalesShowSoldProducts;
import com.example.myprogect.ViewHolder.ItemViewHolder;
import com.example.myprogect.ViewHolder.ProductSellerViewHolder;
import com.example.myprogect.model.AdminOrsers;
import com.example.myprogect.model.OrdersSold;
import com.example.myprogect.model.Products;
import com.example.myprogect.prevalent.AdminOrdersViewHolder;
import com.example.myprogect.prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MySalesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MySalesFragment extends Fragment {
    private View containtViewMySales;
    private RecyclerView recyclerViewMySales;
    private DatabaseReference  soldProduct;
    private TextView textViewExite;

    DatabaseReference orderRef;


    private DatabaseReference alPproduct;
    public MySalesFragment() {
        // Required empty public constructor
    }

    public static MySalesFragment newInstance(String param1, String param2) {
        MySalesFragment fragment = new MySalesFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);   ///   menu
        if (getArguments() != null) {

        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       setHasOptionsMenu(true);
     //   orderRef = FirebaseDatabase.getInstance().getReference().child("orders").child("ordersApproved");
        orderRef = FirebaseDatabase.getInstance()
                .getReference().child("orders")
                .child("ordersApproved");


        containtViewMySales =  inflater.inflate(R.layout.fragment_my_sales, container, false);
        textViewExite= containtViewMySales.findViewById(R.id.exite);

        textViewExite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "exited", Toast.LENGTH_SHORT).show();
                FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

                Intent intentlogout = new Intent(getActivity(), MainActivity.class);
                intentlogout.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentlogout);

            }
        });


        recyclerViewMySales = containtViewMySales.findViewById(R.id.sellery_mySales_rv_fragment_container);
        recyclerViewMySales.setLayoutManager(new LinearLayoutManager(getContext()));
       // soldProduct = FirebaseDatabase.getInstance().getReference().child("Products");
        alPproduct = FirebaseDatabase.getInstance().getReference().child("Products");

        return containtViewMySales ;

  }



@Override
public void onStart() {
    super.onStart();
    final FirebaseRecyclerOptions<Products> options =
            new FirebaseRecyclerOptions.Builder<Products>()
                    .setQuery(

                            alPproduct.orderByChild("sid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()), Products.class)
                    .build();

    FirebaseRecyclerAdapter<Products, ItemViewHolder> adapter =
            new FirebaseRecyclerAdapter<Products, ItemViewHolder>(options) {


                @Override
                protected void onBindViewHolder(@NonNull final ItemViewHolder holder, int position, @NonNull final Products model) {

                    holder.txtNameseller2.setText(model.getSellerName());
                    holder.txtProductNameseller.setText(model.getPname());
                    holder.txtProductDescriptionseller.setText(model.getDescription());

                    holder.textproductstatusseller.setText("الحاله :" + model.getProductstate());
                    holder.txtProductPriceseller.setText("السعر = " + model.getPrice() + "$");

                    Picasso.get().load(model.getImage()).into(holder.imageView);

                }

                @NonNull
                @Override
                public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_item_view, parent, false);
                    ItemViewHolder holde = new ItemViewHolder(v);
                    return holde;
                }
            };
    recyclerViewMySales.setAdapter(adapter);
    adapter.startListening();
}

}