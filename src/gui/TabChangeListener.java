package gui;

/**
 * Created by hyx on 2015/6/3.
 */
public interface TabChangeListener {
    void notifyRemoveTabAt(int index);
    void notifyInsertTabAt(int index);
    void notifySelectTabAt(int index);
}
