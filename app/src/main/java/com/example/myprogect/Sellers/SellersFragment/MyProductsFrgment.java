package com.example.myprogect.Sellers.SellersFragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myprogect.R;
import com.example.myprogect.ViewHolder.ItemViewHolder;
import com.example.myprogect.model.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MyProductsFrgment extends Fragment {

    private View containtVorew;
    private RecyclerView recyclerView;
    private DatabaseReference alPproduct;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public MyProductsFrgment() {
        // Required empty public constructor
    }

    public static MyProductsFrgment newInstance(String param1, String param2) {
        MyProductsFrgment fragment = new MyProductsFrgment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        containtVorew = inflater.inflate(R.layout.fragment_my_products_frgment, container, false);


        recyclerView = containtVorew.findViewById(R.id.sellery_home_rv_fragment_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        alPproduct = FirebaseDatabase.getInstance().getReference().child("Products");
        return containtVorew;

    }


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
                        holder.imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final String producID = model.getPid();
                                CharSequence options[] = new CharSequence[]
                                        {
                                                "نعم "
                                                , "لا"
                                        };
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("هل تريد حذف هذا المنتج. هل أنت واثق؟");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == 0) {

                                            deletproduct(producID);

                                        }
                                        if (which == 1) {

                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_item_view, parent, false);
                        ItemViewHolder holde = new ItemViewHolder(v);
                        return holde;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    private void deletproduct(String producID) {

        alPproduct.child(producID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "تم حذف هذا العنصر بنجاح ، وهو الآن متاح للبيع من البائع\n", Toast.LENGTH_SHORT).show();
                    }
                });

    }




}