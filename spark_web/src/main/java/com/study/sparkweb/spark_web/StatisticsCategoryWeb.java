package com.study.sparkweb.spark_web;

import com.study.dao.CategoryClickCountDAO;
import com.study.domain.CategoryClickCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class StatisticsCategoryWeb {

    private static Map<String,String> courses = new HashMap<>();
    static {
        courses.put("1","偶像爱情");
        courses.put("2","宫斗权谋");
        courses.put("3","玄幻史诗");
        courses.put("4","都市生活");
        courses.put("6","历险科幻");
    }

    @Autowired
    CategoryClickCountDAO categoryClickCountDAO;

    @RequestMapping(value = "/categoryClickCount",method = RequestMethod.POST)
    @ResponseBody
    public List<CategoryClickCount> categoryClickCounts() throws IOException {
        List<CategoryClickCount> list = categoryClickCountDAO.query("20210120");
        for(CategoryClickCount model:list){
            String name = courses.get(model.getName().substring(9));
            if (name != null){
                model.setName(name);
            } else {
                model.setName("其他");
            }

        }
        return list;
    }

    @RequestMapping(value = "category",method = RequestMethod.GET)
    public ModelAndView firstDemo(){
        return new ModelAndView("categoryechart");
    }
}
