<!DOCTYPE html>
<html lang="ru">
<#-- @ftlvariable name="data" type="io.qameta.allure.attachment.http.HttpResponseAttachment" -->
<head>
    <title>Response</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/styles/github.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/highlight.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/languages/json.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/languages/xml.min.js"></script>

    <style>
        body {
            font-family: Arial, sans-serif;
            font-size: 13px;
        }
        h4 {
            margin-top: 16px;
            margin-bottom: 6px;
        }
        pre {
            background: #cab6b6;
            padding: 10px;
            border-radius: 4px;
            overflow-x: auto;
        }
        table {
            border-collapse: collapse;
            width: 100%;
        }
        th, td {
            border: 1px solid #8ba887;
            padding: 6px;
            text-align: left;
            vertical-align: top;
            font-family: monospace;
        }
        th {
            background: #8b80b4;
            width: 200px;
        }
    </style>

    <script>
        function highlightBody() {
            const code = document.getElementById("body-code");
            if (!code) return;
            const text = code.textContent.trim();
            if (!text) return;
            try {
                const json = JSON.parse(text);
                code.textContent = JSON.stringify(json, null, 2);
                code.className = "language-json";
                hljs.highlightElement(code);
                return;
            } catch (e) {}
            if (text.startsWith("<")) {
                code.className = "language-xml";
                hljs.highlightElement(code);
            }
        }
        window.onload = highlightBody;
    </script>
</head>

<body>

<h4>Headers</h4>
<#if data.headers?? && data.headers?has_content>
    <table>
        <#list data.headers?keys as key>
            <tr>
                <th>${key}</th>
                <td>${data.headers[key]!""}</td>
            </tr>
        </#list>
    </table>
</#if>

<h3>HTTP response</h3>

<p>
    <b>Status code:</b>
    <#if data.responseCode??>
        ${data.responseCode}
    <#else>
        not available
    </#if>
</p>

<h4>Body</h4>
<#if data.body?? && data.body?has_content>
    <pre><code id="body-code">${data.body?html}</code></pre>
</#if>

</body>
</html>