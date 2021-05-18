package com.jntuk.engineers.View;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jntuk.engineers.Model.Material;
import com.jntuk.engineers.R;
import com.jntuk.engineers.View.ViewHolder.MaterialViewHolder;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class MaterialFragment extends Fragment {


    public MaterialFragment() {
        // Required empty public constructor
    }

    private RecyclerView material_recycler;
    private DatabaseReference material_data;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_material, container, false);

        material_recycler = view.findViewById(R.id.material_recyclerId);
        shimmerFrameLayout = view.findViewById(R.id.material_shimmer_viewer);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        // Set the layout manager to your recyclerview
        material_recycler.setLayoutManager(mLayoutManager);

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() == true){
            material_data = FirebaseDatabase.getInstance().getReference().child("Admin").child("Material");

            FirebaseRecyclerOptions<Material> options = new FirebaseRecyclerOptions.Builder<Material>()
                    .setQuery(material_data, Material.class).build();
            FirebaseRecyclerAdapter<Material, MaterialViewHolder> adapter = new FirebaseRecyclerAdapter<Material, MaterialViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull MaterialViewHolder materialViewHolder, int i, @NonNull Material material) {
                    materialViewHolder.material_sub.setText(material.getSubject());
                    materialViewHolder.material_title.setText(material.getTitle());
                    materialViewHolder.submitter_name.setText(material.getSubmitter());
                    onPause();
                    materialViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CharSequence options[] = new CharSequence[]{"Download", "Open"};
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Material");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0)
                                    {
                                        downloadFile(getContext(), material.getTitle(),".pdf",DIRECTORY_DOWNLOADS, material.getUri());
                                    }else if (which == 1)
                                    {
                                        openWebPage(material.getUri());
                                    }
                                }
                            });
                            builder.show();
                        }
                    });
                }

                @NonNull
                @Override
                public MaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_sample, parent, false);
                    MaterialViewHolder holder = new MaterialViewHolder(view);
                    return holder;
                }
            };
            material_recycler.setAdapter(adapter);
            adapter.startListening();
        }
        else
        {
            View parentLayout = getActivity().findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar.make(parentLayout, "You are offline! Check Internet Connection...", Snackbar.LENGTH_LONG);
            snackbar.setDuration(5000);
            snackbar.show();
        }

        return view;
    }
    public long downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {

        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        return downloadmanager.enqueue(request);
    }

    public void openWebPage(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(url), "application/pdf");
        startActivity(Intent.createChooser(intent, "Choose an Application:"));

//        Intent intent = new Intent(getContext(), WebService.class);
//        intent.putExtra("url", url);
//        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmer();
    }

    @Override
    public void onPause() {
        super.onPause();
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
    }
}