/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.slave_services.babel.backend;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.slave_services.entity.MessageFamily;
import name.martingeisse.slave_services.entity.MessageTranslation;
import name.martingeisse.slave_services.entity.QMessageFamily;
import name.martingeisse.slave_services.entity.QMessageTranslation;
import name.martingeisse.sql.EntityConnectionManager;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.mysema.query.sql.SQLQuery;

/**
 * Utility methods used in front-end pages to handle their data.
 */
public class BabelDataUtil {

	/**
	 * Loads a message family based on the domain and message key from the page parameters.
	 * 
	 * @param pageParameters the page parameters
	 * @return the message family
	 */
	public static MessageFamily loadMessageFamily(PageParameters pageParameters) {
		String domain = pageParameters.get("domain").toString("");
		String messageKey = pageParameters.get("messageKey").toString("");
		return loadMessageFamily(domain, messageKey);
	}

	/**
	 * Loads a message family based on the domain and message key.
	 * 
	 * @param domain the domain
	 * @param messageKey the message key
	 * @return the message family
	 */
	public static MessageFamily loadMessageFamily(String domain, String messageKey) {
		final QMessageFamily qmf = QMessageFamily.messageFamily;
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		MessageFamily messageFamily = query.from(qmf).where(qmf.domain.eq(domain), qmf.messageKey.eq(messageKey)).singleResult(qmf);
		if (messageFamily == null) {
			throw new RuntimeException("message family not found; domain = '" + domain + "', message key = '" + messageKey + "'");
		}
		return messageFamily;
	}

	/**
	 * Loads a message translation based on domain, message key and language key from the page parameters.
	 * 
	 * @param pageParameters the page parameters
	 * @return the message translation
	 */
	public static MessageTranslation loadMessageTranslation(PageParameters pageParameters) {
		return loadMessageFamilyAndMessageTranslation(pageParameters).getRight();
	}

	/**
	 * Loads a message translation based on domain, message key and language key.
	 * 
	 * @param domain the domain
	 * @param messageKey the message key
	 * @param languageKey the language key
	 * @return the message translation
	 */
	public static MessageTranslation loadMessageTranslation(String domain, String messageKey, String languageKey) {
		return loadMessageFamilyAndMessageTranslation(domain, messageKey, languageKey).getRight();
	}

	/**
	 * Loads a message translation and its message family based on domain, message key and language key from the page parameters.
	 * 
	 * @param pageParameters the page parameters
	 * @return the message family and message translation
	 */
	public static Pair<MessageFamily, MessageTranslation> loadMessageFamilyAndMessageTranslation(PageParameters pageParameters) {
		String domain = pageParameters.get("domain").toString("");
		String messageKey = pageParameters.get("messageKey").toString("");
		String languageKey = pageParameters.get("languageKey").toString("");
		return loadMessageFamilyAndMessageTranslation(domain, messageKey, languageKey);
	}

	/**
	 * Loads a message and its message family based on key and language.
	 * 
	 * @param domain the domain
	 * @param messageKey the message key
	 * @param languageKey the language key
	 * @return the message family and message translation
	 */
	public static Pair<MessageFamily, MessageTranslation> loadMessageFamilyAndMessageTranslation(String domain, String messageKey, String languageKey) {
		final QMessageFamily qmf = QMessageFamily.messageFamily;
		final QMessageTranslation qmt = QMessageTranslation.messageTranslation;
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(qmf, qmt).where(qmt.messageFamilyId.eq(qmf.id));
		query.where(qmf.domain.eq(domain), qmf.messageKey.eq(messageKey), qmt.languageKey.eq(languageKey));
		final Object[] result = query.singleResult(qmt, qmf);
		if (result == null) {
			throw new RuntimeException("message not found; domain = '" + domain + "', message key = '" + messageKey + "', language key = '" + languageKey + "'");
		}
		final MessageFamily messageFamily = (MessageFamily)result[0];
		final MessageTranslation messageTranslation = (MessageTranslation)result[1];
		return Pair.of(messageFamily, messageTranslation);
	}
	
	/**
	 * Loads all message families for a specific domain as well as translations for a specific language.
	 * 
	 * This method takes a flag that tells it whether message families without a translation for
	 * the specified language shall be included or not.
	 * 
	 * @param domain the domain
	 * @param languageKey the language key
	 * @param includeMissing if true, message families without a translation for the specified
	 * language will be included in the result, with the {@link MessageTranslation} part being
	 * null. If false, such message families will be omitted from the result.
	 * @return the message families and translations
	 */
	public static List<Pair<MessageFamily, MessageTranslation>> loadDomainTranslations(String domain, String languageKey, boolean includeMissing) {
		final QMessageFamily qmf = QMessageFamily.messageFamily;
		final QMessageTranslation qmt = QMessageTranslation.messageTranslation;
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		if (includeMissing) {
			query.from(qmf).leftJoin(qmt).on(qmt.messageFamilyId.eq(qmf.id));
		} else {
			query.from(qmf).innerJoin(qmt).on(qmt.messageFamilyId.eq(qmf.id));
		}
		query.where(qmf.domain.eq(domain), qmt.languageKey.eq(languageKey));
		List<Pair<MessageFamily, MessageTranslation>> result = new ArrayList<>();
		for (Object[] row : query.list(qmf, qmt)) {
			final MessageFamily messageFamily = (MessageFamily)row[0];
			final MessageTranslation messageTranslation = (MessageTranslation)row[1];
			result.add(Pair.of(messageFamily, messageTranslation));
		}
		return result;
	}

}
