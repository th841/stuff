package hu.th.dynorgchart.servlet;

import hu.th.dynorgchart.util.StreamUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;

/**
 * File servlet for serving static content
 * 
 * @author th
 */
public class FileServlet extends HttpServlet {

    private static final long serialVersionUID = -4373608333802584996L;
    private static final int BUFFER_SIZE = 2048;
    static final String FILEPATH = "/resources/html/";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestedFile = request.getPathInfo();

        if (request.getRequestURL().toString().endsWith(getServletContext().getContextPath())) {
            response.sendRedirect(request.getRequestURL().toString() + "/");
            return;
        }
        if (requestedFile == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } else if ("/".equals(requestedFile) == true) {
            requestedFile += "index.html";
        }
        // find file
        String pathPrefix = getServletContext().getRealPath("");

        String pathEnd = FilenameUtils.separatorsToSystem(FILEPATH);
        File file = new File(pathPrefix + pathEnd, URLDecoder.decode(requestedFile, "UTF-8"));

        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentType = null;
        String mimeType = fileNameMap.getContentTypeFor(file.getName());
        if (mimeType != null && mimeType.isEmpty() == false) {
            contentType = mimeType;
        } else {
            contentType = getServletContext().getMimeType(file.getName());
        }

        response.reset();
        response.setBufferSize(BUFFER_SIZE);
        if (contentType != null) {
            response.setContentType(contentType);
        }
        response.setHeader("Cache-Control", "max-age=604800");
        response.setHeader("Content-Length", String.valueOf(file.length()));

        BufferedInputStream input = null;
        BufferedOutputStream output = null;

        try {
            if (file.isFile()) {
                input = new BufferedInputStream(new FileInputStream(file), BUFFER_SIZE);
                output = new BufferedOutputStream(response.getOutputStream(), BUFFER_SIZE);
                byte[] buffer = new byte[BUFFER_SIZE];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        } finally {
            StreamUtils.close(output);
            StreamUtils.close(input);
        }
    }

}
