package hu.th.dynorgchart.util;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

import javax.servlet.ServletOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilities for stream handling
 * 
 * @author th
 */
public class StreamUtils {

    protected static final Logger logger = LoggerFactory.getLogger(StreamUtils.class);

    /**
     * Silent close a stream, null-safe
     * 
     * @param enc
     *            can be null
     */
    public static void close(Closeable enc) {
        if (enc != null) {
            try {
                enc.close();
            } catch (Exception e) {
                logger.error("Failed closing stream: " + enc, e);
            }
        }
    }

    /**
     * @see org.springframework.util.StreamUtils.copy(byte[] in, OutputStream out) copy
     */
    public static void copy(byte[] data, OutputStream ostream) throws IOException {
        ostream.write(data);
    }

    /**
     * Reads the given inputstream to a string
     * 
     * @param inputStream
     *            to read
     * @param encoding
     *            defaults to UTF-8 if not set
     * @return the string from stream
     * @throws IOException
     */
    public static String readToString(InputStream inputStream, String encoding) throws IOException {
        if (inputStream == null) {
            throw new IOException("Failed reading inputstream: stream is null");
        }
        if (encoding == null) {
            encoding = "UTF-8";
        }
        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, encoding);
        return writer.toString();
    }

    /**
     * Write a byte array to disk to a desired target file. If this file exists then a random number will be added to
     * it's end like the file was img.jpg and it exists then it will write to img43.jpg
     * 
     * @param content
     *            the content to write out
     * @param tempFile
     *            desired target file
     * @return the real file the content was written to
     * @throws IOException
     *             in case of I/O error
     */
    public static File writeByteArrayToDisk(byte[] content, File tempFile) throws IOException {
        FileOutputStream fos = null;
        try {
            Random random = new Random();
            while (tempFile.exists()) {

                tempFile = new File(tempFile.getParentFile(), FilenameUtils.getBaseName(tempFile.getName())
                        + random.nextInt() + "." + FilenameUtils.getExtension(tempFile.getName()));
            }
            fos = new FileOutputStream(tempFile);
            copy(content, fos);
            return tempFile;
        } finally {
            close(fos);
        }
    }

    public static void writeStringToDisk(String htmlString, File tempFile) throws IOException {
        BufferedWriter buf = null;
        OutputStreamWriter fw = null;
        try {
            if (tempFile.isFile() == false) {
                try {
                    tempFile.getParentFile().mkdirs();
                } catch (Exception ed) {
                    // ignore should always have permission for temp folder
                }
                tempFile.createNewFile();
            }
            fw = new OutputStreamWriter(new FileOutputStream(tempFile));
            buf = new BufferedWriter(fw);
            buf.write(htmlString);
        } finally {
            close(buf);
            close(fw);
        }
    }

    public static void write(String data, ServletOutputStream ostream) throws UnsupportedEncodingException, IOException {
        copy(data.getBytes("UTF-8"), ostream);
    }

    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                logger.error("Failed closing database connection", e);
            }
        }
    }

    public static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (Exception e) {
                logger.error("Failed closing statement", e);
            }
        }
    }

    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                logger.error("Failed closing resultset", e);
            }
        }
    }

    public static File writeStreamToFile(InputStream in, File file, boolean overwrite) throws IOException {
        if (file == null || in == null) {
            throw new IOException("Received null file or file stream");
        }
        // check file existence
        int i = 0;
        String baseName = file.getName();
        if (overwrite == true && file.isFile()) {
            boolean delete = FileUtils.deleteQuietly(file);
            if (delete == false) {
                throw new IOException("File could not be deleted for overwrite");
            }
        } else {
            while (file.isFile()) {
                file = new File(file.getParentFile(), FilenameUtils.getBaseName(baseName) + "_" + ++i + "."
                        + FilenameUtils.getExtension(baseName));
            }
        }
        // create folder if not exist
        file.getParentFile().mkdirs();
        // create new file (it should not exist now)
        file.createNewFile();

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            byte[] buffer = new byte[8096];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } finally {
            StreamUtils.close(out);
            StreamUtils.close(in);
        }
        return file;
    }

    public static void close(Writer stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                logger.error("Failed closing stream: " + stream, e);
            }
        }
    }
}