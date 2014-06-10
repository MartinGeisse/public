/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.forum.frontend;

import java.util.List;
import name.martingeisse.forum.entity.PostBase;
import name.martingeisse.forum.entity.PostImage;
import name.martingeisse.forum.entity.PostText;
import name.martingeisse.forum.entity.QPostBase;
import name.martingeisse.forum.model.PostListModel;
import name.martingeisse.sql.EntityConnectionManager;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.DynamicImageResource;
import com.mysema.query.sql.SQLQuery;

/**
 * Simple "Hello World" page for the application template.
 */
public class ConversationPage extends AbstractFrontendPage {

	/**
	 * the textBeingPosted
	 */
	private String textBeingPosted;
	
	/**
	 * the filesBeingUploaded
	 */
	private List<FileUpload> filesBeingUploaded;
	
	/**
	 * Constructor.
	 * @param parameters the page parameters
	 */
	public ConversationPage(PageParameters parameters) {
		final long conversationId = parameters.get("id").toLong(0);
		// final IModel<Conversation> conversationModel = new ConversationByIdModel(conversationId);
		final IModel<List<Object[]>> postListModel = new PostListModel(conversationId);
		add(new ListView<Object[]>("posts", postListModel) {
			@Override
			protected void populateItem(ListItem<Object[]> item) {
				Object[] data = item.getModelObject();
				PostBase postBase = (PostBase)data[0];
				item.add(new Label("authorName", postBase.getAuthorName()));
				if (data[1] != null) {
					PostText postText = (PostText)data[1];
					item.add(new Label("body", postText.getText()));
				} else if (data[2] != null) {
					final PostImage postImage = (PostImage)data[2];
					Fragment fragment = new Fragment("body", "postImageFragment", ConversationPage.this);
					fragment.add(new Image("image", new DynamicImageResource(postImage.getContentType().substring("image/".length())) {
						@Override
						protected byte[] getImageData(Attributes attributes) {
							return postImage.getData();
						}
					}));
					item.add(fragment);
				} else {
					item.add(new Label("body"));
				}
			}
		});
		Form<?> form = new StatelessForm<Void>("postForm") {
			@Override
			protected void onSubmit() {

				// get rid of the ugly post URL
				forceReload();

				// try posting a text message
				if (textBeingPosted != null) {
					String text = textBeingPosted;
					textBeingPosted = null;
					text = text.trim();
					if (!text.isEmpty()) {
						postText(conversationId, "me", text);
						textBeingPosted = null;
						return;
					}
				}
				
				// try uploading a file
				if (filesBeingUploaded != null) {
					List<FileUpload> files = filesBeingUploaded;
					filesBeingUploaded = null;
					if (!files.isEmpty()) {
						for (FileUpload file : files) {
							if (file.getContentType() != null && file.getContentType().startsWith("image/")) {
								postImage(conversationId, "me", file.getContentType(), file.getBytes());
							}
						}
						return;
					}
				}
				
			}
		};
		add(form);
		form.add(new TextArea<>("textarea", new PropertyModel<>(this, "textBeingPosted")));
		form.add(new FileUploadField("uploader", new PropertyModel<List<FileUpload>>(this, "filesBeingUploaded")));

	}
	
	/**
	 * Getter method for the textBeingPosted.
	 * @return the textBeingPosted
	 */
	public String getTextBeingPosted() {
		return textBeingPosted;
	}
	
	/**
	 * Setter method for the textBeingPosted.
	 * @param textBeingPosted the textBeingPosted to set
	 */
	public void setTextBeingPosted(String textBeingPosted) {
		this.textBeingPosted = textBeingPosted;
	}
	
	/**
	 * Getter method for the filesBeingUploaded.
	 * @return the filesBeingUploaded
	 */
	public List<FileUpload> getFilesBeingUploaded() {
		return filesBeingUploaded;
	}
	
	/**
	 * Setter method for the filesBeingUploaded.
	 * @param filesBeingUploaded the filesBeingUploaded to set
	 */
	public void setFilesBeingUploaded(List<FileUpload> filesBeingUploaded) {
		this.filesBeingUploaded = filesBeingUploaded;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.forum.application.page.AbstractApplicationPage#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		// response.render(OnDomReadyHeaderItem.forScript("window.scrollTo(0, document.body.scrollHeight);"));
	}
	
	/**
	 * 
	 */
	private void forceReload() {
		setResponsePage(getClass(), getPageParameters());
	}

	/**
	 * 
	 */
	private static int determineNextOrderIndex(long conversationId) {
		final QPostBase qpb = QPostBase.postBase;
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		return query.from(qpb).where(qpb.conversationId.eq(conversationId)).singleResult(qpb.orderIndex.max()) + 1;
	}
	
	/**
	 * 
	 */
	private static long postBaseData(long conversationId, String authorName) {
		PostBase postBase = new PostBase();
		postBase.setConversationId(conversationId);
		postBase.setOrderIndex(determineNextOrderIndex(conversationId));
		postBase.setAuthorName(authorName);
		postBase.setAuthorIdenticonCode(0L);
		postBase.setAuthorIpAddress("127.0.0.1");
		postBase.insert();
		return postBase.getId();
	}
	
	/**
	 * 
	 */
	private static void postText(long conversationId, String authorName, String text) {
		PostText postText = new PostText();
		postText.setPostBaseId(postBaseData(conversationId, authorName));
		postText.setText(text);
		postText.insert();
	}
	
	/**
	 * 
	 */
	private static void postImage(long conversationId, String authorName, String contentType, byte[] data) {
		PostImage postImage = new PostImage();
		postImage.setPostBaseId(postBaseData(conversationId, authorName));
		postImage.setContentType(contentType);
		postImage.setData(data);
		postImage.insert();
	}
	
}
