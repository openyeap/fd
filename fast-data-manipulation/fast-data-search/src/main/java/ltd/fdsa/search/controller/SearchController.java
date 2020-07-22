package ltd.fdsa.search.controller;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.search.model.MarkdownDoc;
import ltd.fdsa.search.model.entity.ElasticSearchDoc;
import ltd.fdsa.search.model.view.ViewSource;
import ltd.fdsa.search.service.ItemService;
import ltd.fdsa.search.service.RestHighLevelClientService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.Strings;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

@RestController
@Slf4j
public class SearchController {

  @Autowired RestHighLevelClientService service;
  @Autowired ItemService itemService;

  @RequestMapping("/search")
  public Object search(
      @RequestParam(required = false, name = "q") String query_word,
      @RequestParam(required = false) String type,
      @RequestParam(required = false) String source,
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    Collection<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    if (Strings.isNullOrEmpty(query_word)) {
      return list;
    }
    try {
      if (page == null || page <= 0 || page > 100) {
        page = 1;
      }
      if (size == null || size <= 0 || size > 100) {
        size = 10;
      }
      if (Strings.isNullOrEmpty(type)) {
        type = "";
      }
      if (Strings.isNullOrEmpty(source)) {
        source = "";
      }
      SearchResponse search = service.search(query_word, type, source, page, size);
      SearchHits hits = search.getHits();
      for (SearchHit hit : hits.getHits()) {
        Map<String, Object> map = hit.getSourceAsMap();
        map.put("score", hit.getScore());
        list.add(map);
      }
      return list;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return list;
  }

  @RequestMapping("/types")
  public Map<String, Object> getTypes() {
    Map<String, Object> list = new HashMap<>();
    try {
      SearchResponse search = service.getMetaData("type", "resources");
      Aggregations aggregations = search.getAggregations();
      Terms aggregation = aggregations.get("type");
      for (Terms.Bucket bucket : aggregation.getBuckets()) {
        list.put(bucket.getKeyAsString(), bucket.getDocCount());
      }
      return list;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return list;
  }

  @RequestMapping("/sources")
  public Map<String, Object> getSources() {
    Map<String, Object> list = new HashMap<>();
    try {
      SearchResponse search = service.getMetaData("source", "resources");
      Aggregations aggregations = search.getAggregations();
      Terms aggregation = aggregations.get("source");
      for (Terms.Bucket bucket : aggregation.getBuckets()) {
        list.put(bucket.getKeyAsString(), bucket.getDocCount());
      }
      return list;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return list;
  }

  @RequestMapping("/test")
  public Object test(@RequestParam(required = false) String path) {
    if (Strings.isNullOrEmpty(path)) {
      path = ".";
    }
    ArrayList<MarkdownDoc> list = new ArrayList<MarkdownDoc>();

    for (String file : this.itemService.getFiles(path, ".md")) {
      log.info(file);
      MarkdownDoc item = this.itemService.readYaml(file);
      if (item != null) {
        list.add(item);
      }
    }

    ElasticSearchDoc[] result = new ElasticSearchDoc[list.size()];
    for (int i = 0; i < result.length; i++) {
      MarkdownDoc doc = list.get(i);
      result[i] =
          new ElasticSearchDoc(
              doc.toUrl(),
              doc.getType(),
              doc.getTitle(),
              doc.getSummary(),
              doc.getContent(),
              doc.toUrl(),
              doc.getDatetime(),
              doc.getSource());
    }
    this.service.importAll("resources", result);
    return result;
  }

  @RequestMapping("/import")
  public boolean importAll(@RequestBody() ViewSource[] sources) {
    ElasticSearchDoc[] list = new ElasticSearchDoc[sources.length];
    for (int i = 0; i < sources.length; i++) {
      ViewSource source = sources[i];
      list[i] =
          new ElasticSearchDoc(
              source.getId(),
              source.getType(),
              source.getTitle(),
              source.getSummary(),
              source.getContent(),
              source.getUrl(),
              new Date(),
              source.getSource());
    }
    return this.service.importAll("resources", list);
  }
}
