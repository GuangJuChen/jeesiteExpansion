/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.imm.web;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.ResponseResult;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.bas.service.EntVaccineService;
import com.thinkgem.jeesite.modules.imm.entity.Vaccine;
import com.thinkgem.jeesite.modules.imm.service.VaccineService;

/**
 * 疫苗登记、免疫记录Controller
 * 
 * @author chenguangju
 * @version 2019-09-11
 */
@Controller
@RequestMapping(value = "${adminPath}/imm/vaccine")
public class VaccineController extends BaseController {

	@Autowired
	private VaccineService vaccineService;

	@Autowired
	private EntVaccineService entVaccineService;

	@ModelAttribute
	public Vaccine get(@RequestParam(required = false) String id) {
		Vaccine entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = vaccineService.get(id);
		}
		if (entity == null) {
			entity = new Vaccine();
		}
		return entity;
	}

	/**
	 * @title：list
	 * @author：chenguangju
	 * @date：2019/9/20 8:36
	 * @description：form
	 * @param：[vaccine, request, response, model] 
	 * @return：java.lang.String
	 */
	@RequiresPermissions("imm:vaccine:view")
	@RequestMapping(value = { "form", "" })
	public String form(Vaccine vaccine,Model model) {
		model.addAttribute("vaccineSpecsCodeList", DictUtils.getDictList("vaccine_specs_code"));
		model.addAttribute("entVaccineNameList",entVaccineService.findEntNameList());
		return "modules/imm/vaccineForm";
	}

	/**
	 * @title: modifyForm
	 * @author: chenguangju
	 * @date: 2019/9/25 9:53
	 * @description: 修改表单
	 * @param: [vaccine, model] 
	 * @return: java.lang.String
	 */
	@RequiresPermissions("imm:vaccine:view")
	@RequestMapping(value = "form/update")
	public String modifyForm(Vaccine vaccine,Model model) {
		model.addAttribute("vaccine",vaccine);
		return "modules/imm/vaccineModifyForm";
	}

	/**
	 * @title：form
	 * @author：chenguangju
	 * @date：2019/9/20 8:36
	 * @description：list
	 * @param：[vaccine, model] 
	 * @return：java.lang.String
	 */
	@RequiresPermissions("imm:vaccine:view")
	@RequestMapping(value = { "list","" })
	public String list(Vaccine vaccine, Model model,HttpServletRequest request,HttpServletResponse response) {
		Page<Vaccine> page = vaccineService.findPage(new Page<Vaccine>(request, response), vaccine);
		model.addAttribute("vaccine", vaccine);
		model.addAttribute("page", page);
		return "modules/imm/vaccineList";
	}

	/**
	 * @title: vaccineSave
	 * @author: chenguangju
	 * @date: 2019/9/24 9:51
	 * @description: 保存
	 * @param: [vaccineArray]
	 * @return: java.lang.String
	 */
	@ResponseBody
	@RequiresPermissions("imm:vaccine:edit")
	@RequestMapping(value = "save")
	public ResponseResult save(@RequestBody String vaccineArray){
		JSONObject jsonObject = JSON.parseObject(vaccineArray);
		JSONArray jsonArray = (JSONArray)jsonObject.get("vaccineArray");
		List<Vaccine> vaccineList = JSONArray.parseArray(jsonArray.toString(), Vaccine.class);
		vaccineService.vaccineSave(vaccineList);
		return ResponseResult.success();
	}

	/**
	 * @title：delete
	 * @author：chenguangju
	 * @date：2019/9/20 8:36
	 * @description：删除
	 * @param：[vaccine, redirectAttributes] 
	 * @return：java.lang.String
	 */
	@ResponseBody
	@RequiresPermissions("imm:vaccine:edit")
	@RequestMapping(value = "delete")
	public ResponseResult delete(Vaccine vaccine, RedirectAttributes redirectAttributes) {
		vaccineService.delete(vaccine);
		return ResponseResult.success();
	}

	/**
	 * @title: update
	 * @author: chenguangju
	 * @date: 2019/9/25 11:31
	 * @description: 编辑
	 * @param: [vaccine] 
	 * @return: java.lang.String
	 */
	@ResponseBody
	@RequiresPermissions("imm:vaccine:edit")
	@RequestMapping(value = "update")
	public ResponseResult update(Vaccine vaccine){
		this.vaccineService.save(vaccine);
		return ResponseResult.success();
	}

	/**
	 * 
	 * @title: entDataByIds
	 * @author: my
	 * @date: 2019年9月19日 下午3:17:49
	 * @description: 通过主键获取企业信息
	 * @param: vaccine
	 * @return: ResponseResult
	 */
	@ResponseBody
	@RequestMapping(value = "data/ent/by/ids")
	public ResponseResult entDataByIds(Vaccine vaccine) {
		List<Vaccine> list = vaccineService.findEntByIds(vaccine);
		return ResponseResult.success(list);
	}

	/**
	 * @title：findEntNo
	 * @author：chenguangju
	 * @date：2019/9/20 8:39
	 * @description：根据企业名称获取企业代码
	 * @param：[]
	 * @return：java.lang.String
	 */
	@ResponseBody
	@RequiresPermissions("imm:vaccine:view")
	@RequestMapping(value = "findEntNo")
	public String findEntNo(Vaccine vaccine){
		return entVaccineService.findEntNo(vaccine.getEntName());
	}

}