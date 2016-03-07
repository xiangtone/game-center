package config
import java.io.File;

import com.mas.rave.vo.AppResourceVO;
import com.mas.rave.common.ImportRule;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.io.FileUtils;

public class AppImportRule implements ImportRule{

	def fields = [
		"appName":["is_must":true,"len":100],
		"fatherChannelId":["is_must":true,"is_number":true,"len":11],
		"channelId":["is_must":true,"is_number":true,"len":11],
		"cpId":["is_must":true,"is_number":true,"len":20],
		"free":["is_must":true,"is_number":true,"len":11],
		"categoryId":["is_must":true,"is_number":true,"len":8],
		"brief":["is_must":true,"len":200],
		"stars":["is_must":true,"is_number":true,"len":11],
		"state":["is_must":true,"is_number":true,"len":1],
		"sort":["is_must":true,"is_number":true,"len":8],
		"serverId":["is_must":true,"is_number":true,"len":11],
		"upgradeType":["is_must":true,"is_number":true,"len":1],
		"osType":["is_must":true,"is_number":true,"len":11],
		"language":["is_must":true,"is_number":true,"len":11],
		"updateRemark":["is_must":true,"len":30],
		"initDowdload":["is_must":true,"is_number":true,"len":11],
		"picUrl":["is_must":true,"len":500],
		"picTitle":["is_must":true,"len":100],
		"picDesc":["is_must":true,"len":500],
		"type":["is_must":true]
	];
	
	public Object getResource(){
		return new AppResourceVO();
	}
	
	
	public String checkField(String fieldName,String value,Object resource){
		def appResource = (AppResourceVO)resource;
		def rules = fields[fieldName];
		def isNumber = false;
		def iter = rules.entrySet().iterator();
		while (iter.hasNext()) {
			def entry = iter.next();
			def key = entry.getKey();
			def val = entry.getValue();
			if(key == "is_must" && val == true && StringUtils.isEmpty(value)){
				return "字段["+ fieldName +"]不能为空";
			}else if(key == "len" && StringUtils.isNotEmpty(value) && value.length() > val){
				return "字段["+ fieldName +"]不能超过["+ val + "]字符";
			}else if(key == "is_number"){
				isNumber = val
			}
		}

		if(rules){
			if(isNumber){
				appResource[fieldName] = value.toInteger();
			}else{
				appResource[fieldName] = value;
			}
		}
		return null;
	}
	
	
}



