package hu.th.dynorgchart.servlet;

import hu.th.dynorgchart.data.DataFileReader;
import hu.th.dynorgchart.model.Person;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RootsServlet extends BaseServlet {

    private static final long serialVersionUID = -8847771151433210610L;

    @Override
    protected void jsonGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Person> roots = DataFileReader.getInstance().getRoots();
            resp.getWriter().append(roots.toString());
        } catch (Throwable e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
