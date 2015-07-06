package hu.th.dynorgchart.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class Person {

    private String title;
    private String firstName;
    private String lastName;
    private String department;
    private String manager;
    private String team;
    private String function;
    private String office;

    private List<Person> people = new ArrayList<Person>();

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Person other = (Person) obj;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        return true;
    }

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }

    @Override
    public String toString() {
        return "Person [firstName=" + firstName + ", lastName=" + lastName + ", hasPeople=" + (people.size() > 0) + "]";
    }

    /**
     * Returns json compatible person data as string
     * 
     * @return json string
     */
    public String toJsonString() {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append(JSONObject.quote("title"));
        sb.append(":");
        sb.append(JSONObject.quote(title));
        sb.append(",");

        sb.append(JSONObject.quote("firstName"));
        sb.append(":");
        sb.append(JSONObject.quote(firstName));
        sb.append(",");

        sb.append(JSONObject.quote("lastName"));
        sb.append(":");
        sb.append(JSONObject.quote(lastName));
        sb.append(",");

        sb.append(JSONObject.quote("department"));
        sb.append(":");
        sb.append(JSONObject.quote(department));
        sb.append(",");

        sb.append(JSONObject.quote("manager"));
        sb.append(":");
        sb.append(JSONObject.quote(manager));
        sb.append(",");

        sb.append(JSONObject.quote("team"));
        sb.append(":");
        sb.append(JSONObject.quote(team));
        sb.append(",");

        sb.append(JSONObject.quote("function"));
        sb.append(":");
        sb.append(JSONObject.quote(function));
        sb.append(",");

        sb.append(JSONObject.quote("office"));
        sb.append(":");
        sb.append(JSONObject.quote(office));
        sb.append(",");

        sb.append(JSONObject.quote("hasPeople"));
        sb.append(":");
        sb.append(Boolean.toString(people.size() > 0));

        sb.append("}");

        return sb.toString();
    }

    public static String toString(List<Person> people) {
        StringBuffer writer = new StringBuffer();
        writer.append("[");
        for (Person person2 : people) {
            writer.append(person2.toJsonString());
            if (people.indexOf(person2) < people.size() - 1) {
                writer.append(",");
            }
        }
        writer.append("]");
        return writer.toString();
    }
}
