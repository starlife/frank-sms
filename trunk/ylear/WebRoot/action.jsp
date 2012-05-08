<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
</head>
 
<body>
<h1>Struts 2 action tag example</h1>
 
<ol>
 
<li>
Execute the action's result, render the page here. 
<s:action id="abc" name="sayHelloAction" executeResult="true"/>
</li>
 
<li>
Doing the same as above, but call action's sayStruts2() method. 
<s:action name="sayHelloAction!sayStruts2" executeResult="true"/>
</li>
 
<li>
Call the action's saySysOut() method only, no result will be rendered,
By defautlt, executeResult="false". 
<s:action name="sayHelloAction!saySysOut" />
</li>
 
</ol>
 <button onclick="" >flush</button>
</body>
</html>