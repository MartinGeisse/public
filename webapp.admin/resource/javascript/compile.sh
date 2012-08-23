java -jar closure-compiler/compiler.jar --charset utf-8 --js own.js --js_output_file own-min.js --process_jquery_primitives
cat jquery-1.8.0.min.js jquery.tools.min.js datatables.min.js own-header.js own-min.js > common.js
cp common.js ../../src/name/martingeisse/admin/component/page/common.js
