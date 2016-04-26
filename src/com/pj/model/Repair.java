package com.pj.model;

public class Repair {
private String repair_id;
private String asset_id;
private String asset_type;
private String rep_strt_time;
private String rep_end_time;
private String rep_cost;
private String decl_time;
public String getRepair_id() {
	return repair_id;
}
public void setRepair_id(String repair_id) {
	this.repair_id = repair_id;
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
public String getRep_strt_time() {
	return rep_strt_time;
}
public void setRep_strt_time(String rep_strt_time) {
	this.rep_strt_time = rep_strt_time;
}
public String getRep_end_time() {
	return rep_end_time;
}
public void setRep_end_time(String rep_end_time) {
	this.rep_end_time = rep_end_time;
}
public String getRep_cost() {
	return rep_cost;
}
public void setRep_cost(String rep_cost) {
	this.rep_cost = rep_cost;
}
public String getDecl_time() {
	return decl_time;
}
public void setDecl_time(String decl_time) {
	this.decl_time = decl_time;
}

}
