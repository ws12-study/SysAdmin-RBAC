<%-- 一次性提示（redirect 后由 session 带回，读一次即清除） --%>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<style>
    .flash { margin: 0 0 14px; padding: 10px 14px; border-radius: 8px; font-size: 13px; }
    .flash.ok { background: #e6f7ee; border: 1px solid #b7eb8f; color: #389e0d; }
    .flash.err { background: #ffecec; border: 1px solid #ffccc7; color: #d4380d; }
</style>
<s:if test="flash != null">
    <div class="flash <s:property value='flash.type'/>"><s:property value="flash.text"/></div>
</s:if>
