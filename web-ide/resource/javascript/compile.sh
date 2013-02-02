java -jar closure-compiler/compiler.jar --charset utf-8 --js own.js --js_output_file own-min.js --process_jquery_primitives
java -jar closure-compiler/compiler.jar --charset utf-8 --js jquery.contextMenu.js --js_output_file jquery.contextMenu.min.js --process_jquery_primitives
# Wicket has its own jQuery version -> collision
# cat jquery-1.8.0.min.js jquery-ui-1.10.0.custom.js jquery.tools.min.js datatables.min.js jquery.contextMenu.min.js own-header.js own-min.js > common.js
# TODO use "min" context menu
cat jquery-ui-1.10.0.custom.js jquery.tools.min.js datatables.min.js jquery.contextMenu.js jquery.hotkeys.js own-header.js own-min.js > common.js
cp common.js ../../src/name/martingeisse/webide/application/common.js
