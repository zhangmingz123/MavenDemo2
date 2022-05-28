package com.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.po.Dep;
import com.po.Emp;
import com.po.PageBean;
import com.po.Welfare;
import com.util.AJAxUtil;
import com.util.BizServiceUtil;

@Controller
public class EmpController {
  @Resource(name="BizServiceUtil")
  private BizServiceUtil bizService;

public BizServiceUtil getBizService() {
	return bizService;
}

public void setBizService(BizServiceUtil bizService) {
	this.bizService = bizService;
}
@RequestMapping(value="save_Emp.do")
public String save(HttpServletRequest request,HttpServletResponse response,Emp emp){
	System.out.println("添加方法:"+emp.toString());
	String realpath=request.getRealPath("/");
	/*************文件上传begin*****************/
	//获取上传照片对象
	MultipartFile multipartFile=emp.getPic();
	if(multipartFile!=null && !multipartFile.isEmpty()){
		//获取上传文件名称
		String fname=multipartFile.getOriginalFilename();
		//更名
		if(fname.lastIndexOf(".")!=-1){
			//获取后缀
			String ext=fname.substring(fname.lastIndexOf("."));
			//限制上传格式
			if(ext.equalsIgnoreCase(".jpg")){
				String newfname=new Date().getTime()+ext;
				//创建文件对象，将上传的字节文件写入到指定文件
				File dostFile=new File(realpath+"/uppic/"+newfname);
				//上传
				try {
					FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), dostFile);
					emp.setPhoto(newfname);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	/*************文件上传end*****************/
	boolean flag=bizService.getEmpService().save(emp);
	if(flag){
		AJAxUtil.printString(response, 1+"");
	}else{
		AJAxUtil.printString(response, 0+"");
	}	
	return null;
}
@RequestMapping(value="update_Emp.do")
public String update(HttpServletRequest request,HttpServletResponse response,Emp emp){
	System.out.println("修改方法:"+emp.toString());
	String realpath=request.getRealPath("/");
	//获取原有照片
	String oldphoto=bizService.getEmpService().findById(emp.getEid()).getPhoto();
	if(emp.getPic()!=null){
		/*************文件上传begin*****************/
		//获取上传照片对象
		MultipartFile multipartFile=emp.getPic();
		if(multipartFile!=null && !multipartFile.isEmpty()){
			//获取上传文件名称
			String fname=multipartFile.getOriginalFilename();
			//更名
			if(fname.lastIndexOf(".")!=-1){
				//获取后缀
				String ext=fname.substring(fname.lastIndexOf("."));
				//限制上传格式
				if(ext.equalsIgnoreCase(".jpg")){
					String newfname=new Date().getTime()+ext;
					//创建文件对象，将上传的字节文件写入到指定文件
					File dostFile=new File(realpath+"/uppic/"+newfname);
					//上传
					try {
						FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), dostFile);
						emp.setPhoto(newfname);
						//删除原有
						File oldfile=new File(realpath+"/uppic/"+oldphoto);
						if(oldfile.exists()&& !oldphoto.equalsIgnoreCase("default.jpg")){
							oldfile.delete();//删除
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		/*************文件上传end*****************/
	}else{//修改时不上传
		emp.setPhoto(oldphoto);
	}
	
	boolean flag=bizService.getEmpService().update(emp);
	if(flag){
		AJAxUtil.printString(response, 1+"");
	}else{
		AJAxUtil.printString(response, 0+"");
	}	
	return null;
}
@RequestMapping(value="delById_Emp.do")
public String delById(HttpServletRequest request,HttpServletResponse response,Integer eid){
	System.out.println("删除："+eid);
	String realpath=request.getRealPath("/");
	//获取原有照片
	String oldphoto=bizService.getEmpService().findById(eid).getPhoto();
	System.out.println("aaa:"+oldphoto);
	boolean flag=bizService.getEmpService().delById(eid);
	if(flag){
		//删除原有
		File oldfile=new File(realpath+"/uppic/"+oldphoto);
		if(oldfile.exists()&& !oldphoto.equalsIgnoreCase("default.jpg")){
			oldfile.delete();//删除
		}
		AJAxUtil.printString(response, 1+"");
	}else{
		AJAxUtil.printString(response, 0+"");
	}	
	return null;
}
@RequestMapping(value="findById_Emp.do")
public String findById(HttpServletRequest request,HttpServletResponse response,Integer eid){
	System.out.println("查询单个："+eid);
	Emp oldemp=bizService.getEmpService().findById(eid);
	System.out.println("oldemp:"+oldemp.toString());
	//集合转json	
	PropertyFilter propertyFilter=AJAxUtil.filterProperts("birthday","pic");
	String jsonstr=JSONObject.toJSONString(oldemp,propertyFilter, SerializerFeature.DisableCircularReferenceDetect);
    AJAxUtil.printString(response, jsonstr);
	return null;
}
@RequestMapping(value="findPageAll_Emp.do")
public String findPageAll(HttpServletRequest request,HttpServletResponse response,Integer page,Integer rows){
	System.out.println("分页查询方法:"+page+"===="+rows);
	Map<String, Object> map=new HashMap<String,Object>();
	PageBean pb=new PageBean();
	page=page==null||page<1?pb.getPage():page;
	rows=rows==null||rows<1?pb.getRows():rows;
	if(rows>10)rows=10;
	pb.setPage(page);
	pb.setRows(rows);
	List<Emp> lsemp=bizService.getEmpService().findPageAll(pb);
	int maxRows=bizService.getEmpService().findMaxRows();
	map.put("page", page);
	map.put("rows", lsemp);
	map.put("total", maxRows);
    //集合转json	
	PropertyFilter propertyFilter=AJAxUtil.filterProperts("birthday","pic");
	String jsonstr=JSONObject.toJSONString(map,propertyFilter, SerializerFeature.DisableCircularReferenceDetect);
    AJAxUtil.printString(response, jsonstr);
	return null;
}
@RequestMapping(value="doint_Emp.do")
public String doint(HttpServletRequest request,HttpServletResponse response){
	System.out.println("添加页面准备数据");
	Map<String, Object> map=new HashMap<String,Object>();
	List<Dep> lsdep=bizService.getDepService().findAll();
	List<Welfare> lswf=bizService.getWelfareService().findAll();
    map.put("lsdep",lsdep);
    map.put("lswf", lswf);
	//集合转json	
	PropertyFilter propertyFilter=AJAxUtil.filterProperts("birthday","pic");
	String jsonstr=JSONObject.toJSONString(map,propertyFilter, SerializerFeature.DisableCircularReferenceDetect);
    AJAxUtil.printString(response, jsonstr);
	return null;
}
}
