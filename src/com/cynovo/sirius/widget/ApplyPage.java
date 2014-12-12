package com.cynovo.sirius.widget;

import com.cynovo.sirius.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ApplyPage extends Fragment {
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        // TODO Auto-generated method stub  
        super.onCreate(savedInstanceState);  
    }  
      
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View fragmentView = inflater.inflate(R.layout.apply_page, container, false);  
        return fragmentView;  
    }  
}
