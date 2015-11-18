package com.kuaizhan.form.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeMsg {

	private int code;
	private String msg;
	private Object data;

	public CodeMsg(int code, String msg) {
		this(code, msg, null);
	}
	
	public static CodeMsg success(){
		return new CodeMsg(0, "OK");
	}
	
	public static CodeMsg failure(){
		return new CodeMsg(-1, "");
	}
	
	public static CodeMsg failure(String msg){
		return new CodeMsg(-1, msg);
	}
	
	public static CodeMsg failure(int code, String msg){
		return new CodeMsg(code, msg);
	}
	
	public static CodeMsg parse(boolean result){
		return parse(result, "");
	}
	
	public static CodeMsg parse(boolean result, String msg){
		return result ? success() : failure(msg);
	}
		
	public CodeMsg code(int code){
		this.code = code;
		return this;
	}

	public CodeMsg msg(String msg){
		this.msg = msg;
		return this;
	}
	
	public CodeMsg data(Object data){
		this.data = data;
		return this;
	}
}
