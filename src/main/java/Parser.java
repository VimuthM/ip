import java.time.LocalDate;
import java.time.LocalTime;

public class Parser {

    public enum Type {
        DONE,
        DELETE,
        DEADLINE,
        EVENT
    }

    public static Command parse(String command) throws DukeException {
        Command c;

        if (command.equals("bye")) {
            c = new End();
        } else if (command.equals("list")) {
            c = new List();
        } else {
            String[] words = command.split(" ");
            String mainCommand = words[0];

            switch (mainCommand) {
                case "done":
                    c = new Action(Type.DONE, words);
                    break;
                case "delete":
                    c = new Action(Type.DELETE, words);
                    break;
                case "todo": {
                    String[] split = command.split(" ");
                    if (split.length < 2) {
                        throw new MissingDescriptionException();
                    }
                    c = new Add(new ToDo(command.substring(5).trim()));
                    break;
                }
                case "deadline": {
                    c = new Add(handleFormat(command.split("/by"),
                            "`deadline ${item} /by ${time}`", Type.DEADLINE));
                    break;
                }
                case "event": {
                    c = new Add(handleFormat(command.split("/at "),
                            "`event ${item} /at ${time}`", Type.EVENT));
                    break;
                }
                default:
                    throw new UnknownCommandException();
            }
        }

        return c;
    }

    private static Task handleFormat(String[] split, String message, Type type) throws DukeException {
        Task t;
        if (split.length < 2) {
            throw new InvalidFormatException(message);
        }
        String[] datetime = split[1].trim().split(" ");
        if (datetime.length == 1) {
            try {
                LocalDate date = LocalDate.parse(datetime[0].replace("/", "-"));
                if (type == Type.DEADLINE) {
                    t = new Deadline(split[0].substring(9).trim(), date);
                } else {
                    t = new Event(split[0].substring(5).trim(), date);
                }
            } catch (Exception e) {
                throw new InvalidDateTimeException();
            }
        } else if (datetime.length == 2) {
            try {
                LocalDate date = LocalDate.parse(datetime[0].replace("/", "-"));
                String hour = datetime[1].replace(":", "").substring(0, 2);
                String minute = datetime[1].replace(":", "").substring(2, 4);
                LocalTime time = LocalTime.of(Integer.parseInt(hour), Integer.parseInt(minute));

                if (type == Type.DEADLINE) {
                    t = new Deadline(split[0].substring(9).trim(), date, time);
                } else {
                    t = new Event(split[0].substring(5).trim(), date, time);
                }
            } catch (Exception e) {
                throw new InvalidDateTimeException();
            }
        } else {
            throw new InvalidDateTimeException();
        }
        return t;
    }
}
