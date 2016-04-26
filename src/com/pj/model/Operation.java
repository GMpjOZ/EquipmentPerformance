package com.pj.model;

import java.util.Set;

public class Operation {

	private String op_id;
	private String op_strt_time;
	private String op_end_time;
	private String op_dept;
	private String asset_id;
	private String asset_type;
	
	private String tables;
	private String rules;
	
	
	public String getTables() {
		return tables;
	}
	public void setTables(String tables) {
		this.tables = tables;
	}
	public String getRules() {
		return rules;
	}
	public void setRules(String rules) {
		this.rules = rules;
	}
	public String getOp_id() {
		return op_id;
	}
	public void setOp_id(String op_id) {
		this.op_id = op_id;
	}
	public String getOp_strt_time() {
		return op_strt_time;
	}
	public void setOp_strt_time(String op_strt_time) {
		this.op_strt_time = op_strt_time;
	}
	public String getOp_end_time() {
		return op_end_time;
	}
	public void setOp_end_time(String op_end_time) {
		this.op_end_time = op_end_time;
	}
	public String getOp_dept() {
		return op_dept;
	}
	public void setOp_dept(String op_dept) {
		this.op_dept = op_dept;
	}
	public String getAsset_id() {
		return asset_id;
	}
	public void setAsset_id(String asset_id) {
		this.asset_id = asset_id;
	}
	public String getAsset_type() {
		return asset_type;
	}
	public void setAsset_type(String asset_type) {
		this.asset_type = asset_type;
	}
	
}
