$.fn.createRawEntityTable=function(){$(this).each(function(){function c(a,b){return a.filter(function(a){return 0!==a.name.lastIndexOf(b,0)})}var b=$(this),d=b.children(".server-to-client-data").text(),d=$.parseJSON(d),e=(d.showSearchField?'<"top1"lf>':'<"top1"l>')+'<"top2"ip>rt<"bottom"ip>',b=b.children(".data-table-container").children("table"),f=b.children("thead").children("tr").children("th").size(),g=b.dataTable({bProcessing:!0,bServerSide:!0,sAjaxSource:d.url,sDom:e,aoColumns:d.columns,fnServerParams:function(a){requestData=
a;requestData=c(requestData,"mDataProp_");requestData=c(requestData,"bSearchable_");requestData=c(requestData,"sSearch_");requestData=c(requestData,"bRegex_");requestData=c(requestData,"bSortable_");a.length=0;Array.prototype.push.apply(a,requestData);console.log(a)}});b.on("click","tr",function(){var a=g._(this)[0][f];location.href=a})})};