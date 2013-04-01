/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.editor.codemirror;

import java.util.List;

import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.webide.editor.IEditorFactory;
import name.martingeisse.webide.editor.IEditorFamily;
import name.martingeisse.webide.plugin.UserExtensionQuery;
import name.martingeisse.webide.plugin.PluginBundleHandle;

/**
 * Editor family implementation for CodeMirror-based editors.
 */
public class CodeMirrorEditorFamily implements IEditorFamily {

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.editor.IEditorFamily#createEditorFactory(name.martingeisse.common.javascript.analyze.JsonAnalyzer)
	 */
	@Override
	public IEditorFactory createEditorFactory(JsonAnalyzer configuration) {
		String modeId = configuration.analyzeMapElement("mode").expectString();
		if (modeId == null) {
			throw new RuntimeException("no CodeMirror mode specified");
		}
		try {
			return new CodeMirrorEditorFactory(getModeById(modeId));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private CodeMirrorMode getModeById(String modeId) throws Exception {

		// TODO
		long userId = 1;
		
		UserExtensionQuery query = new UserExtensionQuery(userId, "webide.editor.codemirror.mode");
		for (UserExtensionQuery.Result result : query.fetch()) {
			JsonAnalyzer modeSpec = new JsonAnalyzer(result.getDescriptor());
			String id = modeSpec.analyzeMapElement("id").expectString();
			if (id.equals(modeId)) {
				String anchorName = modeSpec.analyzeMapElement("anchor").expectString();
				Class<?> anchor = new PluginBundleHandle(result.getPluginBundleId()).getClassLoader().loadClass(anchorName);
				String[] paths = getPaths(modeSpec.analyzeMapElement("path"));
				return new CodeMirrorMode(id, anchor, paths);
			}
		}
		throw new IllegalArgumentException("unknown CodeMirror mode: " + modeId);

	}

	/**
	 * 
	 */
	private String[] getPaths(JsonAnalyzer analyzer) {
		Object value = analyzer.getValue();
		if (value instanceof String) {
			return new String[] {(String)analyzer.getValue()};
		} else if (value instanceof List) {
			for (Object element : (List<?>)value) {
				if (!(element instanceof String)) {
					throw analyzer.expectedException("single path or path array");
				}
			}
			@SuppressWarnings("unchecked")
			List<String> pathList = (List<String>)value;
			return pathList.toArray(new String[pathList.size()]);
		} else {
			throw analyzer.expectedException("single path or path array");
		}
	}
	
}
