package com.grillo78.beycraft.capabilities;

import java.util.concurrent.Callable;

public class BladerLevelFactory implements Callable<IBladerLevel>{

    @Override
    public IBladerLevel call() throws Exception {
        return new BladerLevel();
    }

}