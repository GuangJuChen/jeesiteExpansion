package com.thinkgem.jeesite.modules.charts.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @version 1.0
 * @ClassName chartsController
 * @Description 各种图表
 * @Author chenguangju
 * @Date 2019/8/30 14:19
 **/
@Controller
@RequestMapping(value = "${adminPath}/charts/charts")
public class ChartsController {

    /**
     * 图表的例子
     * @author chenguangju
     * @date 2019/8/30 14:37
     * @param
     * @return java.lang.String
     */
    @RequiresPermissions("charts:charts:view")
    @RequestMapping(value = "chartsForm")
    public String chartsForm(){
        return "modules/charts/chartsForm";
    }

    /**
     * 曲线图
     * @author chenguangju
     * @date 2019/8/30 14:37
     * @param
     * @return java.lang.String
     */
    @RequiresPermissions("charts:charts:view")
    @RequestMapping(value = "chartsCurve")
    public String chartsCurve(){
        return "modules/charts/chartsCurve";
    }

    /**
     * 区域图
     * @author chenguangju
     * @date 2019/8/30 14:37
     * @param
     * @return java.lang.String
     */
    @RequiresPermissions("charts:charts:view")
    @RequestMapping(value = "chartsRegion")
    public String chartsRegion(){
        return "modules/charts/chartsRegion";
    }

    /**
     * 条形图
     * @author chenguangju
     * @date 2019/8/30 14:37
     * @param
     * @return java.lang.String
     */
    @RequiresPermissions("charts:charts:view")
    @RequestMapping(value = "chartsStrip")
    public String chartsStrip(){
        return "modules/charts/chartsStrip";
    }

    /**
     * 柱形图
     * @author chenguangju
     * @date 2019/8/30 14:37
     * @param
     * @return java.lang.String
     */
    @RequiresPermissions("charts:charts:view")
    @RequestMapping(value = "chartsCylindrical")
    public String chartsCylindrical(){
        return "modules/charts/chartsCylindrical";
    }

    /**
     * 饼图
     * @author chenguangju
     * @date 2019/8/30 14:37
     * @param
     * @return java.lang.String
     */
    @RequiresPermissions("charts:charts:view")
    @RequestMapping(value = "chartsCake")
    public String chartsCake(){
        return "modules/charts/chartsCake";
    }

    /**
     * 散点图
     * @author chenguangju
     * @date 2019/8/30 14:37
     * @param
     * @return java.lang.String
     */
    @RequiresPermissions("charts:charts:view")
    @RequestMapping(value = "chartsScatter")
    public String chartsScatter(){
        return "modules/charts/chartsScatter";
    }

    /**
     * 气泡图
     * @author chenguangju
     * @date 2019/8/30 14:37
     * @param
     * @return java.lang.String
     */
    @RequiresPermissions("charts:charts:view")
    @RequestMapping(value = "chartsBubble")
    public String chartsBubble(){
        return "modules/charts/chartsBubble";
    }

    /**
     * 动态图
     * @author chenguangju
     * @date 2019/8/30 14:37
     * @param
     * @return java.lang.String
     */
    @RequiresPermissions("charts:charts:view")
    @RequestMapping(value = "chartsDynamic")
    public String chartsDynamic(){
        return "modules/charts/chartsDynamic";
    }

    /**
     * 组合图
     * @author chenguangju
     * @date 2019/8/30 14:37
     * @param
     * @return java.lang.String
     */
    @RequiresPermissions("charts:charts:view")
    @RequestMapping(value = "chartsCombination")
    public String chartsCombination(){
        return "modules/charts/chartsCombination";
    }

    /**
     * 3D图
     * @author chenguangju
     * @date 2019/8/30 14:37
     * @param
     * @return java.lang.String
     */
    @RequiresPermissions("charts:charts:view")
    @RequestMapping(value = "chartsThreeD")
    public String chartsThreeD(){
        return "modules/charts/chartsThreeD";
    }

    /**
     * 测量图
     * @author chenguangju
     * @date 2019/8/30 14:37
     * @param
     * @return java.lang.String
     */
    @RequiresPermissions("charts:charts:view")
    @RequestMapping(value = "chartsMeasuring")
    public String chartsMeasuring(){
        return "modules/charts/chartsMeasuring";
    }

    /**
     * 树状图
     * @author chenguangju
     * @date 2019/8/30 14:37
     * @param
     * @return java.lang.String
     */
    @RequiresPermissions("charts:charts:view")
    @RequestMapping(value = "chartsTree")
    public String chartsTree(){
        return "modules/charts/chartsTree";
    }
}
