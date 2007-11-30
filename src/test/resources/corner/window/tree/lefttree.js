var LeftTree = Class.create();
LeftTree.prototype = {
	initialize: function(element,page,title) {
		Ajax.Tree.Invoice = Ajax.Tree.create({
				types: {
					leftTreeSite: {
						page: 'test.txt',
						insertion: function(el,data){
							var node = Builder.node('dl',{},
							[
								Builder.node('dt',[data.name])
							]);
							el.appendChild(node);

							this.element.setAttribute("left",data.left);
							this.element.setAttribute("right",data.right);
							this.element.setAttribute("depth",data.depth);
							this.element.setAttribute("treeName",data.name);
							this.element.setAttribute("entityId",data.id);
							
							if((data.right - data.left) == 1){
								this.clickExpense = function(evt){
									queryBox.onSelect(this.element);
									queryBox.win.close();
								}.bind(this);
								Event.observe(this.span,'click',this.clickExpense);
							}else{
								this.clickExpense = function(evt){
									this.onClick();
								}.bind(this);
								Event.observe(this.span,'click',this.clickExpense);
							}
						},
						callback:{
							call:function(node,id){
								var left=node.element.getAttribute("left");
								var right=node.element.getAttribute("right");
								var depth=node.element.getAttribute("depth");
								
								var dependField = queryBox.options.dependField;
								
								var depend="null";
								
								if(dependField != null && dependField != ""){
									depend = parent.document.getElementById(dependField).value;
								}
															
								return "left=" + left + "&" + "right=" + right + "&" + "depth=" + depth + 
								"&" + "queryClassName=" + queryBox.options.queryClassName + "&" + "dependField=" + depend;
							}
						}
					}
				},
				createNodes: function(nodes){
					if(!nodes.length){ return; }
					this.showChildren();
					this.loaded = true;
					for(var i=0; i < nodes.length; i++){
						if((nodes[i].data.right - nodes[i].data.left) == 1){
							this.options.leafNode = true;
						}else{
							this.options.leafNode = false;
						}
						var newNode = new this.constructor(this.element,nodes[i].id,nodes[i].type,nodes[i]);
					}
					if(this.options.sortable){ this.createSortable(); }
				}
			});
			new Ajax.Tree.Invoice(element,'root','leftTreeSite',{data:{name:title,left:-1,right:-1,depth:0}});
	}
}