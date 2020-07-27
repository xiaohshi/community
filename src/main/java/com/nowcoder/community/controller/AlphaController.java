package com.nowcoder.community.controller;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AlphaController {

    @RequestMapping("/xiaohshi")
    public void xiaohshi(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //get request data
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ":" + value);
        }
        System.out.println(request.getParameter("code"));

        //return response data
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write("<h1>xiaohshi</h1>");
    }

    //GET request

    //获取浏览器中的参数，/get/sxh?limit=10
    @RequestMapping(path = "/get/sxh", method = RequestMethod.GET)
    @ResponseBody
    public String getData(
            @RequestParam(name = "limit", required = false, defaultValue = "1") int limit) {
        System.out.println(limit);
        return "xiaohshi";
    }

    // /student/123
    @RequestMapping(path = "/student/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getData(@PathVariable("id") String id) {
        System.out.println(id);
        return "xiaohshi";
    }

    //post请求
    @RequestMapping(path = "/student", method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name, int age) {
        System.out.println(name + "---" + age);
        return "success";
    }

    //响应html数据
    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    public String getTeacher(Model model) {
        return "/view.html";
    }

    //响应json数据
    //Java对象 -> Json字符串 -> JS对象
    @RequestMapping(path = "/sxh", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getSxh() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "xiaohshi");
        map.put("age", 26);
        return map;
    }
}