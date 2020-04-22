package com.grillo78.beycraft.capabilities;

import java.util.concurrent.Callable;

public class Factory implements Callable<IBladerLevel>{

    @Override
    public IBladerLevel call() throws Exception {
        return new BladerLevel();
    }

}