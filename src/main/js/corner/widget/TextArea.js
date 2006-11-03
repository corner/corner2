/*
	Copyright (c) Beijing Maxinfo Technology Co.,Ltd.
	All Rights Reserved.

*/

dojo.provide("corner.widget.TextArea");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.Select");

//定义一个TextArea的widget.
dojo.widget.defineWidget(
	"corner.widget.TextArea",
 	dojo.widget.Select,{
		autoComplete: false,
		forceValidOption: false,
		protoUrl:null,
		templatePath: dojo.uri.dojoUri("../corner/widget/templates/TextAreaBox.html"),
		templateCssPath: dojo.uri.dojoUri("../corner/widget/templates/TextAreaBox.css"),
		fillInTemplate: function( args,  frag){
			this.downArrowNode=document.createElement("span");
			corner.widget.TextArea.superclass.fillInTemplate.call(this,args,frag);
			var source = this.getFragNodeRef(frag);
			dojo.html.removeClass(this.textInputNode,"dojoComboBox");
			this.domNode.style.textAlign="left";
			if(dojo.html.getAttribute(source,"rows")){
				this.textInputNode.setAttribute("rows",dojo.html.getAttribute(source,"rows"));
			}
			if(dojo.html.getAttribute(source,"cols")){
				this.textInputNode.setAttribute("cols",dojo.html.getAttribute(source,"cols"));
			}
		},
		startSearchFromInput: function(){
			var searchStr = this.textInputNode.value
			if(searchStr != null && searchStr.length>0 && searchStr.search(';')>-1){
				this.startSearch(searchStr.substr(searchStr.lastIndexOf(';')+1,searchStr.length));
			}
			else{
				this.startSearch(this.textInputNode.value);
			}
		},

		//overwirte the selectOptionMethod,record all of the user input
		//在用户选择了下拉菜单之后，记录并现实先前输入的全部内容
		selectOption: function(evt){
			var tgt = null;
			if(!evt){
				evt = { target: this._highlighted_option };
			}
	
			if(!dojo.dom.isDescendantOf(evt.target, this.optionsListNode)){
				// handle autocompletion where the the user has hit ENTER or TAB
	
				// if the input is empty do nothing
				if(!this.textInputNode.value.length){
					return;
				}
				tgt = dojo.dom.firstElement(this.optionsListNode);
	
				// 判断用户输入是否在 option list 中
				/*
				if(!tgt || !this._isInputEqualToResult(tgt.getAttribute("resultName")),tgt.getAttribute("resultValue")){
					return;
				}*/
				// otherwise the user has accepted the autocompleted value
			}else{
				tgt = evt.target; 
			}
	
			while((tgt.nodeType!=1)||(!tgt.getAttribute("resultName"))){
				tgt = tgt.parentNode;
				if(tgt === document.body){
					return false;
				}
			}
	
			this.textInputNode.value = tgt.getAttribute("resultName");		
			this.selectedResult = [tgt.getAttribute("resultName"), tgt.getAttribute("resultValue")];		
			//自动填充':'
			//this.setAllValues(this.comboBoxSelectionValue.value.substr(0,this.comboBoxSelectionValue.value.lastIndexOf(';')+1)+tgt.getAttribute("resultName")+":", this.comboBoxSelectionValue.value.substr(0,this.comboBoxSelectionValue.value.lastIndexOf(';')+1));
			this.setAllValues(this.comboBoxSelectionValue.value.substr(0,this.comboBoxSelectionValue.value.lastIndexOf(';')+1)+tgt.getAttribute("resultName"), this.comboBoxSelectionValue.value.substr(0,this.comboBoxSelectionValue.value.lastIndexOf(';')+1));
			
			if(!evt.noHide){
				this.hideResultList();
				//设置是否选中刚TextArea中的全部内容
				//this.setSelectedRange(this.textInputNode, 0, null);
			}
			this.tryFocus();
		},
		convertResultList:function(results){
			dojo.debug("call here");
			var r=[];
			for(var i=0;i<results.length;i++){
				var tr=results[i];
				if(tr)
					results[i]=[tr[1],tr[0]];
			}
		},
		postCreate: function(){
			corner.widget.TextArea.superclass.postCreate.call(this);
			dojo.event.connect("before",this,"openResultList",this,"convertResultList");
			//此处加入对禁用IE缓存的策略 通过动态构建URL
			//see http://dev.bjmaxinfo.com/projects/manufacturing-system/wiki/2006/10/26/09.11
			this.protoUrl=this.dataUrl;
			
			dojo.event.connect("before",this,"startSearch",this,"constructDynamicUrl");
			
		},
		constructDynamicUrl:function(){
			var d = new Date();
			var time = d.getTime();
			tmpUrl=this.protoUrl;
			if (tmpUrl.indexOf('?') > 0)
				tmpUrl = tmpUrl+'&prevent_cache='+time;
			else
				tmpUrl = tmpUrl+'?prevent_cache='+time;
			
			this.dataUrl=tmpUrl;
			dojo.debug("data url :["+tmpUrl+"]");
		}
 	
 });



