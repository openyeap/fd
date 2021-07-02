package ltd.fdsa.devtools.generate;

import ltd.fdsa.component.constant.AdminConst;
import ltd.fdsa.component.utils.ResultVoUtil;
import ltd.fdsa.component.utils.ToolUtil;
import ltd.fdsa.component.vo.ResultVo;
import ltd.fdsa.devtools.generate.domain.Basic;
import ltd.fdsa.devtools.generate.domain.Generate;
import ltd.fdsa.devtools.generate.domain.Template;
import ltd.fdsa.devtools.generate.enums.FieldQuery;
import ltd.fdsa.devtools.generate.enums.FieldType;
import ltd.fdsa.devtools.generate.enums.FieldVerify;
import ltd.fdsa.devtools.generate.enums.ModuleType;
import ltd.fdsa.devtools.generate.template.*;
import ltd.fdsa.devtools.generate.utils.GenerateUtil;
import ltd.fdsa.modules.system.domain.Menu;
import ltd.fdsa.modules.system.domain.Role;
import ltd.fdsa.modules.system.enums.MenuTypeEnum;
import ltd.fdsa.modules.system.service.MenuService;
import ltd.fdsa.modules.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @date 2018/8/14
 */
@Controller
@RequestMapping("/dev/code")
public class GenerateController {

    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("basic", DefaultValue.getBasic());
        model.addAttribute("fieldList", DefaultValue.fieldList());
        model.addAttribute("fieldType", ToolUtil.enumToMap(FieldType.class));
        model.addAttribute("fieldQuery", ToolUtil.enumToMap(FieldQuery.class));
        model.addAttribute("fieldVerify", ToolUtil.enumToMap(FieldVerify.class));
        return "/devtools/generate/index";
    }

    @PostMapping("/save")
    @ResponseBody
    public ResultVo save(@RequestBody Generate generate) {
        Map<String, String> fieldMap = new LinkedHashMap<>();
        if (generate.getTemplate().isEntity()) {
            fieldMap.put("实体类", EntityTemplate.generate(generate));
        }
        if (generate.getTemplate().isValidator()) {
            fieldMap.put("验证类", ValidatorTemplate.generate(generate));
        }
        if (generate.getTemplate().isRepository()) {
            fieldMap.put("数据访问层", RepositoryTemplate.generate(generate));
        }
        if (generate.getTemplate().isService()) {
            fieldMap.put("服务层", ServiceTemplate.generate(generate));
            fieldMap.put("服务实现层", ServiceImplTemplate.generate(generate));
        }
        if (generate.getTemplate().isController()) {
            fieldMap.put("控制器", ControllerTemplate.generate(generate));
        }
        if (generate.getTemplate().isIndex()) {
            fieldMap.put("列表页面", IndexHtmlTemplate.generate(generate));
        }
        if (generate.getTemplate().isAdd()) {
            fieldMap.put("添加页面", AddHtmlTemplate.generate(generate));
        }
        if (generate.getTemplate().isDetail()) {
            fieldMap.put("详细页面", DetailHtmlTemplate.generate(generate));
        }
        // 当模块结构为独立模块时生成业务模块
        if (generate.getBasic().getModuleType().equals(ModuleType.ALONE.getCode())) {
            GenerateUtil.genMavenModule(generate);
        }
        // 自动生成菜单和角色权限
        if (generate.getTemplate().isController()) {
            genMenuRule(generate);
        }

        return ResultVoUtil.success(fieldMap);
    }

    /**
     * 自动生成菜单和角色权限
     */
    private void genMenuRule(Generate generate) {
        Template temp = generate.getTemplate();
        Basic basic = generate.getBasic();
        // 父级编号
        Long pid = basic.getGenPMenu();
        // 所有父级编号
        Menu pMenu = menuService.getById(pid);
        String pids = pMenu.getPids() + ",[" + pid + "]";
        // 排序序号
        Integer sortMax = menuService.getSortMax(pid);
        int sort = sortMax != null ? sortMax + 1 : 1;
        // 菜单url前缀
        String preUrl = basic.getRequestMapping() + "/";
        String prePerms = preUrl.substring(1).replace("/", ":");
        // 菜单状态组
        boolean[] status = {temp.isIndex(), temp.isAdd(), temp.isAdd(), temp.isDetail(), temp.isIndex()};
        // 菜单标题组
        String[] titles = {basic.getGenTitle(), "添加", "编辑", "详细", "修改状态"};
        // 菜单名称组
        String[] name = {"index", "add", "edit", "detail", "status"};

        // 保存全部菜单
        List<Menu> bthMenus = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            if (status[i]) {
                Menu menu = new Menu();
                menu.setTitle(titles[i]);
                menu.setPid(pid);
                menu.setPids(pids);
                menu.setUrl(preUrl + name[i]);
                menu.setPerms(prePerms + name[i]);
                // 判断是否为父级菜单
                if (i == 0) {
                    if (pid == 0) {
                        menu.setType(MenuTypeEnum.DIRECTORY.getCode());
                    } else {
                        menu.setType(MenuTypeEnum.MENU.getCode());
                    }
                    pMenu = menuService.getByMenuToExample(menu);
                    if (pMenu == null) {
                        menu.setSort(sort);
                        pMenu = menuService.save(menu);
                    }
                    pid = pMenu.getId();
                    pids = pids + ",[" + pid + "]";
                } else {
                    Menu bthMenu = menuService.getByMenuToExample(menu);
                    if (bthMenu == null) {
                        Integer bthSort = menuService.getSortMax(pid);
                        menu.setSort(bthSort != null ? bthSort + 1 : 1);
                        menu.setType(MenuTypeEnum.BUTTON.getCode());
                        bthMenus.add(menu);
                    }
                }
            }
        }
        menuService.save(bthMenus);

        // 添加管理员角色权限
        Role role = roleService.getById(AdminConst.ADMIN_ROLE_ID);
        bthMenus.add(pMenu);
        role.getMenus().addAll(bthMenus);
        roleService.save(role);
    }
}
