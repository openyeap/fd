package cn.zhumingwu.dataswitch.admin.controller;

import cn.zhumingwu.dataswitch.admin.repository.JobGroupRepository;
import cn.zhumingwu.dataswitch.admin.annotation.PermissionLimit;
import cn.zhumingwu.dataswitch.admin.entity.JobGroup;
import cn.zhumingwu.dataswitch.admin.entity.User;
import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.core.util.I18nUtil;

import cn.zhumingwu.dataswitch.admin.service.impl.SystemUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private SystemUserService systemUserService;
    @Resource
    private JobGroupRepository jobGroupRepository;

    @RequestMapping
    @PermissionLimit(adminuser = true)
    public String index(Model model) {

        // 执行器列表
        List<JobGroup> groupList = jobGroupRepository.findAll();
        model.addAttribute("groupList", groupList);

        return "user/user.index";
    }

    @RequestMapping("/pageList")
    @ResponseBody
    @PermissionLimit(adminuser = true)
    public Map<String, Object> pageList(
            @RequestParam(required = false, defaultValue = "0") int start,
            @RequestParam(required = false, defaultValue = "10") int length,
            String username,
            int role) {

        // page list
//        List<JobUser> list = JobUserDao.pageList(start, length, username, role);
//        int list_count = JobUserDao.pageListCount(start, length, username, role);

        // package result
        Map<String, Object> maps = new HashMap<String, Object>();
//        maps.put("recordsTotal", list_count); // 总记录数
//        maps.put("recordsFiltered", list_count); // 过滤后的总记录数
//        maps.put("data", list); // 分页列表
        return maps;
    }

    @RequestMapping("/add")
    @ResponseBody
    @PermissionLimit(adminuser = true)
    public Result<String> add(User systemUser) {

        // valid username
        if (!StringUtils.hasText(systemUser.getName())) {
            return Result.fail(500,
                    I18nUtil.getInstance("").getString("system_please_input") + I18nUtil.getInstance("").getString("user_username"));
        }
        systemUser.setName(systemUser.getName().trim());
        if (!(systemUser.getName().length() >= 4 && systemUser.getName().length() <= 20)) {
            return Result.fail(500, I18nUtil.getInstance("").getString("system_lengh_limit") + "[4-20]");
        }
        // valid password
        if (!StringUtils.hasText(systemUser.getPassword())) {
            return Result.fail(500,
                    I18nUtil.getInstance("").getString("system_please_input") + I18nUtil.getInstance("").getString("user_password"));
        }
        systemUser.setPassword(systemUser.getPassword().trim());
        if (!(systemUser.getPassword().length() >= 4 && systemUser.getPassword().length() <= 20)) {
            return Result.fail(500, I18nUtil.getInstance("").getString("system_lengh_limit") + "[4-20]");
        }
        // md5 password
        systemUser.setPassword(DigestUtils.md5DigestAsHex(systemUser.getPassword().getBytes()));

        // check repeat
        User existUser = systemUserService.loadByUserName(systemUser.getName());
        if (existUser != null) {
            return Result.fail(500, I18nUtil.getInstance("").getString("user_username_repeat"));
        }

        // write
        systemUserService.update(systemUser);
        return Result.success();
    }

    @RequestMapping("/update")
    @ResponseBody
    @PermissionLimit(adminuser = true)
    public Result<String> update(  User systemUser) {

        // avoid opt login seft
        User loginUser = this.systemUserService.checkLogin();
        if (loginUser.getName().equals(systemUser.getName())) {
            return Result.fail(500, I18nUtil.getInstance("").getString("user_update_loginuser_limit"));
        }

        // valid password
        if (StringUtils.hasText(systemUser.getPassword())) {
            systemUser.setPassword(systemUser.getPassword().trim());
            if (!(systemUser.getPassword().length() >= 4 && systemUser.getPassword().length() <= 20)) {
                return Result.fail(500, I18nUtil.getInstance("").getString("system_lengh_limit") + "[4-20]");
            }
            // md5 password
            systemUser.setPassword(DigestUtils.md5DigestAsHex(systemUser.getPassword().getBytes()));
        } else {
            systemUser.setPassword(null);
        }

        // write
        systemUserService.update(systemUser);
        return Result.success();
    }

    @RequestMapping("/remove")
    @ResponseBody
    @PermissionLimit(adminuser = true)
    public Result<String> remove( int id) {

        // avoid opt login seft
        User loginUser =  this.systemUserService.checkLogin();

        if (loginUser.getId() == id) {
            return Result.fail(500, I18nUtil.getInstance("").getString("user_update_loginuser_limit"));
        }

        systemUserService.deleteById(id);
        return Result.success();
    }

    @RequestMapping("/updatePwd")
    @ResponseBody
    public Result<String> updatePwd(  String password) {

        // valid password
        if (password == null || password.trim().length() == 0) {
            return Result.fail(500, "密码不可为空");
        }
        password = password.trim();
        if (!(password.length() >= 4 && password.length() <= 20)) {
            return Result.fail(500, I18nUtil.getInstance("").getString("system_lengh_limit") + "[4-20]");
        }

        // md5 password
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());

        // update pwd
        User loginUser =this.systemUserService.checkLogin();

        // do write
        User existUser = systemUserService.loadByUserName(loginUser.getName());
        existUser.setPassword(md5Password);
        systemUserService.update(existUser);

        return Result.success();
    }
}
