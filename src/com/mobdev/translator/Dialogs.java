package com.mobdev.translator;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class Dialogs {		
	public void showEmptyEditTextDialog(Context context) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle(R.string.no_english_letters)
				.setMessage(R.string.type_english_letters)
				.setCancelable(false)
				.setIcon(R.drawable.question_mark)
				.setNegativeButton(R.string.ok_string, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		AlertDialog emptyEditTextDialog = builder.create();
		emptyEditTextDialog.show();
	}
}
