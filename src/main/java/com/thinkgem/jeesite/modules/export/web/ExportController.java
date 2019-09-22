package com.thinkgem.jeesite.modules.export.web;

import com.thinkgem.jeesite.modules.export.service.ExportService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "${adminPath}/export/export")
public class ExportController{

    @Autowired
    private ExportService exportService;

    @RequiresPermissions("export:export:view")
    @RequestMapping(value = "export")
    public String export() {
        return "modules/export/export";
    }

    @RequiresPermissions("export:export:view")
    @RequestMapping(value = "exportExcel")
    @ResponseBody
    public String exportExcel(HttpServletRequest request,HttpServletResponse response) throws Exception {
        exportService.exportExcel(request,response);
        return "";
    }
}
