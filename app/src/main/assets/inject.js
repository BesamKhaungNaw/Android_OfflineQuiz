document.getElementsByTagName('form')[0].onsubmit = function(){
					var val="";
					var content = {};
					var inputs = document.getElementsByTagName('form')[0];
					for(var i=0;i<inputs.length;i++){
							if(inputs[i].checked || inputs[i].type === "text" || inputs[i].type === "textarea" || inputs[i].type === "hidden"){
									if(inputs[i].type != "hidden"){
												var newArray = {};
												newArray[inputs[i].name] = inputs[i].value;
												content[val].push(newArray);

									} else{
											val = inputs[i].value;
											content[val] = [];
									}
							}
					}
					window.Android.htmlFilledUpData(JSON.stringify(content));
				return false;
}
