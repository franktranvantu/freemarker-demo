package com.franktran;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.MessageFormat;
import java.util.Map;

public class TemplateLoader {
    private static final String CLASSPATH_PREFIX = "classpath:";
    private static final String FILE_PREFIX = "file:";

    public byte[] loadTemplate(String path, String templateName, Map<String, Object> dataModel) {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        try {
            if (path.startsWith(CLASSPATH_PREFIX)) {
                var basePackagePath = getClassPathPath(path);
                var inputStream = getClass().getResourceAsStream(basePackagePath);
                if (inputStream == null) {
                    throw new IllegalArgumentException(MessageFormat.format("The base package path {0} was not found.", path));
                }
                cfg.setClassForTemplateLoading(getClass(), basePackagePath);
            } else if (path.startsWith(FILE_PREFIX)) {
                var file = new File(path.replace(FILE_PREFIX, ""));
                cfg.setDirectoryForTemplateLoading(file);
            }
            var template = cfg.getTemplate(templateName);
            var outputStream = new ByteArrayOutputStream();
            var writer = new OutputStreamWriter(outputStream);
            template.process(dataModel, writer);
            return outputStream.toByteArray();
        } catch (TemplateException | IOException e) {
            throw new IllegalArgumentException(MessageFormat.format("Could not process template {0}", path + templateName));
        }
    }

    private String getClassPathPath(String path) {
        var replacedPath = path.replace(CLASSPATH_PREFIX, "");
        if (replacedPath.startsWith("/")) {
            return replacedPath;
        }
        return "/" + replacedPath;
    }

}
