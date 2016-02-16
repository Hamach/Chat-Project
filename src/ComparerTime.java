import java.util.Comparator;

public class ComparerTime implements Comparator<Message> {
    @Override
    public int compare(Message a, Message b) {
        return a.getTimestamp().compareTo(b.getTimestamp());
    }
}
