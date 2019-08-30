package com.thinkgem.jeesite.modules.map.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @version 1.0
 * @ClassName MapController
 * @Description 百度地图
 * @Author chenguangju
 * @Date 2019/8/30 16:59
 **/
@Controller
@RequestMapping(value = "${adminPath}/map/map")
public class MapController {

    /**
     * 默认的百度地图
     * @author chenguangju
     * @date 2019/8/30 18:26
     * @param
     * @return java.lang.String
     */
    @RequiresPermissions("map:map:view")
    @RequestMapping(value = "mapTest")
    public String mapTest(){
        return "modules/map/mapTest";
    }

    /**
     * 百度地图的自定义控件
     * @author chenguangju
     * @date 2019/8/30 18:27
     * @param
     * @return java.lang.String
     */
    @RequiresPermissions("map:map:view")
    @RequestMapping(value = "mapCustomize")
    public String mapCustomize(){
        return "modules/map/mapCustomize";
    }
}
