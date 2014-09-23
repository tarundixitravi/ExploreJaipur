package com.girnar.explorejaipur.jsondata;

import android.app.ProgressDialog;

/**
 *  this class is used for showing and dismiss the progress dialog for Async Task in Activities
 *
 */
public class DialogShow {

	/**
	 * this method shows the progress dialog
	 * @param ProgressDialog Instance 
	 *  @return ProgressDialog with title and message
	 * 
	 */
	public static ProgressDialog showProgressDialog(ProgressDialog p_desc_Dialog,String data){
//		"Please Wait While Getting Data...."
		p_desc_Dialog.setMessage(data);
		p_desc_Dialog.setIndeterminate(false);
		p_desc_Dialog.setCancelable(false);
		p_desc_Dialog.show();
		return p_desc_Dialog;
	}
	
	/**
	 *  this method close progress dialog 
	 *  @param this takes the progress dialog instance which we want to close
	 * 
	 */
	public static void closeProgressDialog(ProgressDialog progressDialog){
		progressDialog.dismiss();
	}
	
	
		
}
