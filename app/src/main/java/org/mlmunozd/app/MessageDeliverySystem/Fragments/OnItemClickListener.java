package org.mlmunozd.app.MessageDeliverySystem.Fragments;

import org.mlmunozd.app.MessageDeliverySystem.Logic.Mensaje;

interface OnItemClickListener {
    void OnItemClick(Mensaje mensaje);

    void onLongItemClick(Mensaje mensaje);
}
