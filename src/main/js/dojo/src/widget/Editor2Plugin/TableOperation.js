

dojo.provide("dojo.widget.Editor2Plugin.TableOperation");

dojo.require("dojo.widget.Editor2");




dojo.event.topic.subscribe("dojo.widget.RichText::init", function(editor){
editor.__TableOperationShowBorder = false;

if(dojo.render.html.ie){
//add/remove a class to a table with border=0 to show the border when loading/saving
editor.contentDomPreFilters.push(dojo.widget.Editor2Plugin.TableOperation.showIETableBorder);
editor.contentDomPostFilters.push(dojo.widget.Editor2Plugin.TableOperation.removeIEFakeClass);
//include the css file to show table border when border=0



}
dojo.event.connect(editor, "editorOnLoad", function(){
dojo.widget.Editor2Plugin.TableOperation.toggleTableBorderCommand.execute(editor);
});
});

dojo.widget.Editor2Plugin.TableOperation = {
getToolbarItem: function(name){
var name = name.toLowerCase();

var item;
switch(name){
case 'inserttable':
case 'toggletableborder':
item = new dojo.widget.Editor2ToolbarButton(name);
}

return item;
},
getContextMenuGroup: function(name, contextmenuplugin){
return new dojo.widget.Editor2Plugin.TableContextMenu(contextmenuplugin);
},
deleteTableCommand: {
execute: function(){
var curInst = dojo.widget.Editor2Manager.getCurrentInstance();
var table = dojo.withGlobal(curInst.window, "getAncestorElement", dojo.html.selection, ['table']);
if(table){
dojo.withGlobal(curInst.window, "selectElement", dojo.html.selection, [table]);
curInst.execCommand("inserthtml", " "); //Moz does not like an empty string, so a space here instead
}
},
getState: function(){
var curInst = dojo.widget.Editor2Manager.getCurrentInstance();
var table = dojo.withGlobal(curInst.window, "hasAncestorElement", dojo.html.selection, ['table']);
return table ? dojo.widget.Editor2Manager.commandState.Enabled : dojo.widget.Editor2Manager.commandState.Disabled;
},
destory: function(){}
},
toggleTableBorderCommand: {
execute: function(instance){
var curInst = instance || dojo.widget.Editor2Manager.getCurrentInstance();
if(curInst.__TableOperationShowBorder){
curInst.__TableOperationShowBorder = false;
if(dojo.render.html.moz){
curInst.removeStyleSheet(dojo.uri.dojoUri("src/widget/templates/Editor2/showtableborder_gecko.css"));
}else if(dojo.render.html.ie){
curInst.removeStyleSheet(dojo.uri.dojoUri("src/widget/templates/Editor2/showtableborder_ie.css"));
}
}else{
curInst.__TableOperationShowBorder = true;
if(dojo.render.html.moz){
curInst.addStyleSheet(dojo.uri.dojoUri("src/widget/templates/Editor2/showtableborder_gecko.css"));
}else if(dojo.render.html.ie){
curInst.addStyleSheet(dojo.uri.dojoUri("src/widget/templates/Editor2/showtableborder_ie.css"));
}
}
},
getState: function(){
var curInst = dojo.widget.Editor2Manager.getCurrentInstance();
return curInst.__TableOperationShowBorder ? dojo.widget.Editor2Manager.commandState.Latched : dojo.widget.Editor2Manager.commandState.Enabled;
},
destory: function(){}
},
showIETableBorder: function(dom){
var tables = dom.getElementsByTagName('table');
dojo.lang.forEach(tables, function(t){
dojo.html.addClass(t, "dojoShowIETableBorders");
});
return dom;
},
removeIEFakeClass: function(dom){
var tables = dom.getElementsByTagName('table');
dojo.lang.forEach(tables, function(t){
dojo.html.removeClass(t, "dojoShowIETableBorders");
});
return dom;
}
}


dojo.widget.Editor2Manager.registerCommand("toggletableborder", dojo.widget.Editor2Plugin.TableOperation.toggleTableBorderCommand);

dojo.widget.Editor2Manager.registerCommand("inserttable", new dojo.widget.Editor2DialogCommand('inserttable',
{contentFile: "dojo.widget.Editor2Plugin.InsertTableDialog",
contentClass: "Editor2InsertTableDialog",
title: "Insert/Edit Table", width: "450px", height: "250px"}));

dojo.widget.Editor2Manager.registerCommand("deletetable", dojo.widget.Editor2Plugin.TableOperation.deleteTableCommand);


dojo.widget.Editor2ToolbarItemManager.registerHandler(dojo.widget.Editor2Plugin.TableOperation.getToolbarItem);


if(dojo.widget.Editor2Plugin.ContextMenuManager){
dojo.widget.Editor2Plugin.ContextMenuManager.registerGroup('Table', dojo.widget.Editor2Plugin.TableOperation.getContextMenuGroup);

dojo.declare("dojo.widget.Editor2Plugin.TableContextMenu",
dojo.widget.Editor2Plugin.SimpleContextMenu,
{
createItems: function(){
this.items.push(dojo.widget.createWidget("Editor2ContextMenuItem", {caption: "Delete Table", command: 'deletetable'}));
this.items.push(dojo.widget.createWidget("Editor2ContextMenuItem", {caption: "Table Property", command: 'inserttable', iconClass: "TB_Button_Icon TB_Button_Table"}));
},
checkVisibility: function(){
var curInst = dojo.widget.Editor2Manager.getCurrentInstance();
var table = dojo.withGlobal(curInst.window, "hasAncestorElement", dojo.html.selection, ['table']);

if(dojo.withGlobal(curInst.window, "hasAncestorElement", dojo.html.selection, ['table'])){
this.items[0].show();
this.items[1].show();
return true;
}else{
this.items[0].hide();
this.items[1].hide();
return false;
}
}
});
}