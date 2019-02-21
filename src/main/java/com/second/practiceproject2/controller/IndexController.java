package com.second.practiceproject2.controller;

import com.second.practiceproject2.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class IndexController {
    @RequestMapping(path = {"/","/index"})
    @ResponseBody
    public String index()
    {
        return "first page";
    }

    @RequestMapping(path = {"/profile/{userId}"},method = {RequestMethod.GET })
    @ResponseBody
    public String profile(@PathVariable("userId") int userId,
                          @RequestParam(value = "page",defaultValue = "1",required = true)int page)
    {
        return String.format("Profile Page of %d, p:%d",userId,page);
    }


    @RequestMapping(path = {"/thy"},method = {RequestMethod.GET })
    public String template(Model model)
    {
        model.addAttribute("name", "hello world");
        List<String> colors = Arrays.asList(new String[]{"RED","GREEN","BLUE"});
        model.addAttribute("colors",colors);

        Map<String,String> map = new HashMap<>();
        for(int i = 0; i< 4; ++i)
        {
            map.put(String.valueOf(i),String.valueOf(i*i));
        }
        model.addAttribute("map",map);
        model.addAttribute("user",new User("TIAN"));
        return "home";
    }
}
