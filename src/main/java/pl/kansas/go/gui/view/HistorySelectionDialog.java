package pl.kansas.go.gui.view;

import javafx.scene.control.ChoiceDialog;
import java.util.List;
import java.util.UUID;

public class HistorySelectionDialog extends ChoiceDialog<UUID> {

    public HistorySelectionDialog(List<UUID> gameIds) {
        super(null, gameIds);
        setTitle("Wybierz grę do odtworzenia");
        setHeaderText("Lista zakończonych gier:");
        setContentText("ID Gry:");
    }
}
