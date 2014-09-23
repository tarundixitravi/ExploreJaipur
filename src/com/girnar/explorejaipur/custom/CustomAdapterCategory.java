package com.girnar.explorejaipur.custom;

import java.util.ArrayList;

import com.girnar.explorejaipur.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapterCategory extends ArrayAdapter<String>{
	Activity con;
	ArrayList<String> upperText;
	int img;
///////////////////////////////////////////////////////////////////////////
	public CustomAdapterCategory(Activity context,ArrayList<String> data ,int image) {
		super(context,R.layout.listcontent,data);
		con=context;
		upperText=data;
		img=image;	
	}
///////////////////////////////////////////////////////////////////
	static class Holder{
		TextView heading;
		ImageView imageView,imageViewSmall;
		TextView sub;
	}
//////////////////////////////////////////////////////////////////
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if(convertView==null){
			holder=new Holder();
			LayoutInflater inflater=con.getLayoutInflater();
			convertView=inflater.inflate(R.layout.listcontent, null);
			holder.heading=(TextView)convertView.findViewById(R.id.text_heading_listcontent);
			holder.imageView=(ImageView)convertView.findViewById(R.id.image_listcontent);
			//if(position==0)
			convertView.setTag(holder);
		}
		else
		holder=(Holder) convertView.getTag();
		holder.heading.setText(upperText.get(position));
		holder.imageView.setImageResource(img);
		return convertView;	
	}
///////////////////////////////////////////////////////////////////////////////
}


