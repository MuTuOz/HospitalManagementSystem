package com.hospitalmanagement;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

import java.util.function.Supplier;

public class DialogUtil {

    /**
     * Attach a validator to a dialog's OK button so the dialog will not close
     * while the validator returns false. The validator should show its own
     * error messages (e.g. via ValidationUtil.showError) and simply return
     * whether the current input is valid.
     */
    public static void attachOkValidation(Dialog<?> dialog, Supplier<Boolean> validator) {
        if (dialog == null || validator == null) return;
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        if (okButton == null) return;
        okButton.addEventFilter(ActionEvent.ACTION, ev -> {
            try {
                boolean ok = validator.get();
                if (!ok) {
                    ev.consume();
                }
            } catch (Exception e) {
                // On exception, prevent closing and show a generic error
                ev.consume();
                ValidationUtil.showError("Girdi doğrulanırken hata oluştu: " + e.getMessage());
            }
        });
    }
}
