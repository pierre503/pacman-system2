package model;

public class Fruit extends StaticTarget implements Scorable {

    protected String possibleType[] = {"Cherry","Strawberry","Orange","Orange","Apple"};

    protected String type;

    public Fruit(Position pos, int level) {
        this.state = State.AVAILABLE;
        this.setPosition(pos);
        if(level<5){
            type = possibleType[level-1];
        }else{
            type = possibleType[4];
        }
    }

    public String type(){
        return type;
    }

    @Override
    public void changeState(State s) {
        if (s == null) {
            throw new IllegalArgumentException("A null state is not allowed.");
        } else if (state == s) {
            throw new IllegalArgumentException("The new state must differ from the old one.");
        }

        if (s == State.EATEN) {
            setVisible(false);
        } else if (s == State.AVAILABLE) {
            setVisible(true);
        }

        this.state = s;
    }

    public int pointType(String type){
        switch(type){
            case("Cherry"):
                return 100;
            case("Strawberry") :
                return 300;
            case("Orange"):
                return 500;
            case ("Apple"):
                return 700;
            default:
                return 0;
        }
    }

    @Override
    public int getScore() {
        return pointType(type);
    }

    @Override
    public void gotEaten() {
        this.changeState(State.EATEN);
    }

    public String toString() {
        return "Point [" + position + ", " + state + ", visible: " + visible + "]";
    }
}
