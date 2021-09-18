package duke;

import duke.exceptions.DukeException;
import duke.commands.Command;


public class Duke {
    private Storage storage;
    private TasksList tasks;
    private Ui ui;

    public Duke(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            storage.createStorageFile();
        } catch (DukeException e) {
            ui.showError(e.getMessage());
        }
        try {
            tasks = new TasksList(storage.load());
        } catch (DukeException e) {
            ui.showLoadingError();
            tasks = new TasksList();
        }
    }

    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (DukeException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    public static void main(String[] args) {
        new Duke("data/tasks.txt").run();
    }
}
