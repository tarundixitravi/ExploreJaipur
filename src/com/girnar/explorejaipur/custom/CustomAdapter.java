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

public class CustomAdapter extends ArrayAdapter<String>{
	Activity con;
	ArrayList<String> upperText;
	ArrayList<String> lowerText;
	int img;
	int distanceImage;
///////////////////////////////////////////////////////////////////////////
	public CustomAdapter(Activity context,ArrayList<String> data ,ArrayList<String> data2,int image,int image2) {
		super(context,R.layout.listcontent,data);
		con=context;
		upperText=data;
		lowerText=data2;
		img=image;	
		distanceImage=image2;
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
			holder.sub=(TextView)convertView.findViewById(R.id.text_subhead_listcontente);
			holder.imageView=(ImageView)convertView.findViewById(R.id.image_listcontent);
			holder.imageViewSmall=(ImageView)convertView.findViewById(R.id.image_distance_listcontent);
			//if(position==0)
			convertView.setTag(holder);
		}
		else
		holder=(Holder) convertView.getTag();
		holder.heading.setText(upperText.get(position));
		holder.sub.setText(lowerText.get(position));
		holder.imageView.setImageResource(img);
		holder.imageViewSmall.setImageResource(distanceImage);
		return convertView;	
	}
///////////////////////////////////////////////////////////////////////////////
}


