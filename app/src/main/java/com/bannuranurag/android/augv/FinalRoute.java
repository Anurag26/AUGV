package com.bannuranurag.android.augv;

import java.util.ArrayList;

public class FinalRoute {
    ArrayList<String> mInstruction, mManeuverBearingsBeforeAftre, maneuvering_longlat;

    public FinalRoute(ArrayList<String> mInstruction, ArrayList<String> mManeuverBearingsBeforeAftre, ArrayList<String> maneuvering_longlat) {
        this.mInstruction = mInstruction;
        this.mManeuverBearingsBeforeAftre = mManeuverBearingsBeforeAftre;
        this.maneuvering_longlat = maneuvering_longlat;
    }
}
