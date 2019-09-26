/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.imm.service;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.bas.service.EntVaccineService;
import com.thinkgem.jeesite.modules.imm.dao.VaccineDao;
import com.thinkgem.jeesite.modules.imm.entity.Vaccine;


/**
 * 疫苗登记、免疫记录Service
 * 
 * @author chenguangju
 * @version 2019-09-11
 */
@Service
@Transactional(readOnly = true)
public class VaccineService extends CrudService<VaccineDao, Vaccine> {

	@Autowired
	private EntVaccineService entVaccineService;

	@Override
	public Vaccine get(String id) {
		return super.get(id);
	}

	@Override
	public List<Vaccine> findList(Vaccine vaccine) {
		return super.findList(vaccine);
	}

	@Override
	public Page<Vaccine> findPage(Page<Vaccine> page, Vaccine vaccine) {
		return super.findPage(page, vaccine);
	}

	@Override
	@Transactional(readOnly = false)
	public void save(Vaccine vaccine) {
		super.save(vaccine);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(Vaccine vaccine) {
		super.delete(vaccine);
	}

	/**
	 * 
	 * @title: findEntByIds
	 * @author: my
	 * @date: 2019年9月19日 下午3:25:18
	 * @description: 通过主键数组查询企业信息
	 * @param: vaccine
	 * @return: List<Vaccine>
	 */
	public List<Vaccine> findEntByIds(Vaccine vaccine) {
		return dao.findEntByIds(vaccine);
	}

	/**
	 * @title: vaccineSave
	 * @author: chenguangju
	 * @date: 2019/9/24 15:16
	 * @description: 保存
	 * @param: [vaccineList]
	 * @return: java.lang.String
	 */
	@Transactional(readOnly = false)
	public void vaccineSave(List<Vaccine> vaccineList) {
		//前端不重复数据
		List<Vaccine> clientVaccineList = dealClientVaccine(vaccineList);
		//数据库数据
		List<Vaccine> serverVaccinelist= dao.findList(new Vaccine());
		//判断数据库中的疫苗和前台传过来合并后的疫苗是否有重复
		Map<String,List<Vaccine>> insertAndUpdateMap = dealServerVaccine(clientVaccineList,serverVaccinelist);
		//插入数据
		List<Vaccine> insertList = insertAndUpdateMap.get("insert");
		List<Vaccine> vaccineListNew = Lists.newArrayList();
		if(insertList.size()>0){
			for(Vaccine vaccine:insertList){
				vaccine.preInsert();
				vaccine.setEntNo(entVaccineService.findEntNo(vaccine.getEntName()));
				vaccineListNew.add(vaccine);
			}
			dao.insertForeach(vaccineListNew);
		}
		//更新数据
		List<Vaccine> updateList = insertAndUpdateMap.get("update");
		if(updateList.size()>0){
			dao.updateBatch(updateList);
		}
	}

	/**
	 * @title: dealServerVaccine
	 * @author: chenguangju
	 * @date: 2019/9/24 17:31
	 * @description: 服务端去重
	 * @param: [clientVaccineList,list]
	 * @return: java.util.Map<java.lang.String,java.util.List<com.thinkgem.jeesite.modules.imm.entity.Vaccine>>
	 */
	public Map<String,List<Vaccine>> dealServerVaccine(List<Vaccine> clientVaccineList,List<Vaccine> serverVaccinelist){
		Map<String,List<Vaccine>> listMap = Maps.newHashMap();
		List<Vaccine> vaccineUpdateList = Lists.newArrayList();
		for(int i=0;i<clientVaccineList.size();i++){
			int vaccineAmountTotal = 0;
			vaccineAmountTotal = Integer.parseInt(clientVaccineList.get(i).getValidityAmount());
			for(int j=0;j<serverVaccinelist.size();j++) {
				if (clientVaccineList.get(i).getEntName().equals(serverVaccinelist.get(j).getEntName()) &&
						clientVaccineList.get(i).getVaccineName().equals(serverVaccinelist.get(j).getVaccineName()) &&
						clientVaccineList.get(i).getBatchNo().equals(serverVaccinelist.get(j).getBatchNo()) &&
						clientVaccineList.get(i).getValidityMonth().equals(serverVaccinelist.get(j).getValidityMonth()) &&
						clientVaccineList.get(i).getProduceDate().equals(serverVaccinelist.get(j).getProduceDate()) &&
						clientVaccineList.get(i).getVaccineSpecsCode().equals(serverVaccinelist.get(j).getVaccineSpecsCode())) {
					//同一种疫苗合并数量
					int vaccineAmount2 = Integer.parseInt(serverVaccinelist.get(j).getValidityAmount());
					vaccineAmountTotal += vaccineAmount2;
					serverVaccinelist.get(j).setValidityAmount(String.valueOf(vaccineAmountTotal));
					//更新数据list
					vaccineUpdateList.add(serverVaccinelist.get(j));
					//移除重复的
					clientVaccineList.remove(i);
					break;
				}
			}
		}
		//插入or更新数据
		listMap.put("update",vaccineUpdateList);
		listMap.put("insert",clientVaccineList);
		return listMap;
	}

	/**
	 * @title: dealClientVaccine
	 * @author: chenguangju
	 * @date: 2019/9/24 11:52
	 * @description: 客户端去重
	 * @param: [vaccineList] 
	 * @return: java.lang.String
	 */
	public List<Vaccine> dealClientVaccine(List<Vaccine> vaccineList){
		//判断前台传的数据是否有重复
		List<Vaccine> vaccineListNew = dealVaccineName(vaccineList);
		for(int i=0;i<vaccineListNew.size()-1;i++){
			int vaccineAmountTotal = 0;
			vaccineAmountTotal = Integer.parseInt(vaccineListNew.get(i).getValidityAmount());
			for(int j=vaccineListNew.size()-1;j>i;j--) {
				if (vaccineListNew.get(i).getEntName().equals(vaccineListNew.get(j).getEntName()) &&
						vaccineListNew.get(i).getVaccineName().equals(vaccineListNew.get(j).getVaccineName()) &&
						vaccineListNew.get(i).getBatchNo().equals(vaccineListNew.get(j).getBatchNo()) &&
						vaccineListNew.get(i).getValidityMonth().equals(vaccineListNew.get(j).getValidityMonth()) &&
						vaccineListNew.get(i).getProduceDate().equals(vaccineListNew.get(j).getProduceDate()) &&
						vaccineListNew.get(i).getVaccineSpecsCode().equals(vaccineListNew.get(j).getVaccineSpecsCode())) {
					//同一种疫苗合并数量
					int vaccineAmount2 = Integer.parseInt(vaccineListNew.get(j).getValidityAmount());
					vaccineAmountTotal += vaccineAmount2;
					vaccineListNew.get(i).setValidityAmount(String.valueOf(vaccineAmountTotal));
					//移除重复的
					vaccineListNew.remove(j);
				}
			}
		}
		return vaccineListNew;
	}

	/**
	 * @title: dealVaccineName
	 * @author: chenguangju
	 * @date: 2019/9/24 13:57
	 * @description: 疫苗名称是否重复(单独处理)
	 * @param: [] 
	 * @return: java.util.List<com.thinkgem.jeesite.modules.imm.entity.Vaccine>
	 */
	public List<Vaccine> dealVaccineName(List<Vaccine> vaccineList){
		for(int i=0;i<vaccineList.size();i++){
			String vaccineName = vaccineList.get(i).getVaccineName();
			//用中文逗号和英文逗号分隔开的疫苗名称，视为同种疫苗
			String vaccineName1 = vaccineName.replace(",","，");
			//疫苗名称中包括中文括号和英文括号的，可视为同种疫苗
			String vaccineName2 = vaccineName1.replace("(","（").replace(")","）");
			//疫苗名称中含英文字母，大写字母与小写字母视为同一种疫苗
			String vaccineName3 = vaccineName2.toUpperCase();
			//疫苗名称中带有横杠的情况下，长杠和短杠可视为一种分割符号
			String vaccineName4 = vaccineName3.replace("-","——");
			vaccineName4.replace("-","——");
			vaccineList.get(i).setVaccineName(vaccineName4);
		}
		return vaccineList;
	}
}