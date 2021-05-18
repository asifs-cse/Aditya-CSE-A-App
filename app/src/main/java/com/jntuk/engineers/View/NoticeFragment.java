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
import com.jntuk.engineers.Model.Notice;
import com.jntuk.engineers.R;
import com.jntuk.engineers.View.ViewHolder.NoticeViewHolder;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class NoticeFragment extends Fragment {

    public NoticeFragment() {
        // Required empty public constructor
    }

    private RecyclerView notice_recycler;
    private DatabaseReference notice_data;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice, container, false);

        shimmerFrameLayout = view.findViewById(R.id.notice_shimmer_viewer);
        notice_recycler = view.findViewById(R.id.notice_recyclerId);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        // Set the layout manager to your recyclerview
        notice_recycler.setLayoutManager(mLayoutManager);


        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() == true)
        {
            showNotice();
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

    private void showNotice() {
        notice_data = FirebaseDatabase.getInstance().getReference().child("Admin").child("Notice");

        FirebaseRecyclerOptions<Notice> options = new FirebaseRecyclerOptions.Builder<Notice>()
                .setQuery(notice_data, Notice.class).build();

        FirebaseRecyclerAdapter<Notice, NoticeViewHolder> adapter = new FirebaseRecyclerAdapter<Notice, NoticeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull NoticeViewHolder noticeViewHolder, int i, @NonNull Notice notice) {
                noticeViewHolder.date.setText(notice.getDate());
                noticeViewHolder.notice_title.setText(notice.getTitle());

                onPause();

                noticeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CharSequence options[] = new CharSequence[]{"Download", "Open"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Notice");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0)
                                {
                                    downloadFile(getContext(), notice.getTitle(),".pdf",DIRECTORY_DOWNLOADS, notice.getUri());
                                }else if (which == 1)
                                {
                                    openWebPage(notice.getUri());
                                }
                            }
                        });
                        builder.show();

                    }
                });
            }

            @NonNull
            @Override
            public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_sample, parent, false);
                NoticeViewHolder holder = new NoticeViewHolder(view);
                return holder;
            }
        };
        notice_recycler.setAdapter(adapter);
        adapter.startListening();
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