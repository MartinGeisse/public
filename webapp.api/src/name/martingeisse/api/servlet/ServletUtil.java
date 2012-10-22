/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Utility methods to deal with the servlet layer.
 */
public class ServletUtil {

	/**
	 * Prepares a plain-text response (text/plain, utf-8). Leaves the status code of the response alone.
	 * @param response the response to write to
	 * @throws IOException on I/O errors
	 */
	public static void preparePlainTextResponse(HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain");
		response.getWriter();
	}

	/**
	 * Prepares an HTML response (text/html, utf-8). Leaves the status code of the response alone.
	 * @param response the response to write to
	 * @throws IOException on I/O errors
	 */
	public static void prepareHtmlResponse(HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		PrintWriter w = response.getWriter();
		w.println("<html><head>");
		w.println("<style type=\"text/css\">");
		w.println("  body {");
		w.println("    font-family: monospace; ");
		w.println("  }");
		w.println("  a {");
		w.println("    color: blue; ");
		w.println("    text-decoration: none; ");
		w.println("  }");
		w.println("  .decorated {");
		w.println("    font-family: serif; ");
		w.println("    color: red; ");
		w.println("  }");
		w.println("</style>");
		w.println("<script type=\"text/javascript\" src=\"/jquery.js\"></script>");
		w.println("</head><body>");
	}
	
	/**
	 * Prepares a plain-text response (text/plain, utf-8), using the specified status code.
	 * @param response the response to write to
	 * @param statusCode the HTTP status code to use
	 * @throws IOException on I/O errors
	 */
	public static void preparePlainTextResponse(HttpServletResponse response, int statusCode) throws IOException {
		response.setStatus(statusCode);
		preparePlainTextResponse(response);
	}
	
	/**
	 * Prepares an HTML response (text/html, utf-8), using the specified status code.
	 * @param response the response to write to
	 * @param statusCode the HTTP status code to use
	 * @throws IOException on I/O errors
	 */
	public static void prepareHtmlResponse(HttpServletResponse response, int statusCode) throws IOException {
		response.setStatus(statusCode);
		prepareHtmlResponse(response);
	}
	
	/**
	 * Finishes a text-based response. This method can be used for any response the uses
	 * the {@link HttpServletResponse}'s {@link Writer}.
	 * @param response the response to write to
	 * @throws IOException on I/O errors
	 */
	public static void finishTextResponse(HttpServletResponse response) throws IOException {
		PrintWriter w = response.getWriter();
		if (response.getContentType().startsWith("text/html")) {
			w.println("</body></html>");
		}
		w.flush();
		w.close();
	}
	
	/**
	 * Writes a response using the specified status code and content, with content type
	 * text/plain (utf-8).
	 * @param response the response to write to
	 * @param statusCode the HTTP status code
	 * @param message the message to write
	 * @throws IOException on I/O errors
	 */
	public static void emitMessageResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
		preparePlainTextResponse(response, statusCode);
		response.getWriter().print(message);
		finishTextResponse(response);
	}

	/**
	 * Writes a 404 response for the specified request.
	 * @param request the request
	 * @param response the response to write to
	 * @throws IOException on I/O errors
	 */
	public static void emitResourceNotFoundResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
		emitMessageResponse(response, 404, "Resource not found: " + request.getRequestURI());
	}

	/**
	 * Writes a 422 (Unprocessable Entity) response to signal well-formed but semantically broken parameters.
	 * @param response the response to write to
	 * @param message the message to write
	 * @throws IOException on I/O errors
	 */
	public static void emitParameterErrorResponse(HttpServletResponse response, String message) throws IOException {
		emitMessageResponse(response, 422, "Parameter error: " + message);
	}

	/**
	 * Writes a 500 response about an internal server error (typically an unexpected exception).
	 * @param response the response to write to
	 * @throws IOException on I/O errors
	 */
	public static void emitInternalServerErrorResponse(HttpServletResponse response) throws IOException {
		emitMessageResponse(response, 500, "Internal server error.");
	}
	
	/**
	 * Prints a line about a handler decorator to the response. Only useful for upgraded HTML
	 * responses.
	 * @param response the response to write to
	 * @param text the text line to print
	 * @throws IOException on I/O errors
	 */
	public static void printDecoratorInfoLine(HttpServletResponse response, String text) throws IOException {
		response.getWriter().println("<div class=\"decorated\">" + text + "</div>");
	}
	
}
