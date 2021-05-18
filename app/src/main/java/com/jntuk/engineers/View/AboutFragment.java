package com.jntuk.engineers.View;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.jntuk.engineers.R;
import com.jntuk.engineers.View.Teacher.TeacherDetailsActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_about, container, false);

        root.findViewById(R.id.facebook_imgId).setOnClickListener((View view)->{
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.facebook.com/asifs.cse"));
                startActivity(intent);
            } catch(Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.facebook.com/asifs.cse")));
            }
        });
        root.findViewById(R.id.youtube_imgId).setOnClickListener((View view)->{
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCvKxlnujRq-CgJkK3fETXNA"));
                startActivity(intent);
            } catch(Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCvKxlnujRq-CgJkK3fETXNA")));
            }
        });
        root.findViewById(R.id.gmail_imageId).setOnClickListener((View view)->{
            try{
                Intent intent = new Intent (Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"asifs.cse@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Any subject if you want");
                intent.setPackage("com.google.android.gm");
                if (intent.resolveActivity(getActivity().getPackageManager())!=null)
                    startActivity(intent);
                else
                    Toast.makeText(getContext(),"Gmail App is not installed", Toast.LENGTH_SHORT).show();
            }catch(ActivityNotFoundException e){
                //TODO smth
            }
        });
        root.findViewById(R.id.privacy_policy).setOnClickListener((View view)->{
            try {
                Intent intent = new Intent(getContext(), WebService.class);
                intent.putExtra("url","https://asiflab.blogspot.com/2021/04/privacy-policy-asif-shahriar-built.html");
                startActivity(intent);
            } catch(Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://asiflab.blogspot.com/2021/04/privacy-policy-asif-shahriar-built.html")));
            }
        });

        root.findViewById(R.id.wap_imageId).setOnClickListener((View view)->{
            openWhatsApp();
        });

        return root;
    }
    private void openWhatsApp() {
        String message="Hello! ";
        String url = "https://api.whatsapp.com/send?phone=" + "+919903269265" + "&text="+message;
        try {
            PackageManager pm = getActivity().getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getActivity(), "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    //Create method appInstalledOrNot
    private boolean appInstalledOrNot(String url){
        PackageManager packageManager =getActivity().getPackageManager();
        boolean app_installed;
        try {
            packageManager.getPackageInfo(url,PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }catch (PackageManager.NameNotFoundException e){
            app_installed = false;
        }
        return app_installed;
    }
}
