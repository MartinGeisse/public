/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.forum.frontend;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import name.martingeisse.forum.application.Constants;
import name.martingeisse.forum.entity.PostBase;
import name.martingeisse.forum.entity.PostFile;
import name.martingeisse.forum.entity.PostText;
import name.martingeisse.forum.model.PostListModel;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.util.resource.AbstractResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.time.Duration;
import org.joda.time.DateTimeUtils;

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
	public ConversationPage(final PageParameters parameters) {
		final long conversationId = parameters.get("id").toLong(0);
		// final IModel<Conversation> conversationModel = new ConversationByIdModel(conversationId);
		final IModel<List<Object[]>> postListModel = new PostListModel(conversationId);
		add(new ListView<Object[]>("posts", postListModel) {
			@Override
			protected void populateItem(final ListItem<Object[]> item) {
				final Object[] data = item.getModelObject();
				final PostBase postBase = (PostBase)data[0];
				item.add(new Label("creationInstant", Constants.uiDateTimeFormatter.print(postBase.getCreationInstant())));
				item.add(new Label("authorName", postBase.getAuthorName()));
				if (data[1] != null) {
					final PostText postText = (PostText)data[1];
					item.add(new Label("body", postText.getText()));
				} else if (data[2] != null) {
					final PostFile postFile = (PostFile)data[2];
					Fragment fragment;
					if (postFile.getContentType().startsWith("image/")) {
						fragment = new Fragment("body", "postImageFragment", ConversationPage.this);
						fragment.add(new Image("image", new DynamicImageResource(postFile.getContentType().substring("image/".length())) {
							@Override
							protected byte[] getImageData(final Attributes attributes) {
								return postFile.getData();
							}
						}));
					} else {
						fragment = new Fragment("body", "postFileFragment", ConversationPage.this);
						MyDownloadLink link = new MyDownloadLink("link", postFile.getId());
						link.add(new Label("filename", postFile.getFilename()));
						fragment.add(link);
						//						fragment.add(new Image("image", new DynamicImageResource(postFile.getContentType().substring("image/".length())) {
						//							@Override
						//							protected byte[] getImageData(Attributes attributes) {
						//								return postFile.getData();
						//							}
						//						}));
					}
					item.add(fragment);
				} else {
					item.add(new Label("body"));
				}
			}
		});
		final Form<?> form = new StatelessForm<Void>("postForm") {
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
					final List<FileUpload> files = filesBeingUploaded;
					filesBeingUploaded = null;
					if (!files.isEmpty()) {
						for (final FileUpload file : files) {
							postFile(conversationId, "me", file.getClientFileName(), file.getContentType(), file.getBytes());
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
	public void setTextBeingPosted(final String textBeingPosted) {
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
	public void setFilesBeingUploaded(final List<FileUpload> filesBeingUploaded) {
		this.filesBeingUploaded = filesBeingUploaded;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.forum.application.page.AbstractApplicationPage#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(final IHeaderResponse response) {
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
	private static long postBaseData(final long conversationId, final String authorName) {
		final PostBase postBase = new PostBase();
		postBase.setConversationId(conversationId);
		postBase.setAuthorName(authorName);
		postBase.setAuthorIdenticonCode(0L);
		postBase.setAuthorIpAddress("127.0.0.1");
		postBase.setCreationInstant(DateTimeUtils.currentTimeMillis());
		postBase.insert();
		return postBase.getId();
	}

	/**
	 * 
	 */
	private static void postText(final long conversationId, final String authorName, final String text) {
		final PostText postText = new PostText();
		postText.setPostBaseId(postBaseData(conversationId, authorName));
		postText.setText(text);
		postText.insert();
	}

	/**
	 * 
	 */
	private static void postFile(final long conversationId, final String authorName, final String filename, final String contentType, final byte[] data) {
		final PostFile postFile = new PostFile();
		postFile.setPostBaseId(postBaseData(conversationId, authorName));
		postFile.setFilename(filename == null ? "file" : filename);
		postFile.setContentType(contentType == null ? "application/octet-stream" : contentType);
		postFile.setData(data);
		postFile.insert();
	}

	/**
	 * 
	 */
	private static class MyDownloadLink extends Link<Long> {

		/**
		 * the postFileId
		 */
		private final long postFileId;

		/**
		 * Constructor.
		 * @param id the wicket id
		 * @param postFileId the ID of the {@link PostFile} to download
		 */
		public MyDownloadLink(final String id, final long postFileId) {
			super(id);
			this.postFileId = postFileId;
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.markup.html.link.Link#onClick()
		 */
		@Override
		public void onClick() {
			final PostFile postFile = PostFile.findById(postFileId);
			if (postFile == null) {
				return;
			}
			final IResourceStream resourceStream = new MyDownloadResourceStream(postFile);
			final ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(resourceStream);
			handler.setFileName(postFile.getFilename());
			handler.setContentDisposition(ContentDisposition.ATTACHMENT);
			handler.setCacheDuration(Duration.MAXIMUM);
			getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
		}

	}

	/**
	 * 
	 */
	private static class MyDownloadResourceStream extends AbstractResourceStream {

		/**
		 * the postFile
		 */
		private final PostFile postFile;

		/**
		 * Constructor.
		 * @param postFile the file to download
		 */
		public MyDownloadResourceStream(final PostFile postFile) {
			this.postFile = postFile;
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.util.resource.IResourceStream#getInputStream()
		 */
		@Override
		public InputStream getInputStream() throws ResourceStreamNotFoundException {
			return new ByteArrayInputStream(postFile.getData());
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.util.resource.IResourceStream#close()
		 */
		@Override
		public void close() throws IOException {
		}

	}

}
