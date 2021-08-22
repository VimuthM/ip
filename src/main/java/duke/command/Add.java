package duke.command;

import duke.exception.DukeException;
import duke.Storage;
import duke.TaskList;
import duke.tasks.Task;
import duke.Ui;

public class Add extends Command {

    private final Task t;

    public Add(Task t) {
        this.t = t;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws DukeException {
        taskList.add(t);
        String plurality = " task";
        if (taskList.size() != 1) {
            plurality += "s";
        }

        ui.print("Got it! I've added this task:\n   "
                + t.toString() + "\nNow you have " + taskList.size()
                + plurality + " in the list.");
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
