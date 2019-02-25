
package comlpy.data.bus.event;

/**
 * @author lpy
 * @date 2019/2/25 11:16
 * @description 
 */
public class DownloadEvent {
    public int taskId;
    public long total;
    public long loaded;

    public DownloadEvent(int taskId, long total, long loaded) {
        this.taskId = taskId;
        this.total = total;
        this.loaded = loaded;
    }
}
