package org.hupisoft.battleships;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

/**
 * Dialog fragment for confirming new game and overwriting old game.
 */
public class NewGameConfirmDialog extends AppCompatDialogFragment {

    /**
     * Callback interface for new game confirm dialog.
     */
    public interface INewGameConfirmListener
    {
        /**
         * New game is confirmed.
         */
        void onNewGameConfirmed();

        /**
         * New game is cancelled.
         */
        void onNewGameCancelled();
    }

    private INewGameConfirmListener mListener = null;

    @Override
    public Dialog onCreateDialog(Bundle savedApplicationInstance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.confirmNewGame_message);

        builder.setPositiveButton(R.string.confirmNewGame_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.onNewGameConfirmed();
            }
        });

        builder.setNegativeButton(R.string.confirmNewGame_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.onNewGameCancelled();
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (INewGameConfirmListener)context;
    }
}
