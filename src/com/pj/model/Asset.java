package com.pj.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "assetview")
public class Asset {
	@Column
	private String asset_id;
	@Column
	private String asset_name;
	@Column
	private String asset_type;
	@Column
	private String asset_dept;

	private String tables;
	private String rules;

	public String getAsset_id() {
		return asset_id;
	}

	public void setAsset_id(String asset_id) {
		this.asset_id = asset_id;
	}

	public String getAsset_name() {
		return asset_name;
	}

	public void setAsset_name(String asset_name) {
		this.asset_name = asset_name;
	}

	public String getAsset_type() {
		return asset_type;
	}

	public void setAsset_type(String asset_type) {
		this.asset_type = asset_type;
	}

	public String getAsset_dept() {
		return asset_dept;
	}

	public void setAsset_dept(String asset_dept) {
		this.asset_dept = asset_dept;
	}

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

}
