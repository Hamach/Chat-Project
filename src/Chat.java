import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Chat {
    private List<Message> chat;
    private Logger log = Logger.INSTANCE;
    private int addedMessagesCount;
    private static final int MAX_MESSAGE_LENGTH = 140;
    private static final int MIN_AUTHOR_LENGTH = 3;
    private static final String ID_FORMAT = "id";
    private static final String AUTHOR_FORMAT = "author";
    private static final String TIMESTAMP_FORMAT = "timestamp";
    private static final String MESSAGE_FORMAT = "message";

    public Chat() {
        this.chat = new ArrayList<>();
        this.addedMessagesCount = 0;
    }

    public int getAddedMessagesCount() {
        return this.addedMessagesCount;
    }

    public boolean addMessage(Message message) {
        if (message.getMessage().length() > MAX_MESSAGE_LENGTH)
            log.add("Warning: ", "message of " + message.getAuthor() + " is very long");
        if (message.getAuthor().length() < MIN_AUTHOR_LENGTH)
            log.add("Warning: ", "name of " + message.getAuthor() + " is very short");
        int countOfEquals = 0;
        for (Message i : this.chat)
            if (i.getId().equals(message.getId()))
                countOfEquals++;
        if (countOfEquals == 0) {
            this.chat.add(message);
            this.addedMessagesCount++;
            return true;
        }
        return false;
    }

    public void addMessagesToList(String information) {
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(information);
            JSONArray jArray = (JSONArray) obj;
            JSONObject jsonObject;
            String id;
            String message;
            String author;
            String timestamp;
            for (Object aJArray : jArray) {
                jsonObject = (JSONObject) aJArray;
                id = jsonObject.get(ID_FORMAT).toString();
                message = jsonObject.get(MESSAGE_FORMAT).toString();
                author = jsonObject.get(AUTHOR_FORMAT).toString();
                timestamp = jsonObject.get(TIMESTAMP_FORMAT).toString();
                addMessage(new Message(id, message, author, timestamp));
            }
        } catch (ParseException e) {
            log.add("Error:", "parse error!");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public String toString() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        for (Message i : chat) {
            jsonObject = new JSONObject();
            jsonObject.put(ID_FORMAT, i.getId());
            jsonObject.put(AUTHOR_FORMAT, i.getAuthor());
            jsonObject.put(TIMESTAMP_FORMAT, i.getTimestamp());
            jsonObject.put(MESSAGE_FORMAT, i.getMessage());
            jsonArray.add(jsonObject);
        }
        return jsonArray.toString();
    }

    public String showMessages() {
        Collections.sort(this.chat, new ComparerTime());
        StringBuilder sb = new StringBuilder();
        for (Message i : chat) {
            sb.append(i.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    public Chat searchByAuthor(String author) {
        Chat tempChat = new Chat();
        int count = 0;
        for (Message i : this.chat)
            if (i.getAuthor().equals(author)) {
                tempChat.addMessage(i);
                count++;
            }
        log.add("Information: ", "count of " + author + " messages = " + count);
        return tempChat;
    }

    public Chat searchByToken(String token) {
        Chat tempChat = new Chat();
        int count = 0;
        for (Message i : chat)
            if (i.getMessage().contains(token)) {
                tempChat.addMessage(i);
                count++;
            }
        log.add("Information: ", "count of messages with " + token + " token = " + count);
        return tempChat;
    }


    public boolean deleteMessage(String id) {
        int countOfEquals = 0;
        for (Message i : chat)
            if (i.getId().equals(id)) {
                this.chat.remove(i);
                countOfEquals++;
                log.add("Information: ", "message with id = " + id + " was deleted");
                break;
            }
        return countOfEquals == 0;
    }

    public Chat messagesFromTimeInterval(Date startDate, Date endDate) {
        long start = startDate.getTime();
        long end = endDate.getTime();
        int count = 0;
        Chat tempChat = new Chat();
        for (Message i : this.chat) {
            if ((Long.parseLong(i.getTimestamp()) >= start) && (Long.parseLong(i.getTimestamp()) <= end)) {
                tempChat.addMessage(i);
                count++;
            }
        }
        log.add("Information: ", "Count of messages from interval you choose = " + count);
        return tempChat;
    }

    public int SearchMessagesWithRegex(String regex) {
        int count = 0;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher;
        for (Message i : this.chat) {
            matcher = pattern.matcher(i.getMessage());
            if (matcher.matches())
                count++;
        }
        log.add("Information: ", "Count of messages with regular expression " + regex + " = " + count);
        return count;
    }
}

