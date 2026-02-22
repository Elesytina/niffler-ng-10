<#-- @ftlvariable name="data" type="guru.qa.niffler.model.allure.HttpAttachmentData" -->
<link type="text/css" href="https://yandex.st" rel="stylesheet"/>
<script type="text/javascript" src="https://yandex.st"></script>
<script type="text/javascript" src="https://yandex.st"></script>

<style>
    pre { background: #f8f8f8; border: 1px solid #77a977; border-radius: 4px; }
    code { font-family: 'Consolas', 'Monaco', monospace; font-size: 13px; line-height: 1.5; tab-size: 2; }
</style>

<#if data.headers?has_content>
    <h4>Headers:</h4>
    <div>
        <#list data.headers as name, value>
            <div><code>${name}: ${value}</code></div>
        </#list>
    </div>
</#if>

<div class="allure-http-container">
    <div style="margin-bottom: 5px;"><b>Response code:</b></div>
    <pre><code id="json-content" class="language-json">${data.responseCode}</code></pre>
</div>

<div class="allure-http-container">
    <div style="margin-bottom: 5px;"><b>Response Body:</b></div>
    <pre><code id="json-content" class="language-json">${data.body}</code></pre>
</div>