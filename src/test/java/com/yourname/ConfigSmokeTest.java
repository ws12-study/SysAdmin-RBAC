package com.yourname;

import com.yourname.mapper.SysUserMapper;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

/**
 * 启动自检（无需 Tomcat 即可运行，用于验证 Spring / Struts / MyBatis 配置正确性）
 *
 * @author yourname
 */
public class ConfigSmokeTest {

    /** 只加载 Bean 定义、不实例化，因此不需要数据库即可校验 XML 与类的正确性 */
    @Test
    public void springXmlShouldLoad() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(new ClassPathResource("applicationContext.xml"));

        String actual[] = beanFactory.getBeanDefinitionNames();
        Assert.assertTrue("缺少 dataSource", Arrays.asList(actual).contains("dataSource"));
        Assert.assertTrue("缺少 sqlSessionFactory", Arrays.asList(actual).contains("sqlSessionFactory"));
        Assert.assertTrue("缺少 transactionManager", Arrays.asList(actual).contains("transactionManager"));
        Assert.assertTrue("Service 未被扫描到", Arrays.asList(actual).contains("sysUserServiceImpl"));

        System.out.println("[OK] applicationContext.xml 校验通过，Bean 数量 = " + beanFactory.getBeanDefinitionCount());
    }

    @Test
    public void strutsXmlShouldBeWellFormed() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try (InputStream in = new ClassPathResource("struts.xml").getInputStream()) {
            Document doc = factory.newDocumentBuilder().parse(in);
            Assert.assertEquals("struts", doc.getDocumentElement().getLocalName());
            int actionCount = doc.getElementsByTagName("action").getLength();
            Assert.assertTrue("action 数量异常：" + actionCount, actionCount >= 4);
            System.out.println("[OK] struts.xml 校验通过，action 数量 = " + actionCount);
        }
    }

    @Test
    public void mapperXmlShouldExist() {
        Assert.assertTrue("缺少 SysUserMapper.xml", new ClassPathResource("com/yourname/mapper/SysUserMapper.xml").exists());
        Assert.assertTrue("缺少 SysLoginLogMapper.xml", new ClassPathResource("com/yourname/mapper/SysLoginLogMapper.xml").exists());
        System.out.println("[OK] MyBatis Mapper XML 均已就位");
    }

    /** 需要真实 MySQL：先建库导数据，改好 jdbc.properties 后才会执行 */
    @Test
    public void dataSourceAndMyBatisShouldWork() {
        Assume.assumeTrue("跳过：3306 未通，请先启动 MySQL 并执行 sql/ 下的脚本", portOpen("127.0.0.1", 3306));

        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        try {
            long userCount = ctx.getBean(SysUserMapper.class).countUser();
            Assert.assertTrue("sys_user 表没有数据，请先执行 sysadmin_init_data.sql", userCount > 0);
            System.out.println("[OK] Druid + MyBatis 连通成功，sys_user 共 " + userCount + " 条");
        } finally {
            ctx.close();
        }
    }

    private static boolean portOpen(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 1500);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
