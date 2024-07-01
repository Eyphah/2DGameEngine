package components;

import cina.Component;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {

    private Vector4f color;

    public SpriteRenderer(Vector4f color){
        this.color = color;
    }

    @Override
    public void update(float dt){
    }

    @Override
    public void start(){
    }

    public Vector4f getColor() {
        return this.color;
    }
}
