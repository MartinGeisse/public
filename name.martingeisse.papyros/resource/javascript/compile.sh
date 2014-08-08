rm build/*
java -jar closure-compiler/compiler.jar --charset utf-8 --js thirdparty/jquery.js --js thirdparty/datatables.min.js --js thirdparty/fastclick.js --js thirdparty/jquery.contextMenu.js --js thirdparty/jquery.hotkeys.js --js thirdparty/jquery.scrollTo.min.js --js thirdparty/wicket-event-jquery.js --js thirdparty/wicket-ajax-jquery.js --js thirdparty/bootstrap.js --js thirdparty/jquery.fitvids.js --js own/own.js --js_output_file build/raw.js --process_jquery_primitives
cat own/header.js build/raw.js > build/common.js
cp build/common.js ../../src/main/java/name/martingeisse/papyros/application/page/common.js
