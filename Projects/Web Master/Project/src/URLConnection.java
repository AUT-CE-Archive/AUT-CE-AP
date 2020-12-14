/**
 * Imports
 */
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.imageio.ImageIO;

/**
 * Apache libraries for PATCH and stuff, you know...
 */
import org.apache.commons.io.FileUtils;

/***
 * URL Connection
 * @author Keivan Ipchi Hagh
 * @version 1.0.0
 */
public class URLConnection {

    /**
     * As always, variables, objects, fields, bluh bluh bluh
     */
    private static List<String> workspaces;
    private static Scanner scanner = new Scanner(System.in);
    private static String responseContent, responseCode, responseLength, responseTime;
    private static Map<String, List<String>> responseHeaders;

    /**
     * I like this one; you can connect GUI to this, or simply use this on its own. Works either way
     * @param args Arguments (IMPORTANT!!!!!!!!)
     * @throws Exception Well, you know...
     */
    public static void main(String[] args) throws Exception {

        if (args.length > 4) {
            String temp = "";
            for (String str: args)
                temp += str + " ";
            args[0] = temp;
        }

        workspaces = new ArrayList<>();

        // Create Workspace Folder if does not exist
        if (!new File("Data//WorkSpaces.txt").isFile()) {
            File dir = new File("Data");
            dir.mkdirs();

            File records = new File("Data//WorkSpaces.txt"); // Create new file if does not already exist
            try {
                FileWriter writer = new FileWriter("Data//WorkSpaces.txt", true);
                writer.append("root");
                writer.close();
            } catch (Exception ex) {
                ExceptionHandler.handle(4);
            }
        } else {
            try {
                File fileReader = new File("Data//WorkSpaces.txt");
                Scanner scanner = new Scanner(fileReader);

                while (scanner.hasNextLine())
                    workspaces.add(scanner.nextLine());

            } catch (Exception ex) {
                ExceptionHandler.handle(8);
            }
        }

        System.out.print("Enter Request: ");
        String request;
        try {
            if (args[0] == null)
                request = scanner.nextLine();
            else {
                request = args[0];
                System.out.println(request);
            }
        } catch (Exception ex) {
            request = scanner.nextLine();
        }

        if (request.contains("curl fire")) {
            List<Integer> indexes = new ArrayList<>();
            String[] arr = request.split(" ");
            for (int i = 3; i < arr.length; i++)
                indexes.add(Integer.parseInt(arr[i]));
            fire(arr[2], indexes);

            //SendRecords(readJSON(), request.split(" ")[2], indexes);
        } else if (request.contains("curl create")) {    // Create WorkSpaces
            try {
                FileWriter writer = new FileWriter("Data//WorkSpaces.txt", true);
                writer.append("\n" + request.split(" ")[2]);
                writer.close();
                System.out.println("Workspace successfully created.");
            } catch (Exception ex) {
                ExceptionHandler.handle(9);
            }
        } else if (request.equals("curl list")) {    // Print Saved WorkSpaces
            System.out.println("Workspaces are as followed (" + workspaces.size() + " total):");
            for (int i = 0; i < workspaces.size(); i++)
                System.out.println((i + 1) + ": " + workspaces.get(i));

        } else if (request.contains("curl list"))     // Print Saved Records For Specific WorkSpace
            printRecords(Objects.requireNonNull(readJSON()), request.split(" ")[2]);
        else {
            // Format and Pre-processes
            request = request.replace("--method", "-M");
            request = request.replace("--headers", "-H");
            request = request.replace("--data", "-d");
            request = request.replace("-save", "-s");
            request = request.replace("-S", "-s");
            request = request.replace("--json", "-j");
            request = request.replace("-O", "-o");
            request = request.replace("--output", "-o");

            List<String> requestPhrases = Arrays.asList(request.split(" "));

            // Output name
            if (request.contains("-o") && requestPhrases.size() == requestPhrases.indexOf("-o") + 1)
                request = request.substring(0, request.indexOf("-o") + 2) + " \"" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime()) + ".txt\"" + request.substring(request.indexOf("-o") + 2);

            if (requestPhrases.get(0).equals("curl")) {

                requestPhrases = Arrays.asList(request.split(" "));

                // Get Method
                String method = null;
                if (!(request.contains("--method") || request.contains("-M"))) {
                    request += " -M GET";    // Format string
                    requestPhrases = Arrays.asList(request.split(" "));
                }
                method = requestPhrases.get(requestPhrases.indexOf("-M") + 1);

                // Get Auths
                HashMap<Object, Object> authMap = new HashMap<>();
                try { authMap.put(args[1], args[2]); } catch (Exception ex) { }

                // Get Headers
                HashMap<Object, Object> headerMap = new HashMap<>();
                if (request.contains("--headers") || request.contains("-H"))
                    for (String header : Arrays.asList(requestPhrases.get(requestPhrases.indexOf("-H") + 1).split(";")))
                        headerMap.put(header.split(":")[0].replace("\"", ""), header.split(":")[1].replace("\"", ""));

                // Get JSON
                JSONObject JSON = new JSONObject();
                if (request.contains("-j") || request.contains("--json"))
                    for (String temp: requestPhrases.get(requestPhrases.indexOf("-j") + 1).replaceAll("\\{", "").replaceAll("}", "").replaceAll("\"", "").split(","))
                        JSON.put(temp.split(":")[0], temp.split(":")[1]);

                // Get Data
                HashMap<Object, Object> dataMap = new HashMap<>();
                if (request.contains("--data") || request.contains("-d"))
                    for (String data : Arrays.asList(requestPhrases.get(requestPhrases.indexOf("-d") + 1).split("&")))
                        dataMap.put(data.split("=")[0].replace("\"", ""), data.split("=")[1].replace("\"", ""));

                String formData = "";
                if (requestPhrases.contains("-d"))
                    formData = requestPhrases.get(requestPhrases.indexOf("-d") + 1);

                if (request.contains("-S") || request.contains("-save") || request.contains("-s")) {
                    // Get workspace
                    String workSpace = "root";
                    try {
                        if (request.contains("-s"))
                            workSpace = requestPhrases.get(requestPhrases.indexOf("-s") + 1);
                    } catch (Exception ignored) { }

                    writeJSON(requestPhrases.get(1), method, headerMap, dataMap, JSON, request.contains("-i"), request.contains("-h") || request.contains("-help"), request.contains("-f"), workSpace, request);   // Write JSON Formatted File
                }

                // Upload file
                String filePath = "";
                try { filePath = args[3]; } catch (Exception ignored) { };

                // If both JSON & Form Data are used
                if (request.contains("-j") && request.contains("-d"))
                    ExceptionHandler.handle(13);

                // Start a thread, assign and run
                cURL_Thread cURLThread = new cURL_Thread();
                cURLThread.start(requestPhrases.get(1), method, headerMap, authMap, formData, JSON, request.contains("-f"), filePath);
                responseCode = cURLThread.getResponseCode();
                responseContent = cURLThread.getResponseContent();
                responseHeaders = cURLThread.getResponseHeaders();
                responseTime = cURLThread.getResponseTime();
                try { responseLength = cURLThread.getResponseLength(); } catch (Exception ex) { responseLength = "0"; }
                //sendRequest(requestPhrases.get(1), method, headerMap, authMap, formData, JSON, request.contains("-f"));

                // Save To File?
                String saveName = null;
                if (request.contains("-o")) {
                    saveName = requestPhrases.get(requestPhrases.indexOf("-o") + 1);
                    saveToFile(saveName, responseContent, requestPhrases.get(1));
                }

            } else { ExceptionHandler.handle(6); }
        }
    }

    /**
     * Pretty useless right now cause we have multi-threading. But in case I'm asked to do this in a single-thread, this comes in handy (;
     * @param url   URL
     * @param method    Method
     * @param headers   Map of Headers
     * @param auth  Map of Authentications
     * @param formData  String of Data
     * @param JSON  Object of JSON
     * @param followRedirect    Boolean
     * @throws IOException  Again...
     */
    private static void sendRequest(String url, String method, HashMap<Object, Object> headers, HashMap<Object, Object> auth, String formData, JSONObject JSON, boolean followRedirect) throws IOException {

        // Set Connection & URL + Query
        if (!url.contains("https") && !url.contains("http"))
            url = "https://" + url + "/";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

        // Set Method - First
        connection.setRequestMethod(method);

        // Set Headers - Second
        for (Map.Entry<Object, Object> entry : headers.entrySet())
            connection.setRequestProperty(entry.getKey().toString(), entry.getValue().toString());

        // Set Authentication
        for (Map.Entry<Object, Object> entry : auth.entrySet())
            connection.setRequestProperty("Authorization", "Basic " + new String(Base64.getEncoder().encode((entry.getKey() + ":" + entry.getValue()).getBytes())));

        // Set Follow Redirect
        connection.setInstanceFollowRedirects(followRedirect);

        // Set Additional Info
        connection.setDoOutput(!formData.equals("") || JSON != null);

        // Set JSON or FormData
        if (JSON.size() != 0) {
            // Avoid UTF-8 problems
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");

            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            wr.write(JSON.toString());
            wr.close();
        } else { // Add Form Data
            try (DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
                dos.writeBytes(formData);
            } catch (Exception ex) {
                ExceptionHandler.handle(12);
                ex.printStackTrace();
            }
        }

        // Send Request
        long startTime = System.nanoTime();
        Reader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        long stopTime = System.nanoTime();

        // Write Request
        responseContent = "";
        for (int c; (c = in.read()) >= 0; ) {
            System.out.print((char) c);
            responseContent += (char) c;
        }

        // Set Details
        responseCode = connection.getResponseCode() + " " + connection.getResponseMessage();
        responseLength = String.valueOf(connection.getContentLength());
        responseTime = String.valueOf((stopTime - startTime) / Math.pow(10, 9));
        responseHeaders = connection.getHeaderFields();

        printResults();

        // Fetch & Print Returned Headers
        HashMap<String, String> returnedHeaders = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : connection.getHeaderFields().entrySet()) {
            returnedHeaders.put(entry.getKey(), entry.getValue().get(0));
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + entry.getKey() + ": " + ConsoleColors.GREEN_BOLD_BRIGHT + entry.getValue().get(0) + ConsoleColors.RESET);
        }
    }

    /**
     * Prints the good looking outputs and responses
     */
    private static void printResults() {
        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Connection Status: " + ConsoleColors.GREEN_BOLD_BRIGHT + responseCode + ConsoleColors.RESET);
        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Response Size: " + ConsoleColors.GREEN_BOLD_BRIGHT + responseLength + ConsoleColors.RESET);
        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Elapse Time: " + ConsoleColors.GREEN_BOLD_BRIGHT + responseTime + ConsoleColors.RESET + "\n");
    }

    /**
     * This dude here, roars like a lion. No kidding
     * @param workspace Workspace name
     * @param items Items to fire
     */
    private static void fire(String workspace, List<Integer> items) {
        List<JSONObject> objects = readJSON();

        int counter = 0;
        for (JSONObject object: objects) {
            if (object.get("workspace").equals(workspace)) {

                if (items.contains(counter)) {
                    System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Firing request for: " + object.get("url") + ConsoleColors.RESET);
                    cURL_Thread cURLThread = new cURL_Thread();
                    cURLThread.start(object.get("url").toString(), object.get("-m").toString(), (HashMap<Object, Object>) object.get("-H"), new HashMap<Object, Object>(), object.get("-d").toString(), (JSONObject) object.get("-j"), object.get("-i").equals("true"), "");
                    System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------" + ConsoleColors.RESET);
                }
                counter++;
            }
        }
    }

    /**
     * Writes the data into a text file using JSON Object. why? Cause it's easier bruh
     * @param url   URL
     * @param method    method
     * @param headersMap    headers map
     * @param dataMap   data string
     * @param JSON  JSON object
     * @param showHeadersInResponse no use at the moment
     * @param showExtended  still...
     * @param followRedirect    oh this actually works YEY!
     * @param workspace workspace name
     * @param commandString why bother? save the entire thing here duh
     */
    public static void writeJSON(String url, String method, HashMap<Object, Object> headersMap, HashMap<Object, Object> dataMap, JSONObject JSON, boolean showHeadersInResponse, boolean showExtended, boolean followRedirect, String workspace, String commandString) {

        try {
            // Create JSON Object
            JSONObject json = new JSONObject();

            // Format content to adjust GUI algorithms
            for (Map.Entry<Object, Object> map: headersMap.entrySet())
                map.setValue(map.getValue() + " | ON");
            for (Map.Entry<Object, Object> map: dataMap.entrySet())
                map.setValue(map.getValue() + " | ON");

            JSONObject newJSON = new JSONObject();
            for (Object entry: JSON.entrySet()) {
                String temp = entry.toString() + " | ON";
                newJSON.put(temp.split("=")[0], temp.split("=")[1]);
            }

            // Import URL, Method
            json.put("url", url);
            json.put("-m", method);

            // Import -i
            json.put("-i", showHeadersInResponse);

            // Import Help
            json.put("-h", showExtended);

            // Import Follow Redirect
            json.put("-f", followRedirect);

            // Import JSON
            json.put("-j", newJSON);

            // Import WorkSpace
            json.put("workspace", workspace);

            // Import Command
            json.put("commandString", commandString);

            // Import Headers
            json.put("-H", headersMap);

            // Import Body
            json.put("-d", dataMap);

            File dir = new File("Data");
            dir.mkdirs();

            File records = new File("Data//Records.txt"); // Create new file if does not already exist
            FileWriter writer = new FileWriter("Data//Records.txt", true);

            writer.append(json.toJSONString() + " \n");

            writer.close();
        } catch (Exception ex) {
            ExceptionHandler.handle(7);
        }
    }

    /**
     * It reads all the records in the data file
     * @return  All JSON objects
     */
    public static List<JSONObject> readJSON() {

        try {
            File fileReader = new File("Data//Records.txt");
            Scanner scanner = new Scanner(fileReader);
            List<JSONObject> jsonObjects = new ArrayList<>();

            while (scanner.hasNextLine())
                jsonObjects.add((JSONObject) new JSONParser().parse(scanner.nextLine()));

            return jsonObjects;

        } catch (Exception ex) {
            ExceptionHandler.handle(8);
        }

        return null;
    }

    /**
     * Prints the records, obvious as hell
     * @param jsonObjects   List of JSON objects
     * @param workspace workspace name
     */
    private static void printRecords(List<JSONObject> jsonObjects, String workspace) {
        int counter = 1;
        for (JSONObject object : jsonObjects) {

            if (object.get("workspace").equals(workspace)) {
                StringBuilder output = new StringBuilder();
                // URL
                output.append(counter++).append(". URL: ").append(object.get("url")).append(" | ");

                // Method
                output.append("Method: ").append(object.get("-m")).append(" | ");

                // Headers
                try {
                    StringBuilder temp = new StringBuilder("");
                    for (Map.Entry pair : (Iterable<Map.Entry>) ((Map) object.get("-H")).entrySet())
                        temp.append(" ").append(pair.getKey()).append(": ").append(pair.getValue()).append(";");
                    output.append("Headers:").append(temp).append(" | ");
                } catch (Exception ignored) {
                }

                // Body
                try {
                    StringBuilder temp = new StringBuilder("");
                    for (Map.Entry pair : (Iterable<Map.Entry>) ((Map) object.get("-d")).entrySet())
                        temp.append(" ").append(pair.getKey()).append(": ").append(pair.getValue()).append(";");
                    output.append("Body:").append(temp).append(" | ");
                } catch (Exception ignored) { }

                // JSON
                try {
                    StringBuilder temp = new StringBuilder("");
                    for (Map.Entry pair : (Iterable<Map.Entry>) ((Map) object.get("-j")).entrySet())
                        temp.append(" ").append(pair.getKey()).append(": ").append(pair.getValue()).append(",");
                    output.append("JSON: {").append(temp).append("} | ");
                } catch (Exception ex) { ex.printStackTrace(); }

                // Follow Redirect
                output.append("Follow Redirect: ").append(object.get("-f")).append(" | ");

                // Show Extended
                output.append("Show Extended: ").append(object.get("-h")).append(" | ");

                // Show Headers In Response
                output.append("Show Headers In Response: ").append(object.get("-i")).append(" | ");

                // Show Headers In Response
                output.append("WorkSpace: ").append(object.get("workspace"));

                System.out.println(output.toString().replaceAll("| ON;", "").replaceAll("| OFF;", "")); // Show Formatted String Builder
            }
        }
    }

    /**
     * Saves the file to the file regardless of its type. No wait, it matters (:
     * @param name  file name
     * @param response  content
     * @param URL   URL
     */
    private static void saveToFile(String name, String response, String URL) {
        try {
            if (name.contains(".jpg") || name.contains(".png") || name.contains(".jpeg") || name.contains(".btm")) {
                URL url = new URL(URL);
                BufferedImage image = ImageIO.read(url);
                ImageIO.write(image, "jpg", new File(name));
            } else {
                FileWriter myWriter = new FileWriter(name.replaceAll("\"", ""));
                myWriter.write(response);
                myWriter.close();
            }
        } catch (Exception ex) { ex.printStackTrace(); ExceptionHandler.handle(11); }
    }

    /**
     * Getter
     * @return
     */
    public static String getResponseCode() { return responseCode; }

    /**
     * Getter
     * @return
     */
    public static String getResponseLength() {
        if (Integer.parseInt(responseLength) < 1000)
            return responseLength + " B";
        else if (Integer.parseInt(responseLength) < 1000000)
            return responseLength + " KB";
        else
            return responseLength + " MB";
    }

    /**
     * Getter
     * @return
     */
    public static String getResponseTime() { return responseTime; }

    /**
     * Getter
     * @return
     */
    public static String getResponseContent() { return responseContent; }

    /**
     * Getter
     * @return
     */
    public static HashMap<String, String> getResponseHeaders() {
        HashMap<String, String> temp = new HashMap<>();

        for (Map.Entry<String, List<String>> entry: responseHeaders.entrySet())
            try { temp.put(entry.getKey(), entry.getValue().get(0)); } catch (Exception ex) { temp.put("Err 404", "Header Not Found"); }
        return temp;
    }
}

/**
 * well, here, multi-threading. simpel huh?
 */
class cURL_Thread implements Runnable {

    /**
     * Variables, fields, objs
     */
    private String url, method, formData, responseContent, responseCode, responseLength, responseTime, filePath;
    private HashMap<Object, Object> headers, auth;
    private JSONObject JSON;
    private Map<String, List<String>> responseHeaders;
    private boolean followRedirect;
    long startTime;

    /**
     * Does the job, I mean all of it. though it's buggy sometimes and can't figure out why...
     */
    public void run() {

        if (method.equals("PATCH")) {
            try {

                // Set Headers - Second
                HashMap<Object, Object> temp = new HashMap<>();
                // Set Headers - Second
                for (Map.Entry<Object, Object> entry : headers.entrySet())
                    if (entry.getValue().toString().contains("multipart/form-data"))
                        temp.put(entry.getKey().toString(), entry.getValue().toString().replace(" | ON", "") + ";boundary=" + "===" + System.currentTimeMillis() + "===");
                    else
                        temp.put(entry.getKey().toString(), entry.getValue().toString().replace(" | ON", ""));

                responseCode = String.valueOf(PATCH.sendPATCH(url, formData, temp));
                responseLength = PATCH.getResponseLength();
                responseTime = PATCH.getResponseTime();
                responseContent = PATCH.getResponseContent();
            } catch (Exception ex) {
                ex.printStackTrace();
                responseCode = "400 Bad Request";
                responseLength = PATCH.getResponseLength();
                responseTime = "0";
                responseContent = "-1";
            }

            printResults();
            return;
        }

        HttpURLConnection connection = null;

        try {

            // Set Connection & URL + Query
            if (!url.contains("https") && !url.contains("http"))
                url = "https://" + url + "/";
            connection = (HttpURLConnection) new URL(url).openConnection();

            // Set Method - First
            connection.setRequestMethod(method);

            // Set Headers - Second
            for (Map.Entry<Object, Object> entry : headers.entrySet())
                if (entry.getValue().toString().contains("multipart/form-data"))
                        connection.setRequestProperty(entry.getKey().toString(), entry.getValue().toString().replace(" | ON", "") + ";boundary=" + "===" + System.currentTimeMillis() + "===");
                else
                    connection.setRequestProperty(entry.getKey().toString(), entry.getValue().toString().replace(" | ON", ""));

            // Set Authentication
            for (Map.Entry<Object, Object> entry : auth.entrySet())
                connection.setRequestProperty("Authorization", "Basic " + new String(Base64.getEncoder().encode((entry.getKey() + ":" + entry.getValue()).getBytes())));

            // Set Follow Redirect
            connection.setInstanceFollowRedirects(followRedirect);

            // Set Additional Info
            connection.setDoOutput(!formData.equals("") || JSON != null);

            // Set JSON or FormData or Upload file
            if (!filePath.equals("")) {
                String boundary = UUID.randomUUID().toString();
                DataOutputStream request = new DataOutputStream(connection.getOutputStream());

                request.writeBytes("--" + boundary + "\r\n");
                request.writeBytes("Content-Disposition: form-data; name=\"description\"\r\n\r\n");

                request.writeBytes("--" + boundary + "\r\n");
                request.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + filePath + "\"\r\n\r\n");
                request.write(FileUtils.readFileToByteArray(new File(filePath)));
                request.writeBytes("\r\n");

                request.writeBytes("--" + boundary + "--\r\n");
                request.flush();
            } else if (JSON.size() != 0) {  // Add JSON
                OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                wr.write(JSON.toJSONString());
                wr.close();
            } else if (!formData.equals("")) { // Add Form Data
                try (DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
                    dos.writeBytes(formData);
                } catch (Exception ex) {
                    ExceptionHandler.handle(12);
                    ex.printStackTrace();
                }
            }

            // Set Details
            responseLength = String.valueOf(connection.getContentLength());
            responseHeaders = connection.getHeaderFields();
            responseCode = connection.getResponseCode() + " " + connection.getResponseMessage();

            // Send Request
            startTime = System.nanoTime();
            Reader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            // Write Request
            responseContent = "";
            for (int c; (c = in.read()) >= 0; ) {
                System.out.print((char) c);
                responseContent += (char) c;
            }

            // Fetch & Print Returned Headers
            System.out.println();
            HashMap<String, String> returnedHeaders = new HashMap<>();
            for (Map.Entry<String, List<String>> entry : connection.getHeaderFields().entrySet()) {
                returnedHeaders.put(entry.getKey(), entry.getValue().get(0));
                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + entry.getKey() + ": " + ConsoleColors.GREEN_BOLD_BRIGHT + entry.getValue().get(0) + ConsoleColors.RESET);
            }
        } catch (IOException ex) {
            //ExceptionHandler.handle(17);

            try {
                Reader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

                // Write Request
                responseContent = "";
                for (int c; (c = in.read()) >= 0; ) {
                    System.out.print((char) c);
                    responseContent += (char) c;
                }
                long stopTime = System.nanoTime();
                responseTime = String.valueOf((stopTime - startTime) / Math.pow(10, 9));

            } catch (Exception ignored) { ExceptionHandler.handle(12); }
        }
        finally {
            long stopTime = System.nanoTime();
            responseTime = String.valueOf((stopTime - startTime) / Math.pow(10, 9));

            printResults();
        }
    }

    /**
     * this dude, initializes all the required fields
     * @param url   URL
     * @param method    method name
     * @param headers   headers map
     * @param auth  authentication map
     * @param formData  data string
     * @param JSON  JSON Objects
     * @param followRedirect    follow redirect
     * @param filePath file path
     */
    public void start(String url, String method, HashMap<Object, Object> headers, HashMap<Object, Object> auth, String formData, JSONObject JSON, boolean followRedirect, String filePath) {
        // Initialize Thread
        this.url = url;
        this.method = method;
        this.headers = headers;
        this.auth = auth;
        this.formData = formData;
        this.JSON = JSON;
        this.followRedirect = followRedirect;
        this.filePath = filePath;

        run();  // Start thread
    }

    /**
     * Prints...
     */
    private void printResults() {
        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "\nConnection Status: " + ConsoleColors.GREEN_BOLD_BRIGHT + responseCode + ConsoleColors.RESET);
        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Response Size: " + ConsoleColors.GREEN_BOLD_BRIGHT + responseLength + ConsoleColors.RESET);
        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Elapse Time: " + ConsoleColors.GREEN_BOLD_BRIGHT + responseTime + ConsoleColors.RESET + "\n");
    }

    /**
     * Getter
     * @return size
     */
    public String getResponseLength() { return responseLength; }

    /**
     * Getter
     * @return  code
     */
    public String getResponseCode() { return responseCode; }

    /**
     * Getter
     * @return content
     */
    public String getResponseContent() { return responseContent; }

    /**
     * Getter
     * @return headers
     */
    public Map<String, List<String>> getResponseHeaders() { return responseHeaders; }

    /**
     * Getter
     * @return time
     */
    public String getResponseTime() { return responseTime; }
}