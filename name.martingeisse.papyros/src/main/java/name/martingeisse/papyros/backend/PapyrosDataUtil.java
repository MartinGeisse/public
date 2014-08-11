/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.papyros.backend;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.papyros.entity.PreviewDataSet;
import name.martingeisse.papyros.entity.QPreviewDataSet;
import name.martingeisse.papyros.entity.QTemplate;
import name.martingeisse.papyros.entity.QTemplateFamily;
import name.martingeisse.papyros.entity.Template;
import name.martingeisse.papyros.entity.TemplateFamily;
import name.martingeisse.sql.EntityConnectionManager;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.mysema.query.sql.SQLQuery;
import com.mysema.query.support.Expressions;

/**
 * Utility methods used in front-end pages to handle their data.
 */
public class PapyrosDataUtil {

	/**
	 * Loads a template family based on the key from the page parameters.
	 * 
	 * @param pageParameters the page parameters
	 * @return the template
	 */
	public static TemplateFamily loadTemplateFamily(PageParameters pageParameters) {
		String key = pageParameters.get("key").toString("");
		return loadTemplateFamily(key);
	}

	/**
	 * Loads a template family based on the key.
	 * 
	 * @param key the template family key
	 * @return the template
	 */
	public static TemplateFamily loadTemplateFamily(String key) {
		final QTemplateFamily qtf = QTemplateFamily.templateFamily;
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		TemplateFamily templateFamily = query.from(qtf).where(qtf.key.eq(key)).singleResult(qtf);
		if (templateFamily == null) {
			throw new RuntimeException("template family not found; key='" + key + "'");
		}
		return templateFamily;
	}

	/**
	 * Loads a template based on key and language from page parameters.
	 * 
	 * @param pageParameters the page parameters
	 * @return the template
	 */
	public static Template loadTemplate(PageParameters pageParameters) {
		return loadTemplateAndTemplateFamily(pageParameters).getLeft();
	}

	/**
	 * Loads a template based on key and language.
	 * 
	 * @param key the template family key
	 * @param language the language
	 * @return the template
	 */
	public static Template loadTemplate(String key, String language) {
		return loadTemplateAndTemplateFamily(key, language).getLeft();
	}

	/**
	 * Loads a template and its template family based on key and language from page parameters.
	 * 
	 * @param pageParameters the page parameters
	 * @return the template and the template family
	 */
	public static Pair<Template, TemplateFamily> loadTemplateAndTemplateFamily(PageParameters pageParameters) {
		String key = pageParameters.get("key").toString("");
		String language = pageParameters.get("language").toString("");
		return loadTemplateAndTemplateFamily(key, language);
	}

	/**
	 * Loads a template and its template family based on key and language.
	 * 
	 * @param key the template family key
	 * @param language the language
	 * @return the template and the template family
	 */
	public static Pair<Template, TemplateFamily> loadTemplateAndTemplateFamily(String key, String language) {
		final QTemplate qt = QTemplate.template;
		final QTemplateFamily qtf = QTemplateFamily.templateFamily;
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		final Object[] result = query.from(qt, qtf).where(qt.templateFamilyId.eq(qtf.id), qtf.key.eq(key), qt.languageKey.eq(language)).singleResult(qt, qtf);
		if (result == null) {
			throw new RuntimeException("template not found; key='" + key + "'; language='" + language + "'");
		}
		final Template template = (Template)result[0];
		final TemplateFamily templateFamily = (TemplateFamily)result[1];
		return Pair.of(template, templateFamily);
	}

	/**
	 * @param templateFamilyId the ID of the the template family to which the preview data sets belong
	 * @param includeData whether to load the actual preview data. If this flag is false then the data
	 * field will be loaded as null.
	 * @return the list of preview data set
	 */
	public static List<PreviewDataSet> loadPreviewDataSetList(long templateFamilyId, boolean includeData) {
		final QPreviewDataSet qpds = QPreviewDataSet.previewDataSet;
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(qpds).where(qpds.templateFamilyId.eq(templateFamilyId));
		List<PreviewDataSet> result = new ArrayList<>();
		for (Object[] row : query.list(qpds.id, qpds.previewDataSetNumber, qpds.orderIndex, qpds.name, includeData ? qpds.data : Expressions.constant(null))) {
			PreviewDataSet previewDataSet = new PreviewDataSet();
			previewDataSet.setId((long)row[0]);
			previewDataSet.setTemplateFamilyId(templateFamilyId);
			previewDataSet.setPreviewDataSetNumber((int)row[1]);
			previewDataSet.setOrderIndex((int)row[2]);
			previewDataSet.setName((String)row[3]);
			previewDataSet.setData((String)row[4]);
			result.add(previewDataSet);
		}
		return result;
	}

	/**
	 * @param templateFamilyId the ID of the the template family to which the preview data set belongs
	 * @param pageParameters the page parameters object that contains the preview data set number
	 * @return the preview data set
	 */
	public static PreviewDataSet loadPreviewDataSet(long templateFamilyId, PageParameters pageParameters) {
		int previewDataSetNumber = pageParameters.get("previewDataSetNumber").toInt(0);
		return loadPreviewDataSet(templateFamilyId, previewDataSetNumber);
	}

	/**
	 * @param templateFamilyId the ID of the the template family to which the preview data set belongs
	 * @param previewDataSetNumber the preview data set number
	 * @return the preview data set
	 */
	public static PreviewDataSet loadPreviewDataSet(long templateFamilyId, int previewDataSetNumber) {
		final QPreviewDataSet qpds = QPreviewDataSet.previewDataSet;
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		return query.from(qpds).where(qpds.templateFamilyId.eq(templateFamilyId), qpds.previewDataSetNumber.eq(previewDataSetNumber)).singleResult(qpds);
	}
	
}
