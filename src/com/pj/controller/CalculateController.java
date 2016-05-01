package com.pj.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.pj.service.CalculateService;

@Controller
@RequestMapping("/calculate")
public class CalculateController {
	@Resource
	CalculateService calculate;

	public ModelAndView index(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("");
		
		mv.addObject("data", "");
		return mv;
	}
}
