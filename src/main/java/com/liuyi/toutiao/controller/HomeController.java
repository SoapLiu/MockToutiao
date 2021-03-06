package com.liuyi.toutiao.controller;

import com.liuyi.toutiao.model.*;
import com.liuyi.toutiao.service.LikeService;
import com.liuyi.toutiao.service.NewsService;
import com.liuyi.toutiao.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    UserService userService;

    @Autowired
    NewsService newsService;

    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/", "home"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model, @RequestParam(value = "pop", defaultValue = "0") int pop) {
        model.addAttribute("voss", getNews(0, 0, 20));
        model.addAttribute("pop", pop);
        return "home";
    }

    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("voss", getNews(userId, 0, 20));
        return "home";
    }

    private List<List<ViewObject>> getNews(int userId, int offset, int limit) {
        List<News> newsList = newsService.getLatestNews(userId, offset, limit);
        List<List<ViewObject>> voss = new ArrayList<>();
        List<ViewObject> newsSameDay = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date createdDate = new Date();
        createdDate.setTime(0L);
        String createdDay = formatter.format(createdDate);
        String initDay = createdDay;
        User currentUser = hostHolder.getUser();

        for(News news : newsList) {
            String newsDay = formatter.format(news.getCreatedDate());
            if(!newsDay.equals(createdDay)) {
                if(!createdDay.equals(initDay)) {
                    voss.add(newsSameDay);
                }
                newsSameDay = new ArrayList<>();
                createdDay = newsDay;
            }
            ViewObject vo = new ViewObject();
            vo.set("news", news);
            if(currentUser != null) {
                vo.set("like", likeService.getLikeStatus(currentUser.getId(), EntityType.NEWS, news.getId()));
            } else {
                vo.set("like", 0);
            }
            vo.set("user", userService.getUser(news.getUserId()));
            newsSameDay.add(vo);
            if(newsList.indexOf(news) == newsList.size()-1) voss.add(newsSameDay);
        }
        return voss;
    }
}
