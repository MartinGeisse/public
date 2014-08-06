rm build/*
cat thirdparty/jquery.js thirdparty/datatables.min.js thirdparty/fastclick.js thirdparty/jquery.contextMenu.js thirdparty/jquery.hotkeys.js thirdparty/jquery.scrollTo.min.js thirdparty/jquery.tools.min.js thirdparty/wicket-event-jquery.js thirdparty/wicket-ajax-jquery.js thirdparty/bootstrap.js thirdparty/jquery.fitvids.js thirdparty/jscurry-0.3.0.js thirdparty/jqmath-0.4.0.js own/own.js > build/raw.js
cat own/header.js build/raw.js > build/common.js
cp build/common.js ../../src/main/java/name/martingeisse/papyros/application/page/common.js
