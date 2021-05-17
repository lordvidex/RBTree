/**
 * Created by IntelliJ IDEA
 * Date: 16.05.2021
 * Time: 1:15 PM
 *
 * @author lordvidex
 * Name: Овамойо Олувадамилола Эванс
 * <p>
 * Desc:
 */
public class SpeedAnalysis {
    boolean isActionSuccess;
    long timeInNano;
    int iterationsCount;
    int item;
    int itemCount;

    public SpeedAnalysis(boolean isActionSuccess, int item,long timeInNano, int iterationsCount, int itemCount) {
        this.isActionSuccess = isActionSuccess;
        this.timeInNano = timeInNano;
        this.item = item;
        this.iterationsCount = iterationsCount;
        this.itemCount = itemCount;
    }

    @Override
    public String toString() {
        return "SpeedAnalysis{" +
                "isActionSuccess=" + isActionSuccess +
                ", timeInNano=" + timeInNano +
                ", iterationsCount=" + iterationsCount +
                ", item=" + item +
                ", itemCount=" + itemCount +
                '}';
    }
}
