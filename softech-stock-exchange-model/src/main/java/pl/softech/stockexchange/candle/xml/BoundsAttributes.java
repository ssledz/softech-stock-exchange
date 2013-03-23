/*
 * Copyright 2013 Sławomir Śledź <slawomir.sledz@sof-tech.pl>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.softech.stockexchange.candle.xml;

import java.awt.Rectangle;
import pl.softech.stockexchange.model.PropertyName;

/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class BoundsAttributes {

    private final Rectangle bounds;

    public BoundsAttributes(Rectangle bounds) {
        this.bounds = bounds;
    }

    public BoundsAttributes() {
        this.bounds = new Rectangle();
    }

    @PropertyName(name = "bounds.x")
    public int getX() {
        return bounds.x;
    }

    @PropertyName(name = "bounds.y")
    public int getY() {
        return bounds.y;
    }

    public void setY(int y) {
        this.bounds.y =  y;
    }

    @PropertyName(name = "bounds.width")
    public int getWidth() {
        return bounds.width;
    }

    public void setWidth(int width) {
        this.bounds.width = width;
    }

    @PropertyName(name = "bounds.height")
    public int getHeight() {
        return bounds.height;
    }

    public void setHeight(int height) {
        this.bounds.height = height;
    }

    public void setX(int x) {
        this.bounds.x = x;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    
    
}
