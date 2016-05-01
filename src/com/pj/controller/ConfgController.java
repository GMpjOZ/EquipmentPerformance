package com.pj.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pj.service.ConfgService;

@Controller  
 
public class ConfgController {
	
	@Resource  
	private ConfgService confgService;
	
//	@RequestMapping(value="")
	@RequestMapping(value="/index") 
	public String index(HttpServletRequest request){
		boolean flag=confgService.isConfg();
		if(flag==true){
		System.out.println(flag);
		 return "/Index";  
		}
		else {
			System.out.println(flag);
			return "/Login";
		}
		
	}
}
