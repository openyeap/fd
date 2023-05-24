package cn.zhumingwu.search.service;

import cn.zhumingwu.search.model.MarkdownDoc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class ItemService {

    static Yaml yaml = new Yaml();
    @Autowired
    ObjectMapper mapper;

    public Collection<String> getFiles(String filePath, String extension) {

        Collection<String> list = new ArrayList<String>();

        File root = new File(filePath);
        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                list.addAll(getFiles(file.getAbsolutePath(), extension));
                continue;
            }
            if (file.getName().endsWith(extension)) {
                list.add(file.getAbsolutePath());
            }
        }
        return list;
    }

    public MarkdownDoc readYaml(String pathname) {
        StringBuilder headerStringBuilder = new StringBuilder();
        StringBuilder bodyStringBuilder = new StringBuilder();

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(pathname), "UTF-8");
             BufferedReader bufferedReader = new BufferedReader(reader);) {

            String line;
            int hasYaml = 0;
            while ((line = bufferedReader.readLine()) != null) {
                if ("---".equals(line.trim())) {
                    hasYaml++;
                    if (hasYaml < 2) {
                        continue;
                    }
                }
                if (hasYaml == 1) {
                    headerStringBuilder.append(line + "\n");
                } else {
                    bodyStringBuilder.append(line + "\n");
                }
            }
            String input = headerStringBuilder.toString();
            String body = bodyStringBuilder.toString();
            if (Strings.isNullOrEmpty(input) || Strings.isNullOrEmpty(body)) {
                return null;
            }
            MarkdownDoc item = yaml.loadAs(input, MarkdownDoc.class);
            item.setContent(body);
            return item;
        } catch (IOException e) {
            log.error(pathname);
            return null;
        }
    }

    public String loadJson() {

        InputStream in = null;
        InputStreamReader inr = null;
        BufferedReader buf = null;

        try {
            in = this.getClass().getClassLoader().getResourceAsStream("items.json");
            inr = new InputStreamReader(in, "utf-8");
            buf = new BufferedReader(inr);

            int ch;
            StringBuilder sb = new StringBuilder();
            while ((ch = buf.read()) != -1) {
                sb.append((char) ch);
            }
            return sb.toString();
        } catch (IOException e) {
            log.error("Exception", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (inr != null) {
                    inr.close();
                }
                if (buf != null) {
                    buf.close();
                }
            } catch (IOException e) {
                log.error("Exception", e);
            }
        }

        return null;
    }

    public String getItemsJson() {
        String res = "";
        String items = loadJson();
        List<MarkdownDoc> itemList = new ArrayList<>();

        try {
            JsonNode jsonNode = mapper.readTree(items);
            if (jsonNode.isArray()) {
                for (JsonNode node : jsonNode) {
                    MarkdownDoc item = new MarkdownDoc();
                    item.setContent(node.get("content").asText());
                    item.setTitle(node.get("title").asText());
                    itemList.add(item);
                }
            }
            res = mapper.writeValueAsString(itemList);
        } catch (JsonProcessingException e) {
            log.error("Exception", e);
        }

        return res;
    }
}
