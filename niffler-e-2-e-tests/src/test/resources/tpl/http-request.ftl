<#-- @ftlvariable name="data" type="guru.qa.niffler.model.allure.HttpAttachmentData" -->
<head>
    <meta http-equiv="content-type" content="application/json; charset = UTF-8">
    <link type="text/css" href="https://yandex.st/highlightjs/8.0/styles/github.min.css" rel="stylesheet"/>
    <script type="text/javascript" src="https://yandex.st/highlightjs/8.0/highlight.min.js"></script>
    <script type="text/javascript" src="https://yandex.st/highlightjs/8.0/languages/sql.min.js"></script>
    <script type="text/javascript">hljs.initHighlightingOnLoad();</script>

    <style>
        pre {
            white-space: pre-wrap;
        }
    </style>
</head>
<div>
    <h3>Request: ${data.method!"GET"} ${data.url!"-"}</h3>

    <#if data.headers?has_content>
        <h4>Headers:</h4>
        <div>
            <#list data.headers as name, value>
                <div><code>${name}: ${value}</code></div>
            </#list>
        </div>
    </#if>

    <#if data.body??>
        <h4>Body:</h4>
        <pre class="pre-scrollable">
        <code class="language-json">${data.body}</code>
    </pre>
    </#if>

    <h4>Curl:</h4>
    <pre>
        <code>curl -X ${data.method} "${data.url}" <#list data.headers as name, value> -H "${name}: ${value}" </#list> <#if data.body??> -d '${data.body}'</#if></code>
    </pre>
</div>