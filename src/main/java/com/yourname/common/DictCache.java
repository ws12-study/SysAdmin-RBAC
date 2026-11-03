package com.yourname.common;

import com.yourname.entity.SysDictData;
import com.yourname.mapper.SysDictDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典缓存：进程内缓存全部启用字典，并同步到 ServletContext，
 * JSP 可直接用 {@code #application.dictMap['sys_user_sex']} 取值。
 *
 * @author yourname
 */
@Component
public class DictCache implements InitializingBean, ServletContextAware {

    /** 类型 -> 字典数据列表 */
    public static final String KEY_MAP = "dictMap";
    /** 类型 -> (键值 -> 标签) */
    public static final String KEY_LABEL = "dictLabel";

    private static final Logger log = LoggerFactory.getLogger(DictCache.class);

    @Autowired
    private SysDictDataMapper dictDataMapper;

    private ServletContext servletContext;

    private volatile Map<String, List<SysDictData>> dictMap = Collections.emptyMap();
    private volatile Map<String, Map<String, String>> dictLabelMap = Collections.emptyMap();

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void afterPropertiesSet() {
        refresh();
    }

    /**
     * 重新加载字典（字典变更后调用）
     */
    public synchronized void refresh() {
        Map<String, List<SysDictData>> map = new LinkedHashMap<String, List<SysDictData>>();
        Map<String, Map<String, String>> labels = new HashMap<String, Map<String, String>>();

        List<SysDictData> all = dictDataMapper.selectAllData();
        if (all != null) {
            for (SysDictData data : all) {
                if (!StringUtils.hasText(data.getDictType())) {
                    continue;
                }
                List<SysDictData> list = map.get(data.getDictType());
                if (list == null) {
                    list = new ArrayList<SysDictData>();
                    map.put(data.getDictType(), list);
                }
                list.add(data);

                Map<String, String> labelMap = labels.get(data.getDictType());
                if (labelMap == null) {
                    labelMap = new HashMap<String, String>();
                    labels.put(data.getDictType(), labelMap);
                }
                labelMap.put(data.getDictValue(), data.getDictLabel());
            }
        }

        this.dictMap = map;
        this.dictLabelMap = labels;
        if (servletContext != null) {
            servletContext.setAttribute(KEY_MAP, map);
            servletContext.setAttribute(KEY_LABEL, labels);
        }
        log.info("字典缓存已刷新：{} 个类型，{} 条数据", map.size(), all == null ? 0 : all.size());
    }

    /**
     * Java 侧取字典列表
     */
    public List<SysDictData> getDictData(String dictType) {
        List<SysDictData> list = dictMap.get(dictType);
        return list == null ? Collections.<SysDictData>emptyList() : list;
    }

    /**
     * Java 侧取单个标签
     */
    public String getLabel(String dictType, String dictValue) {
        Map<String, String> labelMap = dictLabelMap.get(dictType);
        if (labelMap == null) {
            return null;
        }
        return labelMap.get(dictValue);
    }

}
