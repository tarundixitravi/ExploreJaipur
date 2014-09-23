package com.girnar.explorejaipur.custom;

import java.util.ArrayList;

import com.girnar.explorejaipur.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomSpinner extends ArrayAdapter<String>{
	Activity con;
	ArrayList<String> rautNo;
///////////////////////////////////////////////////////////////////////	
	public CustomSpinner(Activity context,ArrayList<String> data) {
		super(context,R.layout.spinner_layout,R.id.text_heading_spinner,data);
		con=context;
		rautNo=data;
	}
///////////////////////////////////////////////////////////////////////
	static class Holder{
		TextView tv;
	}
/////////////////////////////////////////////////////////////////
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if(convertView==null)
		{
				holder=new Holder();
				LayoutInflater inflater=con.getLayoutInflater();
				convertView=inflater.inflate(R.layout.spinner_layout, null);
				holder.tv=(TextView)convertView.findViewById(R.id.text_heading_spinner);
				//if(position==0)
				convertView.setTag(holder);
		}
		else
			holder=(Holder) convertView.getTag();
		holder.tv.setText(rautNo.get(position));		
		return convertView;
	}
//////////////////////////////////////////////////////////////
}