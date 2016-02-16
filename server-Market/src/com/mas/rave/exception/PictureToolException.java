package com.mas.rave.exception;


public class PictureToolException extends Exception {
        private String errMsg = ""; 
        public PictureToolException(String errMsg) 
        { 
                this.errMsg = errMsg; 
        } 

        public String getMsg(){ 
            return "JpegToolException:"+this.errMsg; 
        } 
}