package ltd.fdsa.devtools.generate.template;

import ltd.fdsa.component.data.PageSort;
import ltd.fdsa.component.enums.StatusEnum;
import ltd.fdsa.component.utils.ToolUtil;
import ltd.fdsa.devtools.generate.domain.Generate;
import ltd.fdsa.devtools.generate.enums.TierType;
import ltd.fdsa.devtools.generate.utils.FileUtil;
import ltd.fdsa.devtools.generate.utils.GenerateUtil;
import ltd.fdsa.devtools.generate.utils.jAngel.JAngelContainer;
import ltd.fdsa.devtools.generate.utils.jAngel.nodes.Document;
import ltd.fdsa.devtools.generate.utils.jAngel.parser.Expression;
import ltd.fdsa.devtools.generate.utils.parser.JavaParseUtil;

import java.nio.file.FileAlreadyExistsException;
import java.util.Set;

public class ServiceImplTemplate {

    /**
     * 生成需要导入的包
     */
    private static Set<String> genImports(Generate generate) {
        JAngelContainer container = new JAngelContainer();
        container.importClass(JavaParseUtil.getPackage(generate, TierType.DOMAIN));
        container.importClass(JavaParseUtil.getPackage(generate, TierType.SERVICE));
        container.importClass(JavaParseUtil.getPackage(generate, TierType.DAO));
        container.importClass(StatusEnum.class);
        container.importClass(PageSort.class);
        return container.getImports();
    }

    /**
     * 生成类体
     */
    private static Document genClazzBody(Generate generate) {
        // 构建数据-模板表达式
        Expression expression = new Expression();
        expression.label("name", ToolUtil.lowerFirst(generate.getBasic().getTableEntity()));
        expression.label("entity", generate.getBasic().getTableEntity());
        String path = FileUtil.templatePath(ServiceImplTemplate.class);

        // 获取jAngel文档对象
        Document document = JavaParseUtil.document(path, expression, generate, TierType.SERVICE_IMPL);
        document.getContainer().importClass(genImports(generate));

        return document;
    }

    /**
     * 生成服务实现层模板
     */
    public static String generate(Generate generate) {
        // 生成文件
        String filePath = GenerateUtil.getJavaFilePath(generate, TierType.SERVICE_IMPL);
        try {
            Document document = genClazzBody(generate);
            GenerateUtil.generateFile(filePath, document.content());
        } catch (FileAlreadyExistsException e) {
            return GenerateUtil.fileExist(filePath);
        }
        return filePath;
    }
}
