package com.pj.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import utils.API;
import utils.TimeUtils;

import com.pj.model.Cost;
import com.pj.model.Operation;
import com.pj.model.Repair;
import com.pj.model.Scrap;
@Service
public class CalculateService {
	@Resource
	private SessionFactory sessionFactory;

/**
 *  计算使用率
 * @param map 存放使用率的容器
 * @param assetType 计算使用率的类型
 * @param model 计算模式 
 * @return
 */
	public Map<String, Double> calculateUtilization(Map<String, Double> map,
			String assetType, String model) {
		String hql = "from Operation op where op.asset_type=? ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, assetType);
		List<Operation> ops = query.list();
		String startTime = API.STARTTIME;
		String endTime = API.ENDTIME;
		Map<String, Long> assetTime = new HashMap<String, Long>();
		// Map<String, Long> assetSumTime = new HashMap<String, Long>();
		Set<String> set = new HashSet<String>();
		for (Operation op : ops) {
			startTime = TimeUtils.getMinTime(startTime, op.getOp_strt_time());
			endTime = TimeUtils.getMaxTime(endTime, op.getOp_end_time());
			Long time = TimeUtils.getDeltaTime(op.getOp_strt_time(),
					op.getOp_end_time());
			if (assetTime.get(op.getAsset_id()) == null) {
				assetTime.put(op.getAsset_id(), time);
			} else {
				long temp = assetTime.get(op.getAsset_id());
				assetTime.put(op.getAsset_id(), time + temp);
			}
		}
		if (model.equals("Asset")) {
			for (Operation op : ops) {
				if (!set.contains(op.getAsset_id())) {
					hql = "select asset.asset_dept from Asset asset where asset.id=?";
					query = sessionFactory.getCurrentSession().createQuery(hql);
					query.setString(0, assetType);
					String dept = (String) query.uniqueResult();
					set.add(op.getAsset_id());
					long sumTime = TimeUtils.getSumTime(startTime, endTime,
							dept);
					double utilization = assetTime.get(op.getAsset_id())
							/ sumTime * 100;
					map.put(op.getAsset_id(), utilization);
				}
			}
		} else if (model.equals("Type")) {
			long sumTime = 0;
			long utilizationTime = 0;
			for (Operation op : ops) {
				if (!set.contains(op.getAsset_id())) {
					hql = "select asset.asset_dept from Asset asset where asset.id=?";
					query = sessionFactory.getCurrentSession().createQuery(hql);
					query.setString(0, op.getAsset_id());
					String dept = (String) query.uniqueResult();
					set.add(op.getAsset_id());
					sumTime = sumTime
							+ TimeUtils.getSumTime(startTime, endTime, dept);
					utilizationTime = utilizationTime
							+ assetTime.get(op.getAsset_id());
				}
			}
			double utilization = utilizationTime / sumTime * 100;
			map.put(assetType, utilization);
		}

		return map;
	}

	/**
	 * 计算可利用率和故障率
	 * @param map 容器 
	 * @param assetType 设备类型
	 * @param model 计算模式
	 * @param analyticType 方法
	 * @return
	 */
	public Map<String, Double> calculateAvalOrFail(Map<String, Double> map,
			String assetType, String model, String analyticType) {
		String hql = "from Repair repair where repair.asset_type=? ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, assetType);
		List<Repair> repairs = query.list();
		String startTime = API.STARTTIME;
		String endTime = API.ENDTIME;
		Map<String, Long> assetTime = new HashMap<String, Long>();
		Set<String> set = new HashSet<String>();
		for (Repair repair : repairs) {
			startTime = TimeUtils.getMinTime(startTime, repair.getDecl_time());
			endTime = TimeUtils.getMaxTime(endTime, repair.getRep_end_time());
			Long time = TimeUtils.getDeltaTime(repair.getDecl_time(),
					repair.getRep_end_time());
			if (assetTime.get(repair.getAsset_id()) == null) {
				assetTime.put(repair.getAsset_id(), time);
			} else {
				long temp = assetTime.get(repair.getAsset_id());
				assetTime.put(repair.getAsset_id(), time + temp);
			}
		}
		if (model.equals("Asset")) {
			for (Repair repair : repairs) {
				if (!set.contains(repair.getAsset_id())) {
					hql = "select asset.asset_dept from Asset asset where asset.id=?";
					query = sessionFactory.getCurrentSession().createQuery(hql);
					query.setString(0, repair.getAsset_id());
					String dept = (String) query.uniqueResult();
					set.add(repair.getAsset_id());
					long sumTime = TimeUtils.getSumTime(startTime, endTime,
							dept);
					double result;
					if (analyticType.equals("Availability")) {
						result = assetTime.get(repair.getAsset_id()) / sumTime
								* 100;
					} else {
						result = (sumTime - assetTime.get(repair.getAsset_id()))
								/ sumTime * 100;
					}
					map.put(repair.getAsset_id(), result);
				}
			}
		} else if (model.equals("Type")) {
			long sumTime = 0;
			long failTime = 0;
			for (Repair repair : repairs) {
				if (!set.contains(repair.getAsset_id())) {
					hql = "select asset.asset_dept from Asset asset where asset.id=?";
					query = sessionFactory.getCurrentSession().createQuery(hql);
					query.setString(0, repair.getAsset_id());
					String dept = (String) query.uniqueResult();
					set.add(repair.getAsset_id());
					sumTime = sumTime
							+ TimeUtils.getSumTime(startTime, endTime, dept);
					failTime = failTime + assetTime.get(repair.getAsset_id());
				}
			}
			double result;
			if (analyticType.equals("Availability")) {
				result = failTime / sumTime * 100;
			} else {
				result = (sumTime - failTime) / sumTime * 100;
			}
			map.put(assetType, result);
		}

		return map;
	}

	/**
	 *  计算停机损失
	 * @param map 容器
	 * @param assetType 设备类型
	 * @param model 计算模式
	 * @return
	 */
	public Map<String, Double> calculateDownLosses(Map<String, Double> map,
			String assetType, String model) {
		String hql = "from Operation op where op.asset_type=? ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, assetType);
		List<Operation> ops = query.list();
		Map<String, Cost> asset_cost = new HashMap<String, Cost>();
		for (Operation op : ops) {
			if (asset_cost.get(op.getAsset_id()) != null) {
				Cost cost = asset_cost.get(op.getAsset_id());
				cost.op_cost = cost.op_cost
						+ Double.parseDouble(op.getOp_cost());
				cost.op_time = cost.op_time
						+ TimeUtils.getDeltaTime(op.getOp_strt_time(),
								op.getOp_end_time());
				asset_cost.put(op.getAsset_id(), cost);
			} else {
				Cost cost = new Cost();
				cost.op_cost = Double.parseDouble(op.getOp_cost());
				cost.op_time = TimeUtils.getDeltaTime(op.getOp_strt_time(),
						op.getOp_end_time());
				asset_cost.put(op.getAsset_id(), cost);
			}
		}

		hql = "from Repair rapair where rapair.asset_type=?";
		query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, assetType);
		List<Repair> repairs = query.list();
		for (Repair repair : repairs) {
			if (asset_cost.get(repair.getAsset_id()) != null) {
				Cost cost = asset_cost.get(repair.getAsset_id());
				cost.fail_time = cost.fail_time
						+ TimeUtils.getDeltaTime(repair.getDecl_time(),
								repair.getRep_end_time());
				cost.repair_time = cost.repair_time
						+ TimeUtils.getDeltaTime(repair.getRep_strt_time(),
								repair.getRep_end_time());
				cost.repair_cost = cost.repair_cost
						+ Double.parseDouble(repair.getRep_cost());
				cost.repair_count = cost.repair_count + 1;
				asset_cost.put(repair.getAsset_id(), cost);
			} else {
				Cost cost = new Cost();
				cost.fail_time = TimeUtils.getDeltaTime(repair.getDecl_time(),
						repair.getRep_end_time());
				cost.repair_time = TimeUtils.getDeltaTime(
						repair.getRep_strt_time(), repair.getRep_end_time());
				cost.repair_cost = Double.parseDouble(repair.getRep_cost());
				cost.repair_count = 1;
				asset_cost.put(repair.getAsset_id(), cost);
			}
		}
		double op_cost = 0.0;
		long op_time = 0;
		double repair_cost = 0;
		long fail_time = 0;
		long repair_time = 0;
		int repair_count = 0;
		if (model.equals("Type")) {

			for (String id : asset_cost.keySet()) {
				op_cost = op_cost + asset_cost.get(id).op_cost;
				op_time = op_time + asset_cost.get(id).op_time;
				repair_cost = repair_cost + asset_cost.get(id).repair_cost;
				fail_time = fail_time + asset_cost.get(id).fail_time;
				repair_time = repair_time + asset_cost.get(id).repair_time;
				repair_count = repair_count + asset_cost.get(id).repair_count;
			}
			double result = (op_cost / op_time + repair_cost / repair_time)
					* fail_time / repair_count;
			map.put(assetType, result);

		} else {
			for (String id : asset_cost.keySet()) {
				op_cost = asset_cost.get(id).op_cost;
				op_time = asset_cost.get(id).op_time;
				repair_cost = asset_cost.get(id).repair_cost;
				fail_time = asset_cost.get(id).fail_time;
				repair_time = asset_cost.get(id).repair_time;
				repair_count = asset_cost.get(id).repair_count;

				double result = (op_cost / op_time + repair_cost / repair_time)
						* fail_time / repair_count;
				map.put(id, result);
			}
		}

		return map;
	}

	/**
	 * 计算某段时间的设备完好率
	 * @param map 容器
	 * @param assetType 设备类型
	 * @param time 时间
	 * @return
	 */
	public Map<String, Double> calculateUnwounded(Map<String, Double> map,
			String assetType, String time) {
		String hql = "from Repair rapair where rapair.asset_type=? and repair.decl_time like ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, assetType);
		query.setString(1, time + "%");
		List<Repair> repairs = query.list();
		Set<String> set = new HashSet<String>();
		for (Repair repair : repairs) {
			set.add(repair.getAsset_id());
		}
		map.put(assetType, (double) set.size() / API.GoodAsset);
		return map;
	}

	/**
	 * 计算MRRT（设备的平均修复时间）
	 * @param map 容器
	 * @param assetType 设备类型
	 * @param model 计算模式
	 * @return
	 */
	public Map<String, Double> calculateMRRT(Map<String, Double> map,
			String assetType, String model) {
		String hql = "from Repair rapair where rapair.asset_type=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, assetType);
		List<Repair> repairs = query.list();
		Map<String, Cost> asset_time = new HashMap<String, Cost>();
		for (Repair repair : repairs) {
			if (asset_time.get(repair.getAsset_id()) == null) {
				Cost cost = new Cost();
				long time = TimeUtils.getDeltaTime(repair.getDecl_time(),
						repair.getRep_end_time());
				cost.fail_time = time;
				cost.repair_count = 1;
				asset_time.put(repair.getAsset_id(), cost);

			} else {
				Cost cost = asset_time.get(repair.getAsset_id());
				long time = TimeUtils.getDeltaTime(repair.getDecl_time(),
						repair.getRep_end_time())
						+ asset_time.get(repair.getAsset_id()).fail_time;
				int count = asset_time.get(repair.getAsset_id()).repair_count + 1;
				cost.fail_time = time;
				cost.repair_count = count;
				asset_time.put(repair.getAsset_id(), cost);
			}
		}
		if (model.equals("Asset")) {
			for (String id : asset_time.keySet()) {
				Cost cost = asset_time.get(id);
				map.put(id, (double) cost.fail_time / cost.repair_count);
			}
		} else if (model.equals("Type")) {
			long failtime = 0;
			int count = 0;
			for (String id : asset_time.keySet()) {
				failtime = asset_time.get(id).fail_time + failtime;
				count = count + asset_time.get(id).repair_count;
			}
			map.put(assetType, (double) failtime / count);
		}
		return map;
	}

	/**
	 * 计算某段时间内的报废率
	 * @param map 容器
	 * @param assetType 设备类型
	 * @param time 时间
	 * @return
	 */
	public Map<String, Double> calculateScrap(Map<String, Double> map,
			String assetType, String time) {
		String hql = "from Scrap scrap where scrap.asset_type=? and scrap_time like ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, assetType);
		query.setString(1, time);
		List<Scrap> scraps = query.list();
		map.put(assetType, (double)scraps.size() / API.GoodAsset);
		return map;
	}
}
