/**
 * Copyright (c) 2012 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

/**
 * Raw entity tables using DataTables. 
 */
$.fn.createRawEntityTable = function() {
   $(this).each(function() {
       var thisQuery = $(this);

       // this function is used to remove unnecessary fields from the server request to make it smaller
       function removeIndexedRequestFields(requestData, prefix) {
           return requestData.filter(function(x) {
               return (x.name.lastIndexOf(prefix, 0) !== 0);
           });
       }

       // removes all unnecessary fields from the server request to make it smaller
       function slimServerRequest(originalRequestData) {
           requestData = originalRequestData;
           requestData = removeIndexedRequestFields(requestData, 'mDataProp_');
           requestData = removeIndexedRequestFields(requestData, 'bSearchable_');
           requestData = removeIndexedRequestFields(requestData, 'sSearch_');
           requestData = removeIndexedRequestFields(requestData, 'bRegex_');
           requestData = removeIndexedRequestFields(requestData, 'bSortable_');

           originalRequestData.length = 0;
           Array.prototype.push.apply(originalRequestData, requestData);
           console.log(originalRequestData);
       }

       // extract configuration that was written by the server
       var configurationText = thisQuery.children('.server-to-client-data').text();
       var configuration = $.parseJSON(configurationText);

       // build the DOM description code for the DataTable
       var top1DomCode = (configuration.showSearchField ? '<\"top1\"lf>' : '<\"top1\"l>');
       var top2DomCode = '<\"top2\"ip>';
       var bottomDomCode = '<\"bottom\"ip>';
       var domCode = (top1DomCode + top2DomCode + "rt" + bottomDomCode);

       // create the table
       var tableQuery = thisQuery.children('.data-table-container').children('table');
       var columnCount = tableQuery.children('thead').children('tr').children('th').size();
       var tableInfo = tableQuery.dataTable({
           'bProcessing': true,
           'bServerSide': true,
           'sAjaxSource': configuration.url,
           'sDom': domCode,
           'aoColumns': configuration.columns,
           'fnServerParams' : slimServerRequest
       });

       // register the click handler
       tableQuery.on('click', 'tr', function () {
           var data = tableInfo._(this)[0];
           var url = data[columnCount];
           location.href = url;
       });

   });
}
