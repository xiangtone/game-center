package com.x.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.db.resource.NativeResourceConstant.FileType;
import com.x.publics.model.FileBean;
import com.x.publics.utils.Utils;

/**
 * Document  Holder
 
 *
 */
public class DocumentHolder {

	private ImageView imageView = null;
	private TextView name, size, date = null;
	public CheckBox checkBox = null;

	public DocumentHolder(View view) {
		if (view != null) {
			imageView = (ImageView) view.findViewById(R.id.doc_image);
			name = (TextView) view.findViewById(R.id.doc_name);
			size = (TextView) view.findViewById(R.id.doc_size);
			date = (TextView) view.findViewById(R.id.doc_date);
			checkBox = (CheckBox) view.findViewById(R.id.docs_check);
		}
	}

	public void initData(FileBean bean) {
		int type = Utils.getFileType(bean.getFilePath());
		switch (type) {
		case FileType.TXT:
			imageView.setImageResource(R.drawable.ic_resource_txt);
			break;
		case FileType.DOC:
			imageView.setImageResource(R.drawable.ic_resource_doc);
			break;
		case FileType.XLS:
			imageView.setImageResource(R.drawable.ic_resource_xls);
			break;
		case FileType.PDF:
			imageView.setImageResource(R.drawable.ic_resource_pdf);
			break;
		case FileType.PPT:
			imageView.setImageResource(R.drawable.ic_resource_ppt);
			break;
		case FileType.UNKNOWN:
			imageView.setImageResource(R.drawable.ic_resource_unknown);
			break;
		}
		name.setText(bean.getFileName());
		size.setText(Utils.sizeFormat2(bean.getFileSize()));
		date.setText(Utils.formatData(bean.getModifiedDate()));
		checkBox.setVisibility(bean.getStatus() == 1 ? View.VISIBLE : View.INVISIBLE);
		checkBox.setChecked(bean.getIscheck() == 1 ? true : false);
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @param @param context    
	* @return void    
	*/
	public void setSkinTheme(Context context) {
		SkinConfigManager.getInstance().setCheckBoxBtnDrawable(context, checkBox, SkinConstan.OPTION_BTN);
	}
}
