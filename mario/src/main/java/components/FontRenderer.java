package components;

import cina.Component;

public class FontRenderer extends Component {
    @Override
    public void update(float dt){

    }

    @Override
    public void start(){
        if (gameObject.getComponent(SpriteRenderer.class)!=null){
            System.out.println("Found Font Renderer!");
        }
    }
}
