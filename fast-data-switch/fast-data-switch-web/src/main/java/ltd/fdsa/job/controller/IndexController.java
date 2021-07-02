//package ltd.fdsa.job.controller;
//
//import lombok.var;
//
//import ltd.fdsa.web.view.Result;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.propertyeditors.CustomDateEditor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.WebDataBinder;
//import org.springframework.web.bind.annotation.InitBinder;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Map;
//
///**
// * index controller
// */
//@Controller
//public class IndexController {
//
//    @Autowired
//    JobGroupService jobInfoService;
//
//    @RequestMapping("/")
//    public String index(Model model) {
//
//        return "index";
//    }
//
//
//    @RequestMapping("/test")
//    public String test() {
//
//        var sss = new JobGroup();
//        sss.setAppName("test");
//        sss.setTitle("test");
//        sss.setAddressList("1");
//        sss.setAddressType(1);
//        this.jobInfoService.insert(sss);
//
//        return "help";
//    }
//
//    @RequestMapping("/help")
//    public String help() {
//
//
//        return "help";
//    }
////
////    @InitBinder
////    public void initBinder(WebDataBinder binder) {
////        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////        dateFormat.setLenient(false);
////        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
////    }
//}
