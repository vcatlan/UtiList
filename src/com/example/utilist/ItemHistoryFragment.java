package com.example.utilist;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.model.History;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class ItemHistoryFragment extends DialogFragment{

    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }

    //private ListView mListView;
    private History history;
    private ItemSelectDialogToActivity mListener;
    private int itemType;
    private CharSequence [] charSequences;
    private AlertDialog alertDialog;
    public ItemHistoryFragment() {
        // Empty constructor required for DialogFragment
    }
    
    public ItemHistoryFragment(History history, int itemType) {
        this.history = history;
        this.itemType = itemType;
    }
    
    @Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{
			mListener = (ItemSelectDialogToActivity) activity;
			
		} catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " ItemSelectDialogToActivity");
        }		
	}
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_item_history_dialog, null);

//    	CharSequence [] charSequences = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Pick an item");
        if(itemType == 1){
        	charSequences = history.getItemCarSequence();
        }
        else if(itemType == 2){
        	charSequences = history.getItemTodoCarSequence();
        }
        if(charSequences.length !=0){
        	builder.setItems(charSequences, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mListener.ItemSelectDialogListener(charSequences[which].toString());
		            dismiss();
							
				}
        	});
        	builder.setNeutralButton("Colse", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dismiss();
					
				}
        		
        	});
        }
        else{
        	builder.setMessage("No item in the history");
        }
        alertDialog = builder.create();
        
        return alertDialog;
    }
    public interface ItemSelectDialogToActivity{
		public void ItemSelectDialogListener(String itemName);
		
	}

}

