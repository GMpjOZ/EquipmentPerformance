package com.pj.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import com.pj.model.AnalysData;

@Service
public class ResultService {
	@Resource
	private SessionFactory sessionFactory;
	@Resource
	private CalculateService calculate;
	@Resource
	private XMLService xmlService;

	/**
	 * 返回部门列表
	 * 
	 * @return
	 */
	public List<String> getDeptList() {
		String hql = "select asset.asset_dept from Asset asset";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		List<String> deptlist = query.list();
		return deptlist;
	}

	/**
	 * 进行计算的统一入口，进行各种指标计算
	 * 
	 * @param analysiticType
	 *            计算的类型 取值为"Utilization"、"Availability"、"FailRate"、"DownLosses"、
	 *            "Unwounded"、"MRRT"、"Scrap"
	 * @param model
	 *            计算针对的粒度（某类设备or某个设备）取值为"Type"、"Asset"
	 * @param assetType
	 *            设备类型，当model为"Type"时为空
	 * @param time
	 *            时间，当analysiticType为"Unwounded" 用到
	 * @return json格式的数据
	 */
	public String getCalculateResult(String analysiticType, String model,
			String assetType, String time) {
		Map<String, Double> map = new HashMap<String, Double>();

		String sql = "select count(*) from ";
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		String count = (String) query.uniqueResult();

		AnalysData analysData = xmlService.readCalculate(analysiticType, model);

		if (analysData.getData() == null
				|| !analysData.getCount().equals(count)) {

			if (model.equals("Asset") && assetType != null) {

				switch (analysiticType) {
				case "Utilization": {
					calculate.calculateUtilization(map, assetType, model);
					break;
				}
				case "Availability": {
					calculate.calculateAvalOrFail(map, assetType, model,
							analysiticType);
					break;
				}
				case "FailRate": {
					calculate.calculateAvalOrFail(map, assetType, model,
							analysiticType);
					break;
				}
				case "DownLosses": {
					calculate.calculateDownLosses(map, assetType, model);
					break;
				}
				case "MRRT": {
					calculate.calculateMRRT(map, assetType, model);
					break;
				}
				}

			} else if (model.equals("Type")) {

				String hql = "select asset.asset_type from Asset asset";
				Query hquery = sessionFactory.getCurrentSession().createQuery(
						hql);
				List<String> typelist = hquery.list();

				switch (analysiticType) {
				case "Utilization": {
					for (String assettype : typelist) {
						calculate.calculateUtilization(map, assettype, model);
					}
					break;
				}
				case "Availability": {
					for (String assettype : typelist) {
						calculate.calculateAvalOrFail(map, assetType, model,
								analysiticType);
					}
					break;
				}
				case "FailRate": {
					for (String assettype : typelist) {
						calculate.calculateAvalOrFail(map, assetType, model,
								analysiticType);
					}
					break;
				}
				case "DownLosses": {
					for (String assettype : typelist) {
						calculate.calculateDownLosses(map, assettype, model);
					}
					break;
				}
				case "Unwounded": {
					for (String assettype : typelist) {
						calculate.calculateUnwounded(map, assetType, time);
					}
					break;
				}
				case "MRRT": {
					for (String assettype : typelist) {
						calculate.calculateMRRT(map, assettype, model);
					}
					break;
				}
				case "Scrap": {
					for (String assettype : typelist) {
						calculate.calculateScrap(map, assetType, "2016");
					}
					break;
				}
				}

			}
			JSONObject json = JSONObject.fromObject(map);
			String result = json.toString();
			xmlService.writeOruodateCalculate(analysiticType, model, result, count);
			return result;
			
		} else if (analysData.getCount().equals(count)) {
			return analysData.getData();
			
		}
		return "";
	}
}
