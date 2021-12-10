public class ParserOutput {
    Integer index;
    Integer parent;
    Integer rightSibling;
    String symbol;

    public ParserOutput(Integer index, Integer parent, Integer rightSibling, String symbol) {
        this.index = index;
        this.parent = parent;
        this.rightSibling = rightSibling;
        this.symbol = symbol;
    }

    public ParserOutput() {

    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public Integer getRightSibling() {
        return rightSibling;
    }

    public void setRightSibling(Integer rightSibling) {
        this.rightSibling = rightSibling;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "ParserOutput{" +
                "index=" + index +
                ", parent=" + parent +
                ", rightSibling=" + rightSibling +
                ", symbol='" + symbol + '\'' +
                '}';
    }
}
