package com.cynovo.sirius.widget;

import java.util.ArrayList;
import java.util.List;

import com.cynovo.sirius.R;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ButtonGroupBox extends RadioGroup {
	Context mContext;
	protected enum GroupType{SINGLECHECK,MULTYCHECK,SINGLECLICK}; 
	GroupType mType = GroupType.SINGLECHECK;
	protected int count = 0;
	protected List<Button> mButtonList = new ArrayList<Button>();
	protected int  mHeight = -1;
	public ButtonGroupBox(Context context )
	{
		super(context);
	}
	
	protected ButtonGroupBox(Context context , GroupType type, int count) {
		super(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(               
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		this.setLayoutParams(lp);
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setGravity(Gravity.CENTER);
		this.mContext = context;
		this.mType = type;
		this.count = count;
		
		for(int i = 0;i < count ; i++)
		{
			Button tmp = createButton();
			ColorStateList csl = (ColorStateList) mContext.getResources().getColorStateList(R.color.text_normal_color);
			tmp.setTextColor(csl);
			tmp.setTextSize(30);
			mButtonList.add(tmp);
			this.addView(tmp, i);
		}
	}
	
	public void setHeight(int h)
	{	
		this.mHeight = h;
	}
	
	public List<Button> getButtonList()
	{
		return mButtonList;
	}
	
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		Log.d("ButtonGroupBox", "      "+mHeight+" "+ count +"  "+w+" "+h+" "+oldw+" "+oldh+" "+this.toString());
		int tmp = mHeight>0?mHeight:LayoutParams.MATCH_PARENT;
		if(count == 0)
			return;
		LinearLayout.LayoutParams clp = new LayoutParams(               
				w/count, tmp);
		for(int i = 0;i < count ; i++)
		{
			mButtonList.get(i).setLayoutParams(clp);
			mButtonList.get(i).invalidate();
		}
		this.invalidate();
	}
	
	private Button createButton()
	{
		switch (mType)
		{
		case SINGLECHECK:
			RadioButton tmpr = new RadioButton(mContext);
			tmpr.setGravity(Gravity.CENTER);
			tmpr.setBackgroundResource(R.drawable.checkbox);
			tmpr.setButtonDrawable(android.R.color.transparent);
			return  tmpr;
		case MULTYCHECK:
			CheckBox tmpm = new CheckBox(mContext);
			tmpm.setGravity(Gravity.CENTER);
			tmpm.setBackgroundResource(R.drawable.checkbox);
			tmpm.setButtonDrawable(android.R.color.transparent);
			return  tmpm;
		case SINGLECLICK:
			Button btn = new Button(mContext);
			btn.setGravity(Gravity.CENTER);
			return btn;
		default:
			return null;
		}
	}

}
