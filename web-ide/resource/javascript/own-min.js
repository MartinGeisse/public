$(document).bind("keydown keypress",function(c){if(8==c.which){var b=!0,a=event.srcElement||event.target,d=a.tagName.toLowerCase();if(("input"===d||"textarea"===d)&&!a.disabled&&!a.readOnly)if(a=a.type.toLowerCase(),"text"===a||"password"===a||"textarea"===d)b=!1;b&&c.preventDefault()}});
$.fn.createRawEntityTable=function(){$(this).each(function(){function c(a,b){return a.filter(function(a){return 0!==a.name.lastIndexOf(b,0)})}var b=$(this),a=b.children(".server-to-client-data").text(),a=$.parseJSON(a),d=(a.showSearchField?'<"top1"lf>':'<"top1"l>')+'<"top2"ip>rt<"bottom"ip>',b=b.children(".data-table-container").children("table"),e=b.children("thead").children("tr").children("th").size(),f=b.dataTable({bProcessing:!0,bServerSide:!0,sAjaxSource:a.url,sDom:d,aoColumns:a.columns,fnServerParams:function(a){requestData=
a;requestData=c(requestData,"mDataProp_");requestData=c(requestData,"bSearchable_");requestData=c(requestData,"sSearch_");requestData=c(requestData,"bRegex_");requestData=c(requestData,"bSortable_");a.length=0;Array.prototype.push.apply(a,requestData);console.log(a)}});b.on("click","tr",function(){var a=f._(this)[0][e];location.href=a})})};
$.fn.applyStyle=function(c,b,a){"string"==typeof c?this.removeClass(c):null!=c&&"undefined"!=typeof c.length&&this.remove(c.join(" "));"string"==typeof b?this.addClass(b):null!=b&&"undefined"!=typeof b.length&&this.add(b.join(" "));if(null!=a)for(key in a)this.css(key,a[key])};
