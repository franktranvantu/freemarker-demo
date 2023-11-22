package com.franktran;

import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

public class TemplateLoaderTest {
    private TemplateLoader underTest = new TemplateLoader();

    @Test
    public void givenClasspath_whenLoadTemplate_thenReturnTemplateContent() throws IOException, TemplateException {
        var inputStream = getClass().getResourceAsStream("/expected.html");
        var result = IOUtils.toByteArray(inputStream);
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("user", new User("Frank", "frank@gmail.com", 31));

        var actual = underTest.loadTemplate("classpath:/templates/", "userTemplate.ftl", dataModel);

        assertThat(actual).isEqualTo(result);
    }

    @Test
    public void givenFile_whenLoadTemplate_thenReturnTemplateContent() throws IOException {
        var inputStream = getClass().getResourceAsStream("/expected.html");
        var result = IOUtils.toByteArray(inputStream);
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("user", new User("Frank", "frank@gmail.com", 31));

        var currentDirectory = System.getProperty("user.dir");
        String path = String.format("file:%s/templates/", currentDirectory);
        var actual = underTest.loadTemplate(path, "userTemplate.ftl", dataModel);

        assertThat(actual).isEqualTo(result);
    }

    @Test
    public void givenInvalidBasePackageClassPath_whenLoadTemplate_thenThrowException() {
        var dataModel = mock(Map.class);

        assertThatThrownBy(() -> underTest.loadTemplate("classpath:/invalidTemplates/", "userTemplate.ftl", dataModel))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The base package path classpath:/invalidTemplates/ was not found.");
    }

    @Test
    public void givenInvalidTemplateName_whenLoadTemplate_thenThrowException() {
        var dataModel = mock(Map.class);

        assertThatThrownBy(() -> underTest.loadTemplate("classpath:/templates/", "invalidTemplate.ftl", dataModel))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Could not process template classpath:/templates/invalidTemplate.ftl");
    }
}
