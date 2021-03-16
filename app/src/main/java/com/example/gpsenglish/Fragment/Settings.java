package com.example.gpsenglish.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gpsenglish.Activity.Options;
import com.example.gpsenglish.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Settings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Settings extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Settings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Settings.
     */
    // TODO: Rename and change types and number of parameters
    public static Settings newInstance(String param1, String param2) {
        Settings fragment = new Settings();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false); // view fragment setting

        // over to Profile
        TextView textVieweditprofile = view.findViewById(R.id.textVieweditprofile);//button over to fragment profile
        textVieweditprofile.setOnClickListener(new View.OnClickListener() { //inner class - click the button, and the fragment is start
            @Override
            public void onClick(View v) {

                Options options = (Options) getActivity();//bring the activity by casting
                options.GoToProfile();//func in Options class

            }
        });

        // over to Call
        TextView textViewemergency = view.findViewById(R.id.textViewemergency);//button over to fragment profile
        textViewemergency.setOnClickListener(new View.OnClickListener() { //inner class - click the button, and the fragment is start
            @Override
            public void onClick(View v) {

                Options options = (Options) getActivity();//bring the activity by casting
                options.GoToCall();//func in Options class

            }
        });

        // over to JoinCircle
        TextView textViewJoinCircle = view.findViewById(R.id.textViewJoinCircle);//button over to fragment profile
        textViewJoinCircle.setOnClickListener(new View.OnClickListener() { //inner class - click the button, and the fragment is start
            @Override
            public void onClick(View v) {

                Options options = (Options) getActivity();//bring the activity by casting
                options.GoToJoinCircle();//func in Options class

            }
        });

        // over to Logout
        TextView textViewloguot = view.findViewById(R.id.textViewloguot);
        textViewloguot.setOnClickListener(new View.OnClickListener() { //inner class - click the button, and the fragment is start
            @Override
            public void onClick(View v) {

                Options options = (Options) getActivity();//bring the activity by casting
                options.Logout(v);//func in Options class

            }
        });

        return view;
    }

}