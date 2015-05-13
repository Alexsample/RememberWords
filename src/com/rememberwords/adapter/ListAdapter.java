package com.rememberwords.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.w1.R;

public class ListAdapter extends BaseAdapter {
	
	private ArrayList<String> adapterList;
	private ArrayList<String> adapterList2;
	private String type = "en_rus";
	private Context context;
	
	public ListAdapter(Context context, ArrayList<String> adapterList, ArrayList<String> adapterList2) {
		this.context = context;
		this.adapterList = adapterList;
		this.adapterList2 = adapterList2;
	}
	
	@Override
	public int getCount() {
		return adapterList.size();
	}

	@Override
	public Object getItem(int position) {
		return adapterList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
    
    
    @Override     
    public View getView(final int position, View convertView, ViewGroup parent) {
    	ViewHolder viewholder;
    	if (convertView == null) {            
    		LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		convertView = inflator.inflate(R.layout.list_element, parent, false);
    		
    		viewholder = new ViewHolder();
			viewholder.text = (TextView) convertView.findViewById(R.id.tvListElement);
			viewholder.number = (TextView) convertView.findViewById(R.id.tvElementNumber);
			
			convertView.setTag(viewholder);
    	} else {
    		viewholder = (ViewHolder) convertView.getTag();
    	}
    	
    	ViewHolder holder = (ViewHolder) convertView.getTag();
    	if (type.equalsIgnoreCase("en")) {
    		holder.text.setText(adapterList.get(position));
    	} else if (type.equalsIgnoreCase("rus")) {
    		holder.text.setText(adapterList2.get(position));
    	} else {
    		holder.text.setText(adapterList.get(position) + " - " + adapterList2.get(position));
    	}
    	holder.number.setText(""+(position+1));
    	
    	return convertView;
     }
    
    private class ViewHolder {        
    	protected TextView text, number;
    }
    
    public void setType(String type) {
    	this.type = type;
    }
	
}
