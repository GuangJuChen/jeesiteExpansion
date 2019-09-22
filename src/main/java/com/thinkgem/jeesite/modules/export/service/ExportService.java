package com.thinkgem.jeesite.modules.export.service;

import com.thinkgem.jeesite.common.utils.ExportUtils;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.service.AreaService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ExportService {

    @Autowired
    private AreaService areaService;

   /* public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<String> headerList = Lists.newArrayList();
        headerList.add("编码");
        headerList.add("类型");


        ExportExcel ee = new ExportExcel("区域数据",headerList);
        List<Area> areaList = areaService.findAll();
        for(int i=0;i<areaList.size();i++){
            Row row = ee.addRow();
            ee.addCell(row,0,areaList.get(i).getCode());
            ee.addCell(row,1,areaList.get(i).getType());
        }
        String filename = "区域数据";
        ee.write(response,filename);
    }*/

   public void exportExcel(HttpServletRequest request,HttpServletResponse response) throws Exception{
       //输入流
       OutputStream out = null;
       //内存中保留1000条数据，以免内存溢出，其余写入硬盘
       SXSSFWorkbook wb = new SXSSFWorkbook(1000);
       //获得该工作区的第一个sheet
       Sheet sheet = wb.createSheet();
       int excelRow = 0;
       //标题行
       Row titleRow = sheet.createRow(excelRow++);
       titleRow.createCell(0).setCellValue("编码");
       titleRow.createCell(1).setCellValue("类型");
       List<Area> areaList = areaService.findAll();
       for(Area area:areaList){
            Row row = sheet.createRow(excelRow++);
            row.createCell(0).setCellValue(area.getCode());
            row.createCell(1).setCellValue(area.getType());
       }
       String fileName ="区域数据";
       String newFileName = ExportUtils.filenameEncoding(fileName,request);
       response.setContentType("application/vnd.ms-excel;charset=UTF-8");
       response.addHeader("Content-Disposition", "attachment;filename=" + newFileName + ".xlsx");
       out = response.getOutputStream(); // 输出流
       wb.write(out);
       out.flush();
       out.close();
   }
}
