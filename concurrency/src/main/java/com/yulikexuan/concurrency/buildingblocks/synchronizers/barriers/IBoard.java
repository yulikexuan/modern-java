//: com.yulikexuan.concurrency.buildingblocks.synchronizers.barriers.IBoard.java

package com.yulikexuan.concurrency.buildingblocks.synchronizers.barriers;


public interface IBoard {

    int getMaxX();
    int getMaxY();
    int getValue(int x, int y);
    int setNewValue(int x, int y, int value);
    boolean hasConverged();

    IBoard getSubBoard(int numPartitions, int index);

    void commitNewValues();
    void waitForConvergence();

}///:~