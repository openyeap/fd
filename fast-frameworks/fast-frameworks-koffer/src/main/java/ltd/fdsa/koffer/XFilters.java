package cn.zhumingwu.koffer;

import cn.zhumingwu.koffer.filter.XAllEntryFilter;
import cn.zhumingwu.koffer.filter.XAnyEntryFilter;
import cn.zhumingwu.koffer.filter.XMixEntryFilter;
import cn.zhumingwu.koffer.filter.XNotEntryFilter;
import cn.zhumingwu.koffer.jar.KofferAntEntryFilter;
import cn.zhumingwu.koffer.jar.KofferRegexEntryFilter;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;

import java.util.Collection;

/**
 * 过滤器工具类
 */
public abstract class XFilters {

    /**
     * 创建多个子过滤器AND连接的混合过滤器
     *
     * @return 多个子过滤器AND连接的混合过滤器
     */
    public static XMixEntryFilter<JarArchiveEntry> all() {
        return new XAllEntryFilter<>();
    }

    /**
     * 创建多个子过滤器AND连接的混合过滤器
     *
     * @param filters 子过滤器
     * @return 多个子过滤器AND连接的混合过滤器
     */
    public static XMixEntryFilter<JarArchiveEntry> all(
            Collection<? extends XEntryFilter<? extends JarArchiveEntry>> filters) {
        return new XAllEntryFilter<>(filters);
    }

    /**
     * 创建多个子过滤器AND连接的混合过滤器
     *
     * @return 多个子过滤器AND连接的混合过滤器
     */
    public static XMixEntryFilter<JarArchiveEntry> and() {
        return all();
    }

    /**
     * 创建多个子过滤器AND连接的混合过滤器
     *
     * @param filters 子过滤器
     * @return 多个子过滤器AND连接的混合过滤器
     */
    public static XMixEntryFilter<JarArchiveEntry> and(
            Collection<? extends XEntryFilter<? extends JarArchiveEntry>> filters) {
        return all(filters);
    }

    /**
     * 创建多个子过滤器OR连接的混合过滤器
     *
     * @return 多个子过滤器OR连接的混合过滤器
     */
    public static XMixEntryFilter<JarArchiveEntry> any() {
        return new XAnyEntryFilter<>();
    }

    /**
     * 创建多个子过滤器OR连接的混合过滤器
     *
     * @param filters 子过滤器
     * @return 多个子过滤器OR连接的混合过滤器
     */
    public static XMixEntryFilter<JarArchiveEntry> any(
            Collection<? extends XEntryFilter<? extends JarArchiveEntry>> filters) {
        return new XAnyEntryFilter<>(filters);
    }

    /**
     * 创建多个子过滤器OR连接的混合过滤器
     *
     * @return 多个子过滤器OR连接的混合过滤器
     */
    public static XMixEntryFilter<JarArchiveEntry> or() {
        return any();
    }

    /**
     * 创建多个子过滤器OR连接的混合过滤器
     *
     * @param filters 子过滤器
     * @return 多个子过滤器OR连接的混合过滤器
     */
    public static XMixEntryFilter<JarArchiveEntry> or(
            Collection<? extends XEntryFilter<? extends JarArchiveEntry>> filters) {
        return any(filters);
    }

    /**
     * 创建非门逻辑运算过滤器，实际上就是将委派过滤器的过滤结果取反
     *
     * @param filter 委派过滤器
     * @return 非门逻辑过滤器
     */
    public static XNotEntryFilter<JarArchiveEntry> not(XEntryFilter<JarArchiveEntry> filter) {
        return new XNotEntryFilter<>(filter);
    }

    /**
     * 构造ANT表达式过滤器
     *
     * @param ant ANT表达式
     * @return ANT表达式过滤器
     */
    public static KofferAntEntryFilter ant(String ant) {
        return new KofferAntEntryFilter(ant);
    }

    /**
     * 构造正则表达式过滤器
     *
     * @param regex 正则表达式
     * @return 正则表达式过滤器
     */
    public static KofferRegexEntryFilter regex(String regex) {
        return new KofferRegexEntryFilter(regex);
    }
}
