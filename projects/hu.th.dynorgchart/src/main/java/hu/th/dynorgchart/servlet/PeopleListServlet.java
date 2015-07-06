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

public class PeopleListServlet extends BaseServlet {
    private static final long serialVersionUID = -4959643529304134567L;

    @Override
    protected void jsonPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = null;
        try {
            String firstName = req.getParameter("firstName");
            String lastName = req.getParameter("lastName");
            Person person = DataFileReader.getInstance().findPerson(firstName, lastName);
            List<Person> people = person.getPeople();
            writer = resp.getWriter();
            writer.append(Person.toString(people));
        } catch (Throwable e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            StreamUtils.close(writer);
        }
    }
}
