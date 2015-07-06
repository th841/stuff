package hu.th.dynorgchart.servlet;

import hu.th.dynorgchart.data.DataFileReader;
import hu.th.dynorgchart.model.Person;
import hu.th.dynorgchart.util.StreamUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RootsServlet extends BaseServlet {

    private static final long serialVersionUID = -8847771151433210610L;

    @Override
    protected void jsonGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = null;
        try {
            List<Person> roots = DataFileReader.getInstance().getRoots();
            writer = resp.getWriter();
            writer.append(Person.toString(roots));
        } catch (Throwable e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            StreamUtils.close(writer);
        }

    }
}
