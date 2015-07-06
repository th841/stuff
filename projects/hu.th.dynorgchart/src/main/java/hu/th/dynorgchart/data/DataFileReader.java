package hu.th.dynorgchart.data;

import hu.th.dynorgchart.model.Person;
import hu.th.dynorgchart.util.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * DataFileReader singleton class, for data access
 * 
 * @author th
 */
public class DataFileReader {

    private volatile static DataFileReader instance;
    private volatile List<Person> roots = new ArrayList<Person>();

    private DataFileReader() {

    }

    /**
     * Return the DataFileReader instance
     * 
     * @return dataFileReader instance
     */
    public static DataFileReader getInstance() {
        if (instance == null) {
            synchronized (DataFileReader.class) {
                if (instance == null) {
                    instance = new DataFileReader();
                }
            }
        }
        return instance;
    }

    /**
     * Process data file, read and extract data
     * 
     * @param dataFile
     *            json file from where we read the data
     * @throws IOException
     *             if the file is not found or cannot be read
     * @throws JSONException
     *             if data cannot be parsed to json
     */
    public void processDataFile(File dataFile) throws IOException, JSONException {
        if (dataFile.isFile() == true) {
            if (dataFile.canRead() == true) {
                FileInputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(dataFile);
                    String dataFileContent = StreamUtils.readToString(inputStream, "UTF-8");
                    processJsonData(dataFileContent);
                } finally {
                    StreamUtils.close(inputStream);
                }
            } else {
                throw new IOException("Could not read data file [" + dataFile.getAbsolutePath() + "]. No permission.");
            }
        } else {
            throw new IOException("Could not read data file [" + dataFile.getAbsolutePath() + "]. File not found.");
        }
    }

    public void processJsonData(String jsonData) throws JSONException {
        if (jsonData == null || jsonData.isEmpty()) {
            System.out.println("Received empty data.");
            return;
        }
        jsonData = jsonData.trim();
        // we just care with json array now
        JSONArray data = new JSONArray(jsonData);
        for (int i = 0; i < data.length(); i++) {
            JSONObject object = data.getJSONObject(i);
            Person person = new Person((String) object.get("first name"), (String) object.get("last name"));
            person.setDepartment(object.getString("department"));
            person.setFunction(object.getString("function"));
            person.setManager(object.getString("manager"));
            person.setOffice(object.getString("office"));
            person.setTeam(object.getString("team"));
            person.setTitle(object.getString("title"));
            String managerString = person.getManager();
            if (managerString != null && managerString.isEmpty() == false) {
                StringTokenizer st = new StringTokenizer(managerString, " ");
                String firstName = st.nextToken();
                String lastName = st.nextToken();
                Person manager = new Person(firstName, lastName);
                Person managerInList = findInList(roots, manager);
                if (managerInList == null) {
                    manager.getPeople().add(person);
                    roots.add(manager);
                } else {
                    managerInList.getPeople().add(person);
                }
            }
        }
    }

    public Person findInList(List<Person> list, Person person) {
        if (list.contains(person)) {
            return list.get(list.indexOf(person));
        }
        for (Person manager : list) {
            List<Person> people = manager.getPeople();
            return findInList(people, person);
        }
        return null;
    }

    /**
     * Returns the root people, who has no manager set
     * 
     * @return root people
     */
    public List<Person> getRoots() {
        return this.roots;
    }

    /**
     * Returns the real person object if it exists in the dataBASE, null if not.
     * 
     * @param firstName
     *            first name of the person
     * @param lastName
     *            last name of the person
     * @return the person object from database, null if not existent
     */
    public synchronized Person findPerson(String firstName, String lastName) {
        Person person = new Person(firstName, lastName);
        return findInList(roots, person);
    }
}
