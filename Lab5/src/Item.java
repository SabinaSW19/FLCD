import java.util.List;
import java.util.Objects;

public class Item {
    private String leftHandSide;
    private List<String> rightHandSide;
    private int dotPosition;

    public Item(String leftHandSide, List<String> rightHandSide) {
        this.leftHandSide = leftHandSide;
        this.rightHandSide = rightHandSide;
        this.dotPosition = 0;
    }

    public String getLeftHandSide() {
        return leftHandSide;
    }

    public void setLeftHandSide(String leftHandSide) {
        this.leftHandSide = leftHandSide;
    }

    public List<String> getRightHandSide() {
        return rightHandSide;
    }

    public void setRightHandSide(List<String> rightHandSide) {
        this.rightHandSide = rightHandSide;
    }

    public int getDotPosition() {
        return dotPosition;
    }

    public void setDotPosition(int dotPosition) {
        this.dotPosition = dotPosition;
    }

    @Override
    public String toString() {
        String s = "";
        s += this.leftHandSide + "->" + this.rightHandSide + ", dot position:" + this.dotPosition;
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return dotPosition == item.dotPosition && Objects.equals(leftHandSide, item.leftHandSide) && Objects.equals(rightHandSide, item.rightHandSide);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftHandSide, rightHandSide, dotPosition);
    }
}
