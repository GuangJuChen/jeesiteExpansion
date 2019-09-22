/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.io.*;
import java.util.*;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.thinkgem.jeesite.common.service.TreeService;
import com.thinkgem.jeesite.modules.sys.dao.AreaDao;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.commons.httpclient.*;

/**
 * 区域Service
 * @author ThinkGem
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class AreaService extends TreeService<AreaDao, Area> {


	/**
	 * 高德地图请求秘钥
	 */
	private static final String KEY = "9e2c4452f891a3af9f750cab6904ea0c";
	/**
	 * 返回值类型
	 */
	private static final String OUTPUT = "JSON";
	/**
	 * 根据地名获取高德经纬度Api
	 */
	private static final String GET_LNG_LAT_URL = "http://restapi.amap.com/v3/geocode/geo?";


	public List<Area> findAll(){
		return UserUtils.getAreaList();
	}

	@Transactional(readOnly = false)
	@Override
	public void save(Area area) {
		super.save(area);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
	}

	@Transactional(readOnly = false)
	@Override
	public void delete(Area area) {
		super.delete(area);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
	}

	/**
	 * 获取所有地址并解析
	 * @author chenguangju
	 * @date 2019/9/6 19:11
	 * @param
	 * @return java.util.List<java.lang.String>
	 */
	@Transactional(readOnly = false)
	public List<String> getAddress() throws Exception{
		//从数据库中查询所有的地址
		List<String> addressList = dao.getAddressList();
		//去掉所有数据中的空格
		List<String> newAddressList = new ArrayList<String>();
		for(String address:addressList){
			String newAddress = address.replace(" ","");
			newAddressList.add(newAddress);
		}
		return  newAddressList;
	}

	/**
	 * 把所有的地址转为经纬度
	 * @author chenguangju
	 * @date 2019/9/9 11:51
	 * @param
	 * @return java.util.List<java.lang.String>
	 */
	public  List<String> getLngAndLat() throws Exception{
		List<String> lngAndLatList = new ArrayList<String>();
		List<String> requestAddressList = getAddress();
		for(String address:requestAddressList){
			String lngAndLat = getLatAndLogByName(KEY,address);
			lngAndLatList.add(lngAndLat);
		}
		return lngAndLatList;
	}

	/*
	 * 把所有经纬度插入数据库
	 * @author chenguangju
	 * @date 2019/9/9
	 * @param `
	 * @return
	 */
	public String insertLngAndLat() throws Exception{
		List<String> lngAndLatList = getLngAndLat();

		for(String lngAndLat :lngAndLatList){
			String lng = lngAndLat.split(",")[0];
			String lat = lngAndLat.split(",")[1];
		}
	return "";
	}

	/**
	 * 根据地址获取经纬度
	 * @author chenguangju
	 * @date 2019/9/9 11:49
	 * @param
	 * @return java.lang.String
	 */
	public static String getLatAndLogByName(String key,String name) throws Exception{
		StringBuffer s = new StringBuffer();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("key",KEY));
		params.add(new BasicNameValuePair("&address",name));
		params.add(new BasicNameValuePair("&output",OUTPUT));
		s.append("key=" + key + "&address=" + name+"&output=" + OUTPUT + "&batch=" + true);
		String res = sendPost(s.toString(),GET_LNG_LAT_URL,"thinkgem"+"admin");
		return res;
	}

	/*
	 * 发送post请求
	 * @author chenguangju
	 * @date 2019/9/9
	 * @param
	 * @return
	 */
	public static String sendPost(String params, String requestUrl,
								  String authorization) throws IOException {
		// 将参数转为二进制流
		byte[] requestBytes = params.getBytes("utf-8");
		// 客户端实例化
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(requestUrl);
		//设置请求头Authorization
		postMethod.setRequestHeader("Authorization", "Basic " + authorization);
		// 设置请求头 Content-Type
		postMethod.setRequestHeader("Content-Type", "application/json");
		InputStream inputStream = new ByteArrayInputStream(requestBytes, 0,
				requestBytes.length);
		RequestEntity requestEntity = new InputStreamRequestEntity(inputStream, requestBytes.length, "application/json; charset=utf-8"); // 请求体
		postMethod.setRequestEntity(requestEntity);
		// 执行请求
		httpClient.executeMethod(postMethod);
		// 获取返回的流
		InputStream soapResponseStream = postMethod.getResponseBodyAsStream();
		byte[] datas = null;
		try {
			datas = readInputStream(soapResponseStream);// 从输入流中读取数据
		} catch (Exception e) {
			e.printStackTrace();
		}
		String result = new String(datas, "UTF-8");// 将二进制流转为String
		String jsonStr = result.toString();
		JSONObject jsonObject = JSONObject.parseObject(jsonStr);
		//判断输入的位置点是否存在
		if(jsonObject.getJSONArray("geocodes").size() > 0){
			return jsonObject.getJSONArray("geocodes").getJSONObject(0).get("location").toString();
		} else{
			return null;
		}
	}

	/*
	 * 读取数据
	 * @author chenguangju
	 * @date 2019/9/9
	 * @param
	 * @return
	 */
	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;
	}
}
