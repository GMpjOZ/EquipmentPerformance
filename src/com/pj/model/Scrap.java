package com.pj.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

//@Entity
@Table(name = "scrapview")
public class Scrap {
	@Column
	private String asset_id;
	@Column
	private String asset_type;
	@Column
	private String scrap_time;
	@Column
	private String scrap_id;
	@Column
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

	public String getScrap_time() {
		return scrap_time;
	}

	public void setScrap_time(String scrap_time) {
		this.scrap_time = scrap_time;
	}

	public String getScrap_id() {
		return scrap_id;
	}

	public void setScrap_id(String scrap_id) {
		this.scrap_id = scrap_id;
	}

}
