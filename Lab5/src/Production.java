

import java.util.List;

public class Production {
    private String leftHandSide;
    private List<String> rightHandSide;
    private int dotPosition;

    public Production(String leftHandSide, List<String> rightHandSide) {
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
        String s="";
        s+=this.leftHandSide+"->"+this.rightHandSide+", dot position:"+this.dotPosition;
        return s;
    }
}
