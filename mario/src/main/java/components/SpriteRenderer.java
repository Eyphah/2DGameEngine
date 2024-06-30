package components;

import cina.Component;

public class SpriteRenderer extends Component {
    private boolean firstTime = false;

    @Override
    public void update(float dt){
        System.out.println("I am updating");
    }

    @Override
    public void start(){
        if (!firstTime){
            System.out.println("I am starting");
            firstTime = true;
        }
    }
}
