package com.jntuk.engineers.View.ui.home;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jntuk.engineers.Model.AllServices;
import com.jntuk.engineers.Model.SignInUser;
import com.jntuk.engineers.Model.SliderClass;
import com.jntuk.engineers.R;
import com.jntuk.engineers.View.BotomSheet.ShowProfile;
import com.jntuk.engineers.View.Profile.UserProfile;
import com.jntuk.engineers.View.Routine.DailyRoutineActivity;
import com.jntuk.engineers.View.Teacher.TeacherActivity;
import com.jntuk.engineers.View.UserListActivity;
import com.jntuk.engineers.View.ViewHolder.ServicesViewHolder;
import com.jntuk.engineers.View.WebService;
import com.jntuk.engineers.ViewModel.SignInViewModel;
import com.jntuk.engineers.ViewModel.UserListViewModel;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.os.Environment.DIRECTORY_DOWNLOADS;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView status, quotes, popup_userName, popup_userRoll,display_userName;
    private RecyclerView serviceRecycler;
    private LinearLayoutManager layoutManager;
    private List<com.jntuk.engineers.Model.status> statuses;
    private ImageView user_profile;
    private int images[] = {R.drawable.user,R.drawable.user};
    private CardView go_userlist, go_my_profile;
    private RelativeLayout title_bar;
    private UserListViewModel userListViewModel;
    private SignInViewModel signInViewModel;
    private CircleImageView displayImage;
    private String currentUserId, s1,s2,s3,s4;
    private TextView show_quotes;
    private ServiceAdapter serviceAdapter;
    private List<AllServices> all_services;
    private int ServicesImage[] = {R.drawable.routine,R.drawable.students_icon,R.drawable.teacher,R.drawable.webcap,R.drawable.board,R.drawable.notice,R.drawable.exam,R.drawable.calendar2};
    private boolean isAuth, isProfile, completeProfile, isInternet;
    private String link;

    SliderLayout sliderLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        initViewModel();
        getSlider();
        checkUser();

        status = root.findViewById(R.id.show_wishId);
        displayImage = root.findViewById(R.id.display_user_imageId);
        popup_userName = root.findViewById(R.id.popup_userNameId);
        popup_userRoll = root.findViewById(R.id.popup_userRollId);
        display_userName = root.findViewById(R.id.display_user_nameId);
        go_my_profile = root.findViewById(R.id.user_profile_info_cardId);
        show_quotes = root.findViewById(R.id.show_quotesId);

        serviceRecycler = root.findViewById(R.id.services_recyclerId);

        quotes = root.findViewById(R.id.show_quotesId);
        quotes.setSelected(true);
        go_userlist = root.findViewById(R.id.goto_userListId);

        title_bar = root.findViewById(R.id.title_barId);

        root.findViewById(R.id.goto_userListId).setOnClickListener((View view)->{
            startActivity(new Intent(getActivity(), UserListActivity.class));
        });

        title_bar.setOnClickListener((View view)->{
            ShowProfile showProfile = new ShowProfile();
            showProfile.show(getActivity().getSupportFragmentManager(), showProfile.getTag());
        });

        all_services = new ArrayList<>();
        all_services.add(new AllServices("T. Table",""));
        all_services.add(new AllServices("Student",""));
        all_services.add(new AllServices("Teacher",""));
        all_services.add(new AllServices("Web Cap",""));
        all_services.add(new AllServices("Board",""));
        all_services.add(new AllServices("Notice",""));
        all_services.add(new AllServices("Result",""));
        all_services.add(new AllServices("Calendar",""));

        serviceAdapter = new HomeFragment.ServiceAdapter(getActivity(),all_services);
        serviceRecycler.setAdapter(serviceAdapter);

        getTimeFromAndroid();

        sliderLayout = root.findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.FILL); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setScrollTimeInSec(1); //set scroll delay in seconds :

        setSliderViews();

        return root;
    }

    private void goMyProfile() {
        go_my_profile.setOnClickListener((View view)->{
            Intent intent = new Intent(getActivity(), UserProfile.class);
            intent.putExtra("user_id",currentUserId);
            startActivity(intent);
        });
    }

    private void currentUser() {
        FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
        if (User != null){
            currentUserId = User.getUid();
            goMyProfile();
        }
    }

    private void checkUser() {
        signInViewModel.checkAuthFirebase();
        signInViewModel.checkAuthLiveData.observe(getActivity(), new Observer<SignInUser>() {
            @Override
            public void onChanged(SignInUser signInUser) {
                if (signInUser.isAuth){
                    isAuth = true;
                    currentUser();
                    if (signInUser.isProfile){
                        showUserStatus();
                    }
                }
            }
        });
    }
    private void url(String link) {
        try {
            Intent intent = new Intent(getContext(), WebService.class);
            intent.putExtra("url", link);
            startActivity(intent);
        }catch (Exception e)
        {
            Toast.makeText(getContext(), "Network Error "+e, Toast.LENGTH_SHORT).show();
        }

    }

    private void initViewModel() {
        signInViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(SignInViewModel.class);

        userListViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(UserListViewModel.class);
    }

    private void showUserStatus() {
        userListViewModel.getUserDetails(currentUserId);
        userListViewModel.getUserDetailsLiveData.observe(this, new Observer<List<SignInUser>>() {
            @Override
            public void onChanged(List<SignInUser> signInUsers) {
                popup_userName.setText(signInUsers.get(0).getFull_name());
                popup_userRoll.setText(signInUsers.get(0).getRoll());
                display_userName.setText(signInUsers.get(0).getFull_name());
                Glide.with(getActivity()).load(signInUsers.get(0).getUser_image()).placeholder(R.drawable.user).into(displayImage);
            }
        });
    }

    private void getTimeFromAndroid() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            status.setText("Good Morning");
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            status.setText("Good Afternoon");
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            status.setText("Good Evening");
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            status.setText("Good Night");
        }
    }
    public class ServiceAdapter extends RecyclerView.Adapter<ServicesViewHolder>{

        private Context context;
        private List<AllServices> servicesList;

        public ServiceAdapter(Context context, List<AllServices> servicesList) {
            this.context = context;
            this.servicesList = servicesList;
        }

        @NonNull
        @Override
        public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.service_sample, parent, false);
            ServicesViewHolder holder = new ServicesViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ServicesViewHolder holder, int position) {
            holder.service_title.setText(servicesList.get(position).getService());
            Glide.with(getActivity()).load(ServicesImage[position]).into(holder.logo);
            holder.itemView.setOnClickListener((View view)->{
                if (holder.service_title.getText().equals("T. Table")){
                    startActivity(new Intent(getActivity(), DailyRoutineActivity.class));
                }else if (holder.service_title.getText().equals("Student")){
                    startActivity(new Intent(getActivity(), UserListActivity.class));
                }else if (holder.service_title.getText().equals("Board")){
                    url(link = "https://www.jntuk.edu.in/");
                }else if (holder.service_title.getText().equals("Web Cap")){
                    url(link = "http://info.aec.edu.in/acoe/default.aspx");
                }else if (holder.service_title.getText().equals("Teacher")){
                    startActivity(new Intent(getActivity(), TeacherActivity.class));
                }else if (holder.service_title.getText().equals("Notice")){
                    url(link = "https://www.jntuk.edu.in/#view2");
                }else if (holder.service_title.getText().equals("Result")){
                    url(link = "http://jntukresults.edu.in/");
                }else if (holder.service_title.getText().equals("Calendar")){
                    goCalender();
                }
            });
        }

        @Override
        public int getItemCount() {
            return servicesList.size();
        }
    }

    private void goCalender() {
        CharSequence options[] = new CharSequence[]{"Download", "Open"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Notice");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0)
                {
                    downloadFile(getContext(), "Academic Calender",".pdf",DIRECTORY_DOWNLOADS, "https://www.jntuk.edu.in/wp-content/uploads/2021/04/Revised-AC-I-Year-B.Tech-AY-2020-21.pdf");
                }else if (which == 1)
                {
                    openWebPage("https://www.jntuk.edu.in/wp-content/uploads/2021/04/Revised-AC-I-Year-B.Tech-AY-2020-21.pdf");
                }
            }
        });
        builder.show();
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

    //get slider url

    private void getSlider() {
        userListViewModel.getSliderUrl();
        userListViewModel.getSliderLiveData.observe(getActivity(), new Observer<List<SliderClass>>() {
            @Override
            public void onChanged(List<SliderClass> sliderClasses) {
                show_quotes.setText(sliderClasses.get(0).getUrl());
            }
        });
    }
    //slider
    private void setSliderViews() {

        for (int i = 0; i <= 3; i++) {

            SliderView sliderView = new SliderView(getActivity());

            switch (i) {
                case 0:
                    sliderView.setImageUrl("https://bit.ly/3dt7Iyt");
                    break;
                case 1:
                    sliderView.setImageUrl("https://img.collegedekhocdn.com/media/img/institute/crawled_images/None/ADT3.jpg?tr=h-400,w-650");
                    break;
                case 2:
                    sliderView.setImageUrl("https://img.collegedekhocdn.com/media/img/institute/crawled_images/None/ADT1.jpg?tr=h-400,w-650");
                    break;
                case 3:
                    sliderView.setImageUrl("https://i.ytimg.com/vi/c7DUvUrc0kQ/maxresdefault.jpg");
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            sliderView.setDescription("ACOE " + (i + 1));

//            final int finalI = i;
//            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
//                @Override
//                public void onSliderClick(SliderView sliderView) {
//                    Toast.makeText(getContext(), "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
//                }
//            });

            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView);
        }
    }
}