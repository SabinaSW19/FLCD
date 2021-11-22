

import java.util.List;

public class Production {
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

    private String leftHandSide;
    private List<String> rightHandSide;
    private int dotPosition;

    @Override
    public String toString() {
        return "Production{" +
                "leftHandSide='" + leftHandSide + '\'' +
                ", rightHandSide=" + rightHandSide +
                ", dotPosition=" + dotPosition +
                '}';
    }

    public Production(String leftHandSide, List<String> rightHandSide) {
        this.leftHandSide = leftHandSide;
        this.rightHandSide = rightHandSide;
        this.dotPosition = 0;
    }
}
