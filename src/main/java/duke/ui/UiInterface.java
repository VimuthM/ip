package duke.ui;

public interface UiInterface {
    void print(String message);
    void showWelcome(String welcomeMessage);
    void showBye(String endMessage);
    void showLoadingError(String error);
    void showError(String error);
    String readCommand();
}
