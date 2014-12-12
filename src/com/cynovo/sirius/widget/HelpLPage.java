package com.cynovo.sirius.widget;

import com.cynovo.sirius.R;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HelpLPage extends Fragment {
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        // TODO Auto-generated method stub  
        super.onCreate(savedInstanceState);  
    }  
      
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View fragmentView = inflater.inflate(R.layout.help_l_page, container, false);  
        return fragmentView;  
    }  
}
