package com.FastCast.Response;

public class Response {
	
	private Object obj;
	
	private String Description ;

	public Response(Object obj, String description) {
		super();
		this.obj = obj;
		Description = description;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}
	
	
}
