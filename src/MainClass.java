import java.io.*;
import java.util.*;

public class MainClass {
    public static void main(String[] args) {
        System.out.println("1.Add message");
        System.out.println("2.Add messages from file");
        System.out.println("3.Delete message");
        System.out.println("4.Show history");
        System.out.println("5.Search messages by author");
        System.out.println("6.Search messages by tokens");
        System.out.println("7.View message history for a certain period");
        System.out.println("8.Search messages by regular expression");
        System.out.println("9.Exit");
        Logger log = Logger.INSTANCE;
        try {
            BufferedReader br = new BufferedReader(new FileReader("log"));
            PrintStream ps = new PrintStream(new FileOutputStream("output.txt"));
            Scanner sc = new Scanner(System.in);
            Scanner sc1 = new Scanner(System.in);
            String line;
            String tempLine;
            StringBuilder sb = new StringBuilder();
            while ((tempLine = br.readLine()) != null) {
                sb.append(tempLine);
            }
            line = sb.toString();
            Chat A = new Chat();
            int a;
            boolean state = true;
            while (state) {
                a = sc.nextInt();
                switch (a) {
                    case 1: {
                        System.out.println("Enter user  name:");
                        String author = sc.nextLine();
                        while (author.length() == 0) {
                            author = sc.nextLine();
                        }
                        System.out.println("Enter message:");
                        String message = sc1.nextLine();
                        while (message.length() == 0) {
                            message = sc.nextLine();
                        }
                        Date date = new Date();
                        A.addMessage(new Message(UUID.randomUUID().toString(), message, author, ((Long) date.getTime()).toString()));
                        System.out.println("Choose operation:");
                        break;
                    }
                    case 2: {
                        A.addMessagesToList(line);
                        System.out.println("Choose operation:");
                        break;
                    }
                    case 3: {
                        System.out.println("Enter id(for example: 8a253b16-e934-4cd6-8d1d-f631ac7a8e44):");
                        String id = sc1.nextLine();
                        A.deleteMessage(id);
                        System.out.println("Choose operation:");
                        break;
                    }
                    case 4: {
                        System.out.println(A.showMessages());
                        System.out.println("Choose operation:");
                        break;
                    }
                    case 5: {
                        System.out.println("Enter author:");
                        String tempAuthor = sc1.nextLine();
                        System.out.println((A.searchByAuthor(tempAuthor)).showMessages());
                        System.out.println("Choose operation:");
                        break;
                    }
                    case 6: {
                        System.out.println("Enter token:");
                        String token = sc1.nextLine();
                        System.out.println((A.searchByToken(token)).showMessages());
                        System.out.println("Choose operation:");
                        break;
                    }
                    case 7: {
                        System.out.println("Enter start of period");
                        System.out.println("Enter number of month:");
                        int monthStart = sc1.nextInt();
                        System.out.println("Enter year:");
                        int yearStart = sc.nextInt();
                        Calendar calendarStart = new GregorianCalendar(yearStart, monthStart, 0);
                        Date startDate = calendarStart.getTime();
                        System.out.println("Enter end of period:");
                        System.out.println("Enter number of month:");
                        int monthEnd = sc1.nextInt();
                        System.out.println("Enter year:");
                        int yearEnd = sc.nextInt();
                        Calendar calendarEnd = new GregorianCalendar(yearEnd, monthEnd, 0);
                        Date endDate = calendarEnd.getTime();
                        System.out.println(A.messagesFromTimeInterval(startDate, endDate).showMessages());
                        System.out.println("Choose operation:");
                        break;
                    }
                    case 8: {
                        System.out.println("Enter regular expression:");
                        String regex = sc1.nextLine();
                        System.out.println(A.SearchMessagesWithRegex(regex));
                        System.out.println("Choose operation:");
                        break;
                    }
                    case 9: {
                        state = false;
                    }
                    default: {
                        break;
                    }
                }
            }
            log.add("Information", "Count of added messages = " + A.getAddedMessagesCount());
            ps.print(A);
            ps.close();
            br.close();
        } catch (IOException e) {
            log.add("Error: ", "Error in input/output");
            e.printStackTrace();
        } catch (InputMismatchException e) {
            log.add("Error: ", "Wrong operation!");
            System.out.println("Error!Wrong operation!");
        }
    }
}
