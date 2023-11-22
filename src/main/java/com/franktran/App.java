package com.franktran;

import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class App {
    public static void main(String[] args) throws IOException, TemplateException {
        TemplateLoader templateLoader = new TemplateLoader();
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("user", new User("Frank", "frank@gmail.com", 30));
        var result = templateLoader.loadTemplate("classpath:/templates/", "userTemplate.ftl", dataModel);
        System.out.println(new String(result));
    }
}
