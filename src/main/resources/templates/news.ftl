<html>
<body>
<pre>
    hello freemaker

   Value: ${value}

    <#list colors as p>
        这是第${p_index+1}
        ${p}
        <br/>
    </#list>

    <#list map?keys as k>

        <option value="${k}">
            Key ${k}
            Value ${map[k]}
        </option>
    </#list>



    ${user.name}
    ${user.getName()}

    <#assign title="nowcoder">
    <#include "header.ftl ">   <br>
        ${title}

        <#macro color_render color index>

            <#return>


            </#return>
        </#macro>



</pre>

</body>
</html>